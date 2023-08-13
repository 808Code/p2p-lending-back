package com.bonsai.walletservice.services.impl;

import com.bonsai.accountservice.models.UserCredential;
import com.bonsai.accountservice.repositories.UserCredentialRepo;
import com.bonsai.walletservice.constants.WalletTransactionTypes;
import com.bonsai.walletservice.dtos.LoadWalletDto;
import com.bonsai.walletservice.models.Wallet;
import com.bonsai.walletservice.models.WalletTransaction;
import com.bonsai.walletservice.repositories.WalletRepo;
import com.bonsai.walletservice.repositories.WalletTransactionRepo;
import com.bonsai.walletservice.services.WalletService;
import com.bonsai.sharedservice.exceptions.AppException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-06
 */
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepo walletRepo;
    private final UserCredentialRepo userCredentialRepo;
    private final WalletTransactionRepo walletTransactionRepo;

    @Override
    public Wallet findUserWallet(String email) {
        Wallet wallet = walletRepo.findByUserEmail(email);
        if (wallet == null) {
            throw new AppException("Wallet not found", HttpStatus.BAD_REQUEST);
        }
        return wallet;
    }

    @Transactional
    @Override
    public LoadWalletDto loadWallet(BigDecimal amount, String user, String remarks) {

        //get the wallet of the given user
        Wallet wallet = findUserWallet(user);

        //add amount to wallet
        wallet.setAmount(wallet.getAmount().add(amount));

        //build transaction for this operation
        WalletTransaction walletTransaction = new WalletTransaction();
        walletTransaction.setWallet(wallet);
        walletTransaction.setDate(LocalDateTime.now());
        walletTransaction.setAmount(amount);
        walletTransaction.setType(WalletTransactionTypes.CREDIT);
        walletTransaction.setRemarks(remarks);

        //save updated wallet into database
        walletRepo.save(wallet);
        //create new transaction and save it into database
        walletTransactionRepo.save(walletTransaction);

        return new LoadWalletDto(amount,walletTransaction);
    }

    @Override
    public Map<String, BigDecimal> fetchBalanceFromWallet(String email) {
        UserCredential userCredential = userCredentialRepo.findByEmail(email)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.BAD_REQUEST));
        return walletRepo.fetchBalanceFromWallet(userCredential.getEmail());
    }

    @Override
    public List<Map<String, Object>> findAllTransactionsByUserEmail(String email) {
        UserCredential userCredential = userCredentialRepo.findByEmail(email)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.BAD_REQUEST));
        return walletTransactionRepo.findAllTransactionsByUserEmail(userCredential.getEmail());
    }

    @Override
    public UUID debitOrLockAmount(String transactionType, BigDecimal amount, String userEmail) {

        transactionType = transactionType.toUpperCase();

        //if transaction type is neither DEBIT nor LOCKED, throw an exception
        if (!transactionType.equals(WalletTransactionTypes.DEBIT)
                && !transactionType.equals(WalletTransactionTypes.LOCKED)) {
            throw new AppException("Transaction type invalid", HttpStatus.BAD_REQUEST);
        }

        //get the wallet of the given user
        Wallet wallet = findUserWallet(userEmail);

        //check whether user's balance is sufficient or not
        if (!isBalanceSufficient(userEmail, amount)) {
            throw new AppException("Sorry, your balance is insufficient", HttpStatus.BAD_REQUEST);
        }

        //build transaction for this operation
        WalletTransaction walletTransaction = new WalletTransaction();
        walletTransaction.setWallet(wallet);
        walletTransaction.setDate(LocalDateTime.now());
        walletTransaction.setAmount(amount);
        walletTransaction.setType(transactionType);
        walletTransaction.setRemarks(
                transactionType.equals(WalletTransactionTypes.DEBIT)
                        ? "Rs. " + amount + " debited from wallet."
                        : "Rs. " + amount + " locked in wallet."
        );

        //subtract amount from wallet if transaction type is DEBIT
        if (transactionType.equals(WalletTransactionTypes.DEBIT)) {
            wallet.setAmount(wallet.getAmount().subtract(amount));
            walletRepo.saveAndFlush(wallet);
        }

        //create new transaction and save it into database
        walletTransaction = walletTransactionRepo.saveAndFlush(walletTransaction);

        return walletTransaction.getId();
    }

    @Override
    public Boolean isBalanceSufficient(String userEmail, BigDecimal amount) {
        BigDecimal availableBalance = fetchBalanceFromWallet(userEmail).get("availableBalance");
        return ((availableBalance.compareTo(amount) > 0) || (availableBalance.compareTo(amount)==0));
    }

    @Override
    public void generateReport(String user,HttpServletResponse response) throws IOException {
        List<Map<String, Object>> borrowerTranReport = walletTransactionRepo.getBorrowerTranReport(user);
        export(borrowerTranReport, "report sheet", response);
    }

    public static void export(List<Map<String, Object>> list, String sheetName, HttpServletResponse response) throws IOException {
        if (list.isEmpty())
            throw new RuntimeException("Data Empty");
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);
        int rowCount = 0;
        int headerCount = 0;
        Row r = sheet.createRow(rowCount);
        List<String> sortedKeyList = list.get(0).keySet().stream().sorted().collect(Collectors.toList());
        for (String m : sortedKeyList) {
//            if (!m.equals("color")) {
            r.createCell(headerCount).setCellValue(m);
            headerCount++;
//            }
        }
        for (Map<String, Object> aBook : list) {
            Row row = sheet.createRow(++rowCount);
            int columnCount = 0;
            int count = 0;
            for (Map.Entry<String, Object> m : aBook.entrySet()) {
                Cell cell = row.createCell(columnCount);
//                if (!sortedKeyList.get(count).equals("color")) {
                cell.setCellValue(String.valueOf(aBook.get(sortedKeyList.get(count))));
                if (aBook.get("color") != null) {
                    XSSFCellStyle cellStyle = workbook.createCellStyle();
                    cellStyle.setFillForegroundColor(IndexedColors.valueOf(aBook.get("color").toString()).getIndex());
                    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    cellStyle.setBorderBottom(BorderStyle.THICK);
                    cell.setCellStyle(cellStyle);
                }

                columnCount++;
                count++;
//                }
            }
        }
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + sheetName);
        ServletOutputStream outStream = response.getOutputStream();
        workbook.write(outStream); // Write workbook to response.
        outStream.close();
    }

}
