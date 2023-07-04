package com.payment.service.impl;

import com.payment.datapool.DataSource;
import com.payment.entitty.Account;
import com.payment.entitty.Bill;
import com.payment.entitty.PaymentHistory;
import com.payment.entitty.Schedule;
import com.payment.service.AccountService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AccountServiceImpl implements AccountService {
    @Override
    public Account login(String username, String password) throws Exception {
        Optional<Account> result = DataSource.ACCOUNT_TABLE.stream().filter(ac->ac.getAccountName().equals(username) && ac.getPassword().equals(password)).findFirst();
        if (result.isPresent()) {
            return result.get();
        }
        throw new Exception("LOGIN FAILED");
    }

    @Override
    public double addBalance(long accountId, double balance) throws Exception {
        Optional<Account> result =  DataSource.ACCOUNT_TABLE.stream().filter(ac->ac.getAccountId() == accountId).findFirst();
        if (result.isPresent()){
            Account account = result.get();
            account.setBalance( account.getBalance() + balance);
            return account.getBalance();
        }
        throw  new Exception("NOT EXIST ACCOUNT WITH ID " + accountId);
    }

    @Override
    public List<Long> payment(long accountId, List<Long> buildNos) throws Exception {
        Account account = getAccount(accountId);
        List<Bill> bills = new ArrayList<>();
        for (Long billId : buildNos) {
           Bill b =  DataSource.BILL_TABLE.stream().filter(bill -> bill.getBillNo() == billId).findFirst().orElseThrow(() -> new Exception("Sorry! Not found a bill with such id:" + billId));
           bills.add(b);
        }
        bills.sort(Comparator.comparing(Bill::getDueDate));
        List<Long> successIds = new ArrayList<>();
        if (checkBalanceBeforePayment(account, bills)) {
            for (Bill bill : bills){
                account.setBalance(account.getBalance() - bill.getAmount());
                bill.setState(Bill.State.PAID);
                PaymentHistory paymentHistory = new PaymentHistory();
                DataSource.PAYMENT_ID_COUNTER  += 1;
                paymentHistory.setPaymentId(DataSource.PAYMENT_ID_COUNTER);
                paymentHistory.setAmount(bill.getAmount());
                paymentHistory.setBillNo(bill.getBillNo());
                paymentHistory.setState(PaymentHistory.PaymentSate.PROCESSED);
                paymentHistory.setAccountId(account.getAccountId());
                paymentHistory.setPaymentDate(LocalDate.now());
                DataSource.PAYMENT_HISTORY_TABLE.add(paymentHistory);
                successIds.add(bill.getBillNo()) ;
            }
        }else  {
            for (Bill bill : bills){
                bill.setState(Bill.State.SPENDING);
                PaymentHistory paymentHistory = new PaymentHistory();
                DataSource.PAYMENT_ID_COUNTER  += 1;
                paymentHistory.setPaymentId(DataSource.PAYMENT_ID_COUNTER);
                paymentHistory.setAmount(bill.getAmount());
                paymentHistory.setBillNo(bill.getBillNo());
                paymentHistory.setState(PaymentHistory.PaymentSate.PENDING);
                paymentHistory.setAccountId(account.getAccountId());
                paymentHistory.setPaymentDate(LocalDate.now());
                DataSource.PAYMENT_HISTORY_TABLE.add(paymentHistory);
            }
            throw  new Exception("Sorry! Not enough fund to proceed with payment.");
        }
        return successIds;
    }

    private boolean checkBalanceBeforePayment(Account account, List<Bill> bills) {
        long totalAmount = 0;
        for (Bill bill : bills){
            totalAmount+= bill.getAmount();
        }
        return account.getBalance() > totalAmount;
    }

    @Override
    public Schedule addSchedule(Schedule schedule) {
        DataSource.SCHEDULE_ID_COUNTER  += 1;
        schedule.setScheduleId(DataSource.SCHEDULE_ID_COUNTER);
        DataSource.SCHEDULE_TABLE.add(schedule);
        return schedule;
    }

    @Override
    public List<PaymentHistory> viewPaymentHistory(long accountId) {
        return DataSource.PAYMENT_HISTORY_TABLE.stream().filter(paymentHistory -> paymentHistory.getAccountId() == accountId).collect(Collectors.toList());
    }

    @Override
    public List<Schedule> findSchedule(long accountId) {
        return DataSource.SCHEDULE_TABLE.stream().filter(schedule -> schedule.getAccountId() == accountId).collect(Collectors.toList());
    }

    private  Account getAccount(long accountId) throws  Exception{
        Optional<Account> result =  DataSource.ACCOUNT_TABLE.stream().filter(ac->ac.getAccountId() == accountId).findFirst();
        if (result.isPresent()){
            return result.get();
        }
        throw  new Exception("NOT EXIST ACCOUNT WITH ID " + accountId);
    }
}
