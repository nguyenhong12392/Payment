package com.payment.service;

import com.payment.entitty.Account;
import com.payment.entitty.PaymentHistory;
import com.payment.entitty.Schedule;

import java.util.List;

public interface AccountService {
    Account login(String username,String password) throws Exception;
    double addBalance(long accountId, double amount) throws  Exception;
    List<Long> payment(long accountId, List<Long> buildNos) throws Exception;
    Schedule addSchedule( Schedule schedule) throws  Exception;
    List<PaymentHistory> viewPaymentHistory(long accountId) ;
    List<Schedule> findSchedule(long accountId);

}
