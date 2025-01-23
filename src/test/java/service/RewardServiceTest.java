package service;

import com.retailer.rewards.dto.Customer;
import com.retailer.rewards.dto.RewardsResponse;
import com.retailer.rewards.dto.Transaction;
import com.retailer.rewards.exception.CustomerNotFoundException;
import com.retailer.rewards.exception.TransactionNotFoundException;
import com.retailer.rewards.repository.CustomerRepository;
import com.retailer.rewards.repository.TransactionRepository;
import com.retailer.rewards.service.RewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RewardServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private RewardService rewardService;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateRewardPoints() throws CustomerNotFoundException {
        // Prepare mock data
        List<Customer> customers = List.of(
                new Customer(1L, "Albert"));
        when(customerRepository.findByCustomerId(1L)).thenReturn(Optional.of(customers));

        List<Transaction> transactions = List.of(
                new Transaction(1L,1L, 120.0, LocalDate.of(2024, 11, 1)),
                new Transaction(2L,1L, 76.0, LocalDate.of(2024, 11, 15))
        );
        when(transactionRepository.findByCustomerId(1L)).thenReturn(Optional.of(transactions));

        // Call the method under test
        List<RewardsResponse> rewardPoints = rewardService.calculateRewardPoints(1L);

        // Assertions for Albert
        RewardsResponse response = rewardPoints.get(0);
        assertEquals("Albert", response.getCustomerName());
        assertEquals(2, response.getMonthlyPoints().size());
        assertEquals(116, response.getTotalPoints());
    }

    @Test
    void testCalculateRewardPointsNoTransactions() {
        // Prepare mock data with no transactions
        List<Customer> customers = List.of(
                new Customer(1L, "Albert"),
                new Customer(2L, "Dani")
        );
        when(customerRepository.findByCustomerId(1L)).thenReturn(Optional.of(customers));
        List<Transaction> transactions = List.of(); // No transactions
        when(transactionRepository.findByCustomerId(1L)).thenReturn(Optional.of(transactions));
        // Assertions to ensure no points are given when there are no transactions
        assertThrows(TransactionNotFoundException.class, () -> rewardService.calculateRewardPoints(1L));
    }

    @Test
    void testCalculateRewardPointsMultipleTransactionsSameMonth() throws CustomerNotFoundException {
        // Prepare mock data with multiple transactions for the same month
        List<Customer> customers = List.of(
                new Customer(1L, "Albert")
        );
        when(customerRepository.findByCustomerId(1L)).thenReturn(Optional.of(customers));
        List<Transaction> transactions = List.of(
                new Transaction(1L,1L, 120.0, LocalDate.of(2024, 11, 1)),
                new Transaction(2L,1L, 80.0, LocalDate.of(2024, 12, 15))
        );
        when(transactionRepository.findByCustomerId(1L)).thenReturn(Optional.of(transactions));
        // Call the method under test
        List<RewardsResponse> rewardPoints = rewardService.calculateRewardPoints(1L);

        // Assertions for Albert (same month, two transactions)
        RewardsResponse albertResponse = rewardPoints.get(0);
        assertEquals("Albert", albertResponse.getCustomerName());
        assertEquals(2, albertResponse.getMonthlyPoints().size());
        assertEquals(90, albertResponse.getMonthlyPoints().get(0).getPoints());
        assertEquals(30, albertResponse.getMonthlyPoints().get(1).getPoints());
    }

    @Test
    void testCalculateRewardsWithNoCustomer() throws CustomerNotFoundException {
        // Prepare mock data with a single transaction for a customer
        List<Customer> customers = List.of();
        when(customerRepository.findByCustomerId(0L)).thenReturn(Optional.of(customers));
        assertThrows(CustomerNotFoundException.class, () -> rewardService.calculateRewardPoints(0L));
    }

    @Test
    void testCalculatePointsEdgeCase() {
        // Edge case: Transaction with exactly 50, 100, and higher values
        assertEquals(0, rewardService.calculatePoints(50.0));
        assertEquals(0, rewardService.calculatePoints(50.01));
        assertEquals(90, rewardService.calculatePoints(120.0));
        assertEquals(150, rewardService.calculatePoints(150.0));
    }
}