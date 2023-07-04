package com.payment.batch;

import com.payment.datapool.DataSource;
import com.payment.entitty.PaymentHistory;
import com.payment.service.AccountService;
import com.payment.service.impl.AccountServiceImpl;

import java.util.Arrays;

public class AutoPaymentPatch implements Runnable {
    private final AccountService accountService = new AccountServiceImpl();

    @Override
    public void run() {
        DataSource.PAYMENT_HISTORY_TABLE.stream().filter(paymentHistory -> paymentHistory.getState() == PaymentHistory.PaymentSate.PENDING).forEach(paymentHistory -> {
            long accountId = paymentHistory.getAccountId();
            long billId = paymentHistory.getBillNo();
            try {
                accountService.payment(accountId, Arrays.asList(billId));
            } catch (Exception e) {
                //TODO Print log
            }
            paymentHistory.setState(PaymentHistory.PaymentSate.PROCESSED);
        });
    }
}
