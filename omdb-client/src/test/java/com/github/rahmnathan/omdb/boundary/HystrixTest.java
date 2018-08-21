package com.github.rahmnathan.omdb.boundary;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HystrixTest extends CamelTestSupport {
    private final Logger logger = LoggerFactory.getLogger(HystrixTest.class);

    @Before
    public void initialize() throws Exception {
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                onException(Exception.class)
                        .useExponentialBackOff()
                        .maximumRedeliveries(3)
                        .redeliveryDelay(500);

                errorHandler(deadLetterChannel("mock:dead").maximumRedeliveries(5).redeliveryDelay(100));

//                from("timer://foo?period=200")
                from("direct:test")
                        .hystrix()
                            .inheritErrorHandler(true)
                            .process(exchange -> System.out.println("Executing hystrix."))
                            .throwException(new IllegalArgumentException())
                        .endHystrix()
                        .onFallback()
                            .process(exchange -> System.out.println("Executing fallback."))
                        .end()
                        .end();
            }
        });
    }

    @Test
    public void hystrixTest() throws Exception {
        template.request("direct:test", Exchange::getIn);
//       Thread.sleep(60000);
    }
}
