package com.vpr42.marketplacefeedapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class MarketplaceFeedApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketplaceFeedApiApplication.class, args);
    }
}
