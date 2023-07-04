package com.payment.service.impl;

import com.payment.datapool.DataSource;
import com.payment.entitty.Bill;
import com.payment.service.BillService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BillServiceImpl implements BillService {
    @Override
    public Bill addBill(Bill bill) {
        DataSource.BILL_NO_COUNTER += 1;
        bill.setBillNo(DataSource.BILL_NO_COUNTER);
        DataSource.BILL_TABLE.add(bill);
        return bill;
    }

    @Override
    public Bill updateBill(Bill bill) {
        int index =  DataSource.BILL_TABLE.indexOf(bill);
        if (index > -1) {
            Bill old = DataSource.BILL_TABLE.get(index);
            old.setAmount(bill.getAmount());
            old.setDueDate(bill.getDueDate());
            old.setType(bill.getType());
            old.setProvider(bill.getProvider());
            old.setState(bill.getState());
            return old;
        }
        return null;
    }

    @Override
    public boolean deleteBill(long billNo) {
        Optional<Bill> result =  DataSource.BILL_TABLE.stream().filter(bill -> bill.getBillNo() == billNo).findFirst();
        if (result.isPresent()) {
            DataSource.BILL_TABLE.remove(result.get());
            return  true;
        }
        return false;
    }

    @Override
    public List<Bill> findBill(long accountId) {
        return DataSource.BILL_TABLE.stream().filter(bill -> bill.getAccountId() == accountId).collect(Collectors.toList());
    }

    @Override
    public Bill findBillNo(long accountId, long billNo) throws Exception {
        return DataSource.BILL_TABLE.stream().filter(bill -> bill.getAccountId() == accountId && bill.getBillNo() == billNo).findFirst().orElseThrow(() -> new Exception("Sorry! Not found a bill with such id:" + billNo));
    }

    @Override
    public List<Bill> findBill(long accountId,long billNo, String type, LocalDate dueDateFrom, LocalDate dueDateTo, Bill.State state, String provider) {
        return DataSource.BILL_TABLE.stream().filter(bill -> {
            boolean accountCon = bill.getAccountId() == accountId;
            boolean buildNoCond = (billNo <= 0 || billNo == bill.getBillNo());
            boolean typeCond = (type == null || type.isEmpty() || bill.getType().contains(type));
            boolean providerCond = (provider == null || provider.isEmpty() || bill.getProvider().contains(provider));
            boolean stateCond = (state == null || bill.getState().equals(state));
            boolean dueDateCond;
            if ((dueDateFrom != null && dueDateTo != null)) {
                dueDateCond = bill.getDueDate().isAfter(dueDateFrom) && bill.getDueDate().isBefore(dueDateTo);
            } else {
                if (dueDateTo != null) {
                    dueDateCond = (bill.getDueDate().isBefore(dueDateTo));
                } else if (dueDateFrom != null) {
                    dueDateCond = (bill.getDueDate().isAfter(dueDateFrom));
                }else {
                    dueDateCond = true;
                }
            }
            return  accountCon && buildNoCond && typeCond  && providerCond && stateCond && dueDateCond;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Bill> findBillDueDate(long accountId, LocalDate now) {
        return DataSource.BILL_TABLE.stream().filter(bill -> {
            boolean accountCon = bill.getAccountId() == accountId;
            boolean dueDateCond = bill.getDueDate().isEqual(now) || bill.getDueDate().isBefore(now);
            boolean stateCond = bill.getState().equals(Bill.State.NOT_PAID);
            return  accountCon && dueDateCond  && stateCond;
        }).collect(Collectors.toList());
    }
}
