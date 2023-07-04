package com.payment.service;

import com.payment.entitty.Bill;

import java.time.LocalDate;
import java.util.List;

public interface BillService {
    Bill addBill(Bill bill);
    Bill updateBill(Bill bill);
    boolean deleteBill(long  billNo);
    List<Bill> findBill(long accountId);
    Bill findBillNo(long accountId,long buildNoId) throws Exception;
    List<Bill> findBill(long accountId,long billNo, String type , LocalDate dueDateFrom, LocalDate dueDateTo, Bill.State state, String provider);

    List<Bill> findBillDueDate(long accountId, LocalDate now);
}
