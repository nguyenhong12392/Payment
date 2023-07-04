package com.payment.entitty;

import java.time.LocalDate;

public class PaymentHistory {
    private long paymentId;
    private long accountId;
    private long billNo;
    private double amount;
    private LocalDate paymentDate;
    private PaymentSate state;

    public PaymentHistory() {
    }

    public PaymentHistory(long paymentId, long accountId, long billNo, double amount, LocalDate paymentDate, PaymentSate state) {
        this.paymentId = paymentId;
        this.accountId = accountId;
        this.billNo = billNo;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.state = state;
    }

    public long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public long getBillNo() {
        return billNo;
    }

    public void setBillNo(long billNo) {
        this.billNo = billNo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentSate getState() {
        return state;
    }

    public void setState(PaymentSate state) {
        this.state = state;
    }

    public enum PaymentSate {
        PROCESSED, PENDING,FAILED;
    }

}
