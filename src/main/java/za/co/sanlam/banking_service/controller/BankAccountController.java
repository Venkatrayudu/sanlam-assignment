package za.co.sanlam.banking_service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import za.co.sanlam.banking_service.model.WithdrawalResponse;
import za.co.sanlam.banking_service.service.WithdrawalService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/bank")
@Slf4j
public class BankAccountController {

    private final WithdrawalService withdrawalService;

    public BankAccountController(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }

    @PostMapping(value = "/withdraw", params = "version=1")
    public String withdrawV1(@RequestParam("accountId") Long accountId,
                           @RequestParam("amount") BigDecimal amount) {
        log.info("BankAccountController -> withdrawV1()");
        return withdrawalService.withdraw(accountId, amount);
    }

    @PostMapping(value = "/withdraw", params = "version=2")
    public ResponseEntity<WithdrawalResponse> withdrawV2(@RequestParam("accountId") Long accountId,
                                                         @RequestParam("amount") BigDecimal amount) {
        log.info("BankAccountController -> withdrawV2()");
        WithdrawalResponse withdrawalResponse = new WithdrawalResponse();
        String response = withdrawalService.withdraw(accountId, amount);
        if ("Withdrawal successful".equalsIgnoreCase(response)) {
            withdrawalResponse.setResponse(response);
            withdrawalResponse.setStatus("SUCCESS");
            withdrawalResponse.setResponseCode(202);
            return new ResponseEntity<>(withdrawalResponse, HttpStatus.ACCEPTED);
        } else {
            withdrawalResponse.setResponse(response);
            withdrawalResponse.setStatus("FAILURE");
            withdrawalResponse.setResponseCode(400);
            return new ResponseEntity<>(withdrawalResponse, HttpStatus.BAD_REQUEST);
        }
    }
}

