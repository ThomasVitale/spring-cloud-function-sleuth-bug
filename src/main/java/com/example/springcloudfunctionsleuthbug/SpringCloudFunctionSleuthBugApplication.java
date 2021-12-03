package com.example.springcloudfunctionsleuthbug;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@SpringBootApplication
public class SpringCloudFunctionSleuthBugApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudFunctionSleuthBugApplication.class, args);
    }

}

record OrderAcceptedMessage(Long orderId){}
record OrderDispatchedMessage(Long orderId){}

@Configuration
class DispatchingFunctions {

    private static final Logger log = LoggerFactory.getLogger(DispatchingFunctions.class);

    @Bean
    Function<OrderAcceptedMessage, Long> pack() {
        return orderAcceptedMessage -> {
            log.info("The order with id {} is packed.", orderAcceptedMessage.orderId());
            return orderAcceptedMessage.orderId();
        };
    }

    @Bean
    Function<Flux<Long>, Flux<OrderDispatchedMessage>> label() {
        return orderFlux -> orderFlux.map(orderId -> {
            log.info("The order with id {} is labeled.", orderId);
            return new OrderDispatchedMessage(orderId);
        });
    }

}
