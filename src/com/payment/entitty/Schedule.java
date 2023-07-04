package com.payment.entitty;

import java.time.LocalDate;

public class Schedule {
    private long scheduleId;
    private long billNo;
    private long accountId;
    private LocalDate dueDate;
    private ScheduleState state;

    public Schedule(long scheduleId, long billNo, long accountId, LocalDate dueDate) {
        this.scheduleId = scheduleId;
        this.billNo = billNo;
        this.accountId = accountId;
        this.dueDate = dueDate;
        state = ScheduleState.UN_EXECUTE;
    }

    public Schedule() {
        state = ScheduleState.UN_EXECUTE;
    }

    public long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public long getBillNo() {
        return billNo;
    }

    public void setBillNo(long billNo) {
        this.billNo = billNo;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public ScheduleState getState() {
        return state;
    }

    public void setState(ScheduleState state) {
        this.state = state;
    }

    public enum ScheduleState {
        UN_EXECUTE, EXECUTED;
    }
}
