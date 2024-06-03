package za.co.sanlam.banking_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Account {
    @Id
    private Long accountId;
    private BigDecimal amount;
}
