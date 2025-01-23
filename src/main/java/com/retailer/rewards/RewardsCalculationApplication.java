package com.retailer.rewards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.demo.repository")
@EntityScan(basePackages = {"com.demo.dto"})
public class RewardsCalculationApplication {
    public static void main(String[] args) {
        SpringApplication.run(RewardsCalculationApplication.class, args);
    }
}