package com.rewardtracker.backend.service;

import com.rewardtracker.backend.model.*;
import com.rewardtracker.backend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RewardsDTOServiceTest {

    @Mock private TransactionRecordsRepository transactionRepository;
    @Mock private BankCardRewardsRepository rewardsRepository;
    @Mock private UserCreditCardRepository userCreditCardRepository;

    @InjectMocks private RewardsDTOService rewardsDTOService;

    private UserCreditCard mockUserCard;
    private final Long cardId = 1L;
    private final LocalDate today = LocalDate.now();

    @BeforeEach
    void setUp() {
        mockUserCard = new UserCreditCard();
        mockUserCard.setId(cardId);
        mockUserCard.setOpenMonth(Month.JANUARY);
        BankCreditCard bankCard = new BankCreditCard();
        bankCard.setId(100L);
        mockUserCard.setBankCreditCard(bankCard);
    }

    @Test
    void testComplexRewardsScenario() {

        BankCardRewards monthlyUber = new BankCardRewards();
        monthlyUber.setId(1L);
        monthlyUber.setMerchantType("UBER");
        monthlyUber.setType("CREDIT");
        monthlyUber.setPerPeriodAmount(10.0);
        monthlyUber.setFrequency("MONTHLY");


        BankCardRewards semiDell = new BankCardRewards();
        semiDell.setId(2L);
        semiDell.setMerchantType("DELL");
        semiDell.setType("CREDIT");
        semiDell.setPerPeriodAmount(50.0);
        semiDell.setFrequency("SEMI_ANNUAL");


        BankCardRewards annualLulu = new BankCardRewards();
        annualLulu.setId(3L);
        annualLulu.setMerchantType("LULULEMON");
        annualLulu.setType("CREDIT");
        annualLulu.setPerPeriodAmount(75.0);
        annualLulu.setFrequency("ANNUAL");


        BankCardRewards diningPoints = new BankCardRewards();
        diningPoints.setId(4L);
        diningPoints.setMerchantType("DINING");
        diningPoints.setType("POINTS");
        diningPoints.setRewardRate(4.0);


        BankCardRewards othersPoints = new BankCardRewards();
        othersPoints.setId(5L);
        othersPoints.setMerchantType("OTHERS");
        othersPoints.setType("POINTS");
        othersPoints.setRewardRate(2.0);


        BankCardRewards milestone = new BankCardRewards();
        milestone.setId(6L);
        milestone.setMerchantType("ALL_SPEND");
        milestone.setType("MILESTONE");
        milestone.setTotalAmount(75000.0);


        BankCardRewards globalEntry = new BankCardRewards();
        globalEntry.setId(7L);
        globalEntry.setMerchantType("GLOBAL_ENTRY_CREDIT");
        globalEntry.setCalculationType("TIME");
        globalEntry.setType("BENEFIT");


        BankCardRewards centurion = new BankCardRewards();
        centurion.setId(8L);
        centurion.setMerchantType("CENTURION_GUEST_ACCESS");
        centurion.setType("MILESTONE");
        centurion.setTotalAmount(75000.0);

        List<BankCardRewards> rules = Arrays.asList(
            monthlyUber, semiDell, annualLulu, 
            diningPoints, othersPoints, milestone, 
            globalEntry, centurion
        );


        TransactionRecords t1 = new TransactionRecords();
        t1.setMerchantType("UBER"); t1.setAmount(15.0); t1.setDate(today);

        TransactionRecords t2 = new TransactionRecords();
        t2.setMerchantType("DELL"); t2.setAmount(60.0); t2.setDate(today);

        TransactionRecords t3 = new TransactionRecords();
        t3.setMerchantType("LULULEMON"); t3.setAmount(200.0); t3.setDate(today);

        TransactionRecords t4 = new TransactionRecords();
        t4.setMerchantType("DINING"); t4.setAmount(100.0); t4.setDate(today);

        List<TransactionRecords> transactions = Arrays.asList(t1, t2, t3, t4);

        when(userCreditCardRepository.findById(cardId)).thenReturn(Optional.of(mockUserCard));
        when(rewardsRepository.findByBankCreditCard_Id(100L)).thenReturn(rules);
        when(transactionRepository.findByUserCreditCardId(cardId)).thenReturn(transactions);


        List<RewardsDTO> results = rewardsDTOService.getCalculatedRewards(cardId);


        assertEquals(10.0, find(results, "UBER").getUsedAmount(), "Uber Credit max $10");
        assertEquals(50.0, find(results, "DELL").getUsedAmount(), "Dell Credit max $50");
        assertEquals(75.0, find(results, "LULULEMON").getUsedAmount(), "Lulu Credit max $75");
        assertEquals(400.0, find(results, "DINING").getUsedAmount(), "Dining 4x points on $100");
        

        assertEquals(280.0, find(results, "OTHERS").getUsedAmount(), "OTHERS points should be 280");


        assertEquals(375.0, find(results, "ALL_SPEND").getUsedAmount(), "Milestone should track all spend");
        assertEquals(1.0, find(results, "GLOBAL_ENTRY_CREDIT").getUsedAmount(), "Time benefit should be active (1.0)");
        assertEquals(375.0, find(results, "CENTURION_GUEST_ACCESS").getUsedAmount(), "Secondary milestone should track all spend");
    }

    private RewardsDTO find(List<RewardsDTO> list, String merchantType) {
        return list.stream()
            .filter(d -> d.getMerchantType().equals(merchantType))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Merchant type " + merchantType + " not found in results"));
    }
}