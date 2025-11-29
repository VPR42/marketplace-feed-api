package com.vpr42.marketplacefeedapi;

import com.vpr42.marketplacefeedapi.config.properties.CoverProperties;
import com.vpr42.marketplacefeedapi.config.properties.MinioProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableDiscoveryClient
@EnableConfigurationProperties({
    MinioProperties.class,
    CoverProperties.class
})
public class MarketplaceFeedApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketplaceFeedApiApplication.class, args);
    }
}
