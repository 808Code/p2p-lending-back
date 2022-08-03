package com.bonsai.loansuggestionservice.rmq;

import com.bonsai.accountservice.models.UserCredential;
import com.bonsai.accountservice.repositories.UserCredentialRepo;
import com.bonsai.loansuggestionservice.services.LoanSuggestionService;
import com.bonsai.sharedservice.dtos.loan.LoanInQueue;
import com.bonsai.sharedservice.exceptions.AppException;
import com.bonsai.sharedservice.rmq.MessagingConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-02
 */
@Component
@RequiredArgsConstructor
public class QueueConsumer {

    private final UserCredentialRepo userCredentialRepo;
    private final LoanSuggestionService loanSuggestionService;

    @RabbitListener(queues = MessagingConfig.QUEUE)
    public void consumeMessageFromQueue(LoanInQueue loanInQueue) {
        String borrower = loanInQueue.borrower();
        //checks if borrower is red or green via ml model
        //proceed only if borrower is green otherwise terminate the process

        List<UserCredential> lenders = userCredentialRepo.findAllActiveLenders();
        if (lenders.isEmpty()) {
            //haha i_am_teapot status cool laagyo ani throw gardiye
            throw new AppException("Loan request of id " + loanInQueue.id() + " can't be suggested to any lenders",
                    HttpStatus.I_AM_A_TEAPOT);
        }

        loanSuggestionService.save(loanInQueue.id(), lenders);
    }
}
