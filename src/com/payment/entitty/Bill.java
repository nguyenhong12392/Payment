package com.payment.entitty;

import java.time.LocalDate;
import java.util.Objects;

public class Bill {
    private long accountId;
    private long billNo;
    private String type;
    private double amount;
    private LocalDate dueDate;
    private State state;
    private String provider;

    public Bill() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bill bill = (Bill) o;
        return billNo == bill.getBillNo();
    }

    @Override
    public int hashCode() {
        return Objects.hash(billNo);
    }

    public Bill(long accountId, long billNo, String type, double amount, LocalDate dueDate, String provider) {
        this.accountId = accountId;
        this.billNo = billNo;
        this.type = type;
        this.amount = amount;
        this.dueDate = dueDate;
        this.state = State.NOT_PAID;
        this.provider = provider;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public enum  State{
        NOT_PAID , PAID , SPENDING,SCHEDULED
    }
}




