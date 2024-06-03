package za.co.sanlam.banking_service.service;

import java.math.BigDecimal;

public interface WithdrawalService {

    String withdraw(Long accountId, BigDecimal amount);
}
