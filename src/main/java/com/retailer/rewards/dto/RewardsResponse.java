package com.retailer.rewards.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardsResponse {

    private Long id;

    private String customerName;

    private Optional<List<Transaction>> transaction;

    private List<MonthlyPoints> monthlyPoints;

    private int totalPoints;
}
