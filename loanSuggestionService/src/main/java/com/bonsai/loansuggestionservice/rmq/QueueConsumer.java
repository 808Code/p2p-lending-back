package com.bonsai.loansuggestionservice.rmq;

import com.bonsai.accountservice.repositories.UserCredentialRepo;
import com.bonsai.sharedservice.dtos.loan.LoanInQueue;
import com.bonsai.sharedservice.rmq.MessagingConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-02
 */
@Component
@RequiredArgsConstructor
public class QueueConsumer {

    private final UserCredentialRepo userCredentialRepo;

    @RabbitListener(queues = MessagingConfig.QUEUE)
    public void consumeMessageFromQueue(LoanInQueue loanInQueue) {
        String borrower = loanInQueue.borrower();
        //checks if borrower is red or green via ml model
        //proceed only if borrower is green otherwise terminate the process
        //saves into loan suggestion table
    }
}
