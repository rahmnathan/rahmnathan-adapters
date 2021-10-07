package com.github.rahmnathan.google.pushnotification.config;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PushNotificationResponseProcessor implements Processor {
    private final Logger logger = LoggerFactory.getLogger(PushNotificationResponseProcessor.class);

    @Override
    public void process(Exchange exchange) {
        Integer responseCode = exchange.getIn().getHeader(Exchange.HTTP_RESPONSE_CODE, Integer.class);
        if(responseCode == null){
            HttpOperationFailedException exception = exchange.getException(HttpOperationFailedException.class);
            if(exception != null) {
                responseCode = exception.getStatusCode();
            }
        }

        logger.info("Response code from push notification send: {}", responseCode);
    }
}
