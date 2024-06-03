package za.co.sanlam.banking_service.service.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import za.co.sanlam.banking_service.model.WithdrawalEvent;
import za.co.sanlam.banking_service.service.EventPublisherService;
import za.co.sanlam.banking_service.service.WithdrawalService;

import java.math.BigDecimal;

@Service
public class WithdrawalServiceImpl implements WithdrawalService {

    private final JdbcTemplate jdbcTemplate;
    private final EventPublisherService eventPublisherService;

    public WithdrawalServiceImpl(JdbcTemplate jdbcTemplate, EventPublisherService eventPublisherService) {
        this.jdbcTemplate = jdbcTemplate;
        this.eventPublisherService = eventPublisherService;
    }

    @Override
    public String withdraw(Long accountId, BigDecimal amount) {
        // Check current balance
        String sql = "SELECT balance FROM accounts WHERE id = ?";
        BigDecimal currentBalance = jdbcTemplate.queryForObject(
                sql, new Object[]{accountId}, BigDecimal.class);
        if (currentBalance != null && currentBalance.compareTo(amount) >= 0) {
            // Update balance
            sql = "UPDATE accounts SET balance = balance - ? WHERE id = ?";
            int rowsAffected = jdbcTemplate.update(sql, amount, accountId);
            if (rowsAffected > 0) {
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
}
