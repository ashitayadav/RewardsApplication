package com.demo.service;

import com.demo.dto.Customer;
import com.demo.dto.MonthlyPoints;
import com.demo.dto.RewardsResponse;
import com.demo.dto.Transaction;
import com.demo.exception.CustomerNotFoundException;
import com.demo.exception.TransactionNotFoundException;
import com.demo.repository.CustomerRepository;
import com.demo.repository.TransactionRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Log4j2
@Service
public class RewardService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;
    /**
     * method to calculate RewardPoints based upon customer and number of transactions.
     *
     * @return list  of RewardResponse as response.
     */

    public List<RewardsResponse> calculateRewardPoints(Long customerId) throws TransactionNotFoundException, CustomerNotFoundException {

        Optional<List<Customer>> customers = customerRepository.findByCustomerId(customerId);
        List<RewardsResponse> rewards = new ArrayList<>();

        if (customers.isPresent() && customers.get().isEmpty()) {
            throw new CustomerNotFoundException("No record found for this customer");
        }

        customers.ifPresent(
                customerList -> customerList.forEach(c -> {
                    Optional<List<Transaction>> transactions = transactionRepository.findByCustomerId(c.getCustomerId());
                    List<MonthlyPoints> monthlyPoints = new ArrayList<>();
                    if (transactions.isPresent() && transactions.get().isEmpty()) {
                        throw new TransactionNotFoundException("No transactions found for this customer");
                    }
                    transactions.ifPresent(
                            transactionList -> transactionList.forEach(t -> {
                                int points = calculatePoints(t.getAmount());
                                String month = t.getTransactionDate().getMonth().toString();
                                int year = t.getTransactionDate().getYear();
                                monthlyPoints.add(new MonthlyPoints(year, month, points));
                                ;
                            })
                    );
                    int totalPoints = monthlyPoints.stream().mapToInt(MonthlyPoints::getPoints).sum();
                    rewards.add(new RewardsResponse(c.getCustomerId(), c.getName(), transactions, monthlyPoints, totalPoints));
                })
        );
        return rewards;

    }


    /**
     * Calculates reward points based upon the amount of transaction.
     *
     * @param amount of the transaction.
     * @return points.
     */
    public int calculatePoints(double amount) {
        int points = 0;
        if (amount > 100) {
            points += (amount - 100) * 2;
            amount = 100;
        }
        if (amount > 50) {
            points += (amount - 50);
        }
        return points;
    }

}
