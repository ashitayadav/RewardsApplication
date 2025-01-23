package com.retailer.rewards.controller;

import com.retailer.rewards.dto.RewardsResponse;
import com.retailer.rewards.exception.CustomerNotFoundException;
import com.retailer.rewards.exception.TransactionNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.retailer.rewards.service.RewardService;

import java.util.List;

@Slf4j
@RestController
public class RewardController {

    @Autowired
    private RewardService rewardService;

    /**
     * endpoint to calculate reward points of the customer.
     *
     * @return list of rewards segregated on the basis of months.
     */
    @GetMapping(value = "/rewards/{id}")
    public List<RewardsResponse> getRewardPoints(@PathVariable(name = "id") Long id) throws TransactionNotFoundException, CustomerNotFoundException {
        return rewardService.calculateRewardPoints(id);
    }
}
