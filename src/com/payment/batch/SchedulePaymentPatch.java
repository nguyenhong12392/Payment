package com.payment.batch;


import com.payment.datapool.DataSource;
import com.payment.entitty.Schedule;
import com.payment.service.AccountService;
import com.payment.service.impl.AccountServiceImpl;

import java.time.LocalDate;
import java.util.Arrays;

public class SchedulePaymentPatch implements Runnable {

    private final AccountService accountService = new AccountServiceImpl();

    @Override
    public void run() {
        DataSource.SCHEDULE_TABLE.stream().filter(schedule -> schedule.getState() == Schedule.ScheduleState.UN_EXECUTE && schedule.getDueDate().equals(LocalDate.now())).forEach(schedule -> {
            long accountId = schedule.getAccountId();
            long billId = schedule.getBillNo();
            try {
                accountService.payment(accountId, Arrays.asList(billId));
            } catch (Exception e) {
                //TODO Print log
            }
            schedule.setState(Schedule.ScheduleState.EXECUTED);

        });
    }
}
