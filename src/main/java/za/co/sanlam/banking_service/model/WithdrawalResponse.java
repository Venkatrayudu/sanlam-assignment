package za.co.sanlam.banking_service.model;

import lombok.Data;

@Data
public class WithdrawalResponse {
    private int responseCode;
    private String response;
    private String status;
}
