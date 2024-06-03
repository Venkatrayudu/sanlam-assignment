package za.co.sanlam.banking_service.service.impl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import za.co.sanlam.banking_service.model.Account;
import za.co.sanlam.banking_service.model.WithdrawalEvent;
import za.co.sanlam.banking_service.repository.AccountsRepository;
import za.co.sanlam.banking_service.service.EventPublisherService;
import za.co.sanlam.banking_service.service.WithdrawalService;

import java.math.BigDecimal;

@Service
public class WithdrawalServiceImpl implements WithdrawalService {

    private final AccountsRepository accountsRepository;
    private final EventPublisherService eventPublisherService;

    public WithdrawalServiceImpl(EventPublisherService eventPublisherService, AccountsRepository accountsRepository) {
        this.eventPublisherService = eventPublisherService;
        this.accountsRepository = accountsRepository;
    }

    @Override
    @CircuitBreaker(name = "fallbackActivity", fallbackMethod = "fallbackActivityForDB")
    public String withdraw(Long accountId, BigDecimal amount) {
        // Check current balance
        Account account = accountsRepository.findById(accountId).get();
        BigDecimal currentBalance = account.getAmount();
        if (currentBalance != null && currentBalance.compareTo(amount) >= 0) {
            // Update balance
            account.setAmount(currentBalance.subtract(amount));
            Account accountUpdated = accountsRepository.save(account);
            if (accountUpdated.getAccountId().equals(accountId)) {
                // After a successful withdrawal, publish a withdrawal event to SNS
                WithdrawalEvent event = new WithdrawalEvent(amount, accountId, "SUCCESSFUL");
                eventPublisherService.publish(event);
                return "Withdrawal successful";
            } else {
                // In case the update fails for reasons other than a balance check
                return "Withdrawal failed";
            }
        } else {
            // Insufficient funds
            return "Insufficient funds for withdrawal";
        }
    }

    public String fallbackActivityForDB(Throwable throwable) {
        return "Internal issue! Please try after sometime!!";
    }
}
