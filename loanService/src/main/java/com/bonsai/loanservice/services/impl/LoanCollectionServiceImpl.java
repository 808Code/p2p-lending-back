package com.bonsai.loanservice.services.impl;

import com.bonsai.accountservice.constants.Roles;
import com.bonsai.accountservice.models.UserCredential;
import com.bonsai.accountservice.repositories.UserCredentialRepo;
import com.bonsai.loanservice.constants.LoanStatus;
import com.bonsai.loanservice.models.LoanCollection;
import com.bonsai.loanservice.models.LoanRequest;
import com.bonsai.loanservice.repositories.LendingRepo;
import com.bonsai.loanservice.repositories.LoanCollectionRepo;
import com.bonsai.loanservice.repositories.LoanRequestRepo;
import com.bonsai.loanservice.services.LoanCollectionService;
import com.bonsai.sharedservice.exceptions.AppException;
import com.bonsai.walletservice.constants.WalletTransactionTypes;
import com.bonsai.walletservice.models.Wallet;
import com.bonsai.walletservice.models.WalletTransaction;
import com.bonsai.walletservice.repositories.WalletRepo;
import com.bonsai.walletservice.repositories.WalletTransactionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanCollectionServiceImpl implements LoanCollectionService {

    private final LoanCollectionRepo loanCollectionRepo;
    private final LoanRequestRepo loanRequestRepo;
    private final UserCredentialRepo userCredentialRepo;
    private final WalletTransactionRepo transactionRepo;
    private final WalletRepo walletRepo;
    private final LendingRepo lendingRepo;

    @Override
    public Long getLoanCollectionAmount(UUID loanId) {
        return loanCollectionRepo.findCollectedAmountByLoanId(loanId);
    }

    @Transactional
    @Override
    public UUID save(UUID loanId, String lenderEmail, UUID transactionId) {
        LoanRequest loanRequest = loanRequestRepo.findById(loanId)
                .orElseThrow(() -> new AppException("Invalid loan id", HttpStatus.BAD_REQUEST));

        UserCredential lender = userCredentialRepo.findByEmailAndRole(lenderEmail, Roles.LENDER)
                .orElseThrow(() -> new AppException("Invalid lender id", HttpStatus.BAD_REQUEST));

        WalletTransaction walletTransaction = transactionRepo.findById(transactionId)
                .orElseThrow(() -> new AppException("Invalid transaction id", HttpStatus.BAD_REQUEST));

        LoanCollection collection = new LoanCollection();
        collection.setLoanRequest(loanRequest);
        collection.setLender(lender);
        collection.setWalletTransaction(walletTransaction);

        collection = loanCollectionRepo.saveAndFlush(collection);
        return collection.getId();
    }

    @Transactional
    @Override
    public UUID fulfillLoan(UUID loanId) {
        //fetch loanRequest from database
        LoanRequest loanRequest = loanRequestRepo.findById(loanId)
                .orElseThrow(() -> new AppException("Invalid loan id", HttpStatus.BAD_REQUEST));

        //if loan request is already fulfilled we can't process further
        if (loanRequest.getLoanStatus().equals(LoanStatus.FULFILLED)) {
            throw new AppException("Loan is already fulfilled", HttpStatus.BAD_REQUEST);
        }

        //fetch all the loan collections associated with the loan request
        List<LoanCollection> loanCollectionList = loanCollectionRepo.findAllByLoanRequestId(loanId);

        //if there are no loan collections, loan has not been fulfilled
        if (loanCollectionList.isEmpty()) {
            throw new AppException("No any loan collection found", HttpStatus.BAD_REQUEST);
        }

        //process each transaction of loan collection individually
        for (LoanCollection loanCollection : loanCollectionList) {
            //extract lender's wallet transaction for given loan collection
            WalletTransaction transaction = loanCollection.getWalletTransaction();
            //if transaction type is "LOCKED" change it into "DEBIT" and deduct the transaction amount
            //from the respective wallet. Save updated transaction and wallet into database
            if (transaction.getType().equals(WalletTransactionTypes.LOCKED)) {

                Wallet wallet = transaction.getWallet();
                wallet.setAmount(wallet.getAmount().subtract(transaction.getAmount()));
                transaction.setType(WalletTransactionTypes.DEBIT);

                //create lending whenever there occurs a "DEBIT" instantly
                //using lending service gave rise to circular dependency issue so, repo was used
                lendingRepo.createLending(
                        //lendingId
                        UUID.randomUUID(),
                        //lent date
                        LocalDateTime.now(),
                        //lender id
                        wallet.getUser().getId(),
                        //loan id
                        loanId,
                        //transaction id
                        transaction.getId()
                );

                walletRepo.saveAndFlush(wallet);
                transactionRepo.saveAndFlush(transaction);
            }
        }
        //set loan status to "FULFILLED"
        loanRequest.setLoanStatus(LoanStatus.FULFILLED);
        loanRequest.setRemainingAmount(0L);
        loanRequest = loanRequestRepo.saveAndFlush(loanRequest);
        return loanRequest.getId();
    }

    @Override
    public void deleteAllByLoanRequestId(UUID loanId) {
        try {
            loanCollectionRepo.deleteAllByLoanRequestId(loanId);
        } catch (Exception e) {
            throw new AppException("Loan collections can't be deleted", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
