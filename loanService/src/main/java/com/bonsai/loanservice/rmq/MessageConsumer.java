package com.bonsai.loanservice.rmq;

import com.bonsai.loanservice.dto.LoanResponse;
import com.bonsai.sharedservice.rmq.MessagingConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-07-29
 */
@Component
public class MessageConsumer {

    @RabbitListener(queues = MessagingConfig.QUEUE)
    public void consumeLoanFromQueue(LoanResponse loanFromQueue) {
        System.out.println(loanFromQueue);
    }
}
