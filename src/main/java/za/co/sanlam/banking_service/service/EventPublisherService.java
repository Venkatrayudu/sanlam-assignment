package za.co.sanlam.banking_service.service;

import za.co.sanlam.banking_service.model.WithdrawalEvent;

public interface EventPublisherService {
    String publish(WithdrawalEvent event);
}
