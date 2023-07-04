package com.payment.api;

import com.payment.batch.AutoPaymentPatch;
import com.payment.batch.SchedulePaymentPatch;
import com.payment.datapool.DataSource;
import com.payment.entitty.Account;
import com.payment.entitty.Bill;
import com.payment.entitty.PaymentHistory;
import com.payment.entitty.Schedule;
import com.payment.service.AccountService;
import com.payment.service.BillService;
import com.payment.service.impl.AccountServiceImpl;
import com.payment.service.impl.BillServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PaymentAPI {
    private final AccountService accountService = new AccountServiceImpl();
    private final BillService billService = new BillServiceImpl();
    public PaymentAPI() {
        SchedulePaymentPatch schedulePaymentPatch = new SchedulePaymentPatch();
        AutoPaymentPatch autoPaymentPatch = new AutoPaymentPatch();
        ScheduledExecutorService executors =  Executors.newScheduledThreadPool(4);
        executors.scheduleAtFixedRate(schedulePaymentPatch,0,2,TimeUnit.MINUTES );
        executors.scheduleAtFixedRate(autoPaymentPatch,0,2,TimeUnit.MINUTES );

    }


    public long login(String username, String password){
        try {
            Account account = accountService.login(username, password);
            long sessionId = DataSource.SESSION_ID_COUNTER += 1;
            DataSource.SESSION_MAP.put(sessionId,account);
            return sessionId;
        }catch (Exception e){
            return -1;
        }

    }

    public String addBalance(long sessionId, double balance) {
        Account account = DataSource.SESSION_MAP.get(sessionId);
        account.setBalance(account.getBalance() + balance);
        DataSource.SESSION_MAP.put(sessionId,account);
        System.out.println(DataSource.ACCOUNT_TABLE);
        return "Your available balance: " + account.getBalance();
    }

    public String listBill(long sessionId) {
        Account account = DataSource.SESSION_MAP.get(sessionId);
        List<Bill> bills = billService.findBill(account.getAccountId());
        return buildBillResponse(bills);
    }

    public String payment(long sessionId, List<Long> buildNos) {
        Account account = DataSource.SESSION_MAP.get(sessionId);
        StringBuilder sb = new StringBuilder();
        try {
            List<Long> paymentIds =  accountService.payment(account.getAccountId(),buildNos);
            paymentIds.forEach(paymentId->
                sb.append("Payment has been completed for Bill with id :").append(paymentId).append("\n")
            );
        }catch (Exception e) {
            return e.getMessage();
        }

        return sb.toString();
    }

    public String listBillByDueDate(long sessionId) {
        Account account = DataSource.SESSION_MAP.get(sessionId);
        List<Bill> bills = billService.findBillDueDate(account.getAccountId(), LocalDate.now());
        return buildBillResponse(bills);
    }

    public String schedule(long sessionId, long billNoId, LocalDate scheduleDate) {
        Account account = DataSource.SESSION_MAP.get(sessionId);
        try {
            Bill bill = billService.findBillNo(account.getAccountId(),billNoId);
            bill.setState(Bill.State.SCHEDULED);
            Schedule schedule = new Schedule();
            schedule.setAccountId(account.getAccountId());
            schedule.setBillNo(bill.getBillNo());
            schedule.setDueDate(scheduleDate);
            accountService.addSchedule(schedule);
            return new StringBuilder().append("Payment for bill id ").append(billNoId).append(" is scheduled on").append(scheduleDate).toString();
        }catch (Exception e) {
            return  e.getMessage();
        }


    }

    public String listPayment(long sessionId) {
        Account account = DataSource.SESSION_MAP.get(sessionId);
        List<PaymentHistory>  paymentHistories =  accountService.viewPaymentHistory(account.getAccountId());
        StringBuilder sb = new StringBuilder();
        //HEADER
        sb.append("No.").append("\t").append("Amount").append("\t").append("Payment Date").append("\t").append("State").append("\t").append("Bill Id");
        sb.append("\n");
        //BODY
        paymentHistories.forEach(b-> {
            sb.append(b.getPaymentId()).append("\t").append(b.getAmount()).append("\t").append(b.getPaymentDate()).append("\t").append(b.getState().name()).append("\t").append(b.getBillNo());
            sb.append("\n");
        });
        return sb.toString();
    }

    public String listBillByProvider(long sessionId, String providerName) {
        Account account = DataSource.SESSION_MAP.get(sessionId);
        List<Bill> bills = billService.findBill(account.getAccountId(),0,null,null,null,null,providerName);
        return buildBillResponse(bills);
    }

    public String listSchedule(long sessionId) {
        Account account = DataSource.SESSION_MAP.get(sessionId);
        List<Schedule> schedules = accountService.findSchedule(account.getAccountId());
        StringBuilder sb = new StringBuilder();
        sb.append("No.").append("\t").append("DueDate").append("\t").append("BillNo").append("\t").append("Due Date");
        sb.append("\n");
        schedules.forEach(s -> {
            sb.append(s.getScheduleId()).append("\t").append(s.getDueDate()).append("\t").append(s.getBillNo());
            sb.append("\n");
        });
        return sb.toString();
    }

    private String buildBillResponse(List<Bill> bills) {
        StringBuilder sb = new StringBuilder();
        //HEADER
        sb.append("Bill No.").append("\t").append("Type").append("\t").append("Amount").append("\t").append("Due Date").append("\t").append("State").append("\t").append("PROVIDER");
        sb.append("\n");
        bills.forEach(b-> {
            sb.append(b.getBillNo()).append("\t").append(b.getType()).append("\t").append(b.getAmount()).append("\t").append(b.getDueDate()).append("\t").append(b.getState().name()).append("\t").append(b.getProvider());
            sb.append("\n");
        });
        //BODY
        return sb.toString();
    }


}
