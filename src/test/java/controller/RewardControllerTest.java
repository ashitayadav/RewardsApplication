package controller;

import com.demo.controller.RewardController;
import com.demo.dto.Customer;
import com.demo.dto.MonthlyPoints;
import com.demo.dto.RewardsResponse;
import com.demo.dto.Transaction;
import com.demo.exception.CustomerNotFoundException;
import com.demo.exception.TransactionNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.demo.service.RewardService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


class RewardControllerTest {

    @InjectMocks
    private RewardController rewardController;

    @Mock
    private RewardService rewardService;

    @BeforeEach
    void setUp() {
        // Initialize Mockito annotations (mocks and injects them into the RewardController)
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRewardPoints() throws CustomerNotFoundException {
        // Prepare mock data
        List<Customer> customers = List.of(
                new Customer(1L, "Albert")
        );

        List<Transaction> transactions = List.of(
                new Transaction(1L,1L, 120.0, LocalDate.of(2024, 11, 1)),
                new Transaction(2L,1L, 76.0, LocalDate.of(2024, 11, 15)),
                new Transaction(3L,2L, 120.0, LocalDate.of(2024, 12, 1)),
                new Transaction(4L,2L, 120.0, LocalDate.of(2024, 12, 1))
        );

        // Creating expected response for mock service
        RewardsResponse response = new RewardsResponse();
        response.setId(1L);
        response.setCustomerName("Albert");
        response.setMonthlyPoints(Collections.singletonList(new MonthlyPoints(2024, "NOVEMBER", 90))); // Assume 50 points for November

        List<RewardsResponse> expectedResponse = List.of(response);

        // Mock the RewardService behavior
        when(rewardService.calculateRewardPoints(1L)).thenReturn(expectedResponse);

        // Call the method in RewardController
        List<RewardsResponse> actualResponse = rewardController.getRewardPoints(1L);

        // Assertions to validate the response

        assertNotNull(actualResponse);
        assertEquals(1, actualResponse.size());
        // Verify that the RewardService's method was called with the correct parameters
        verify(rewardService, times(1)).calculateRewardPoints(1L);
    }

    @Test
    void getRewardPointsThrowsCustomerException() throws CustomerNotFoundException {
        when(rewardService.calculateRewardPoints(0L)).thenThrow(CustomerNotFoundException.class);
        assertThrows(CustomerNotFoundException.class,() -> rewardController.getRewardPoints(0L));
    }

    @Test
    void getRewardPointsThrowsTransactionException() throws TransactionNotFoundException, CustomerNotFoundException {
        when(rewardService.calculateRewardPoints(3L)).thenThrow(TransactionNotFoundException.class);
        assertThrows(TransactionNotFoundException.class, () -> rewardController.getRewardPoints(3L));
    }
}
