package com.payment.datapool;

import com.payment.entitty.Account;
import com.payment.entitty.Bill;
import com.payment.entitty.PaymentHistory;
import com.payment.entitty.Schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSource {

    public static long ACCOUNT_ID_COUNTER = 0;
    public static long BILL_NO_COUNTER = 0;
    public static long SCHEDULE_ID_COUNTER = 0;
    public static long PAYMENT_ID_COUNTER = 1;
    public static long SESSION_ID_COUNTER = 0;
    public static List<Account> ACCOUNT_TABLE = new ArrayList<>();
    public static List<Bill> BILL_TABLE = new ArrayList<>();
    public static List<Schedule> SCHEDULE_TABLE = new ArrayList<>();
    public static List<PaymentHistory> PAYMENT_HISTORY_TABLE = new ArrayList<>();
    public static Map<Long,Account> SESSION_MAP = new HashMap<>();
    static {
        ACCOUNT_ID_COUNTER +=1;
        Account account = new Account(ACCOUNT_ID_COUNTER,"user1","user1@gmail.com","0123456789","123456",300000);
        ACCOUNT_TABLE.add(account);
        Bill electric = new Bill(account.getAccountId(),BILL_NO_COUNTER+=1, "ELECTRIC",200000, LocalDate.of(2023,6,25),"EVN HCMC" );
        Bill water =  new Bill(account.getAccountId(),BILL_NO_COUNTER+=1, "WATER",200000, LocalDate.of(2023,5,30),"SAVACO HCMC" );
        Bill internet =  new Bill(account.getAccountId(),BILL_NO_COUNTER+=1, "INTERNET",200000, LocalDate.of(2023,7,27),"VNPT" );
        Bill shopping =  new Bill(account.getAccountId(),BILL_NO_COUNTER+=1, "SHOPPING",200000, LocalDate.of(2023,7,4),"CHANEL" );
        Bill electric2 =  new Bill(account.getAccountId(),BILL_NO_COUNTER+=1, "ELECTRIC",170000, LocalDate.of(2023,6,4),"EVN HCMC" );
        Bill restaurant =  new Bill(account.getAccountId(),BILL_NO_COUNTER+=1, "RESTAURANT",2000000, LocalDate.of(2023,7,15),"HUONG PHO" );
        Bill market =  new Bill(account.getAccountId(),BILL_NO_COUNTER+=1, "SUPPER MARKET",750000, LocalDate.of(2023,7,22),"Aeon" );
        Bill travel =  new Bill(account.getAccountId(),BILL_NO_COUNTER+=1, "TRAVEL",570000, LocalDate.of(2023,7,17),"ROSE HOTEL" );
        BILL_TABLE.add(electric);
        BILL_TABLE.add(water);
        BILL_TABLE.add(internet);
        BILL_TABLE.add(shopping);
        BILL_TABLE.add(electric2);
        BILL_TABLE.add(restaurant);
        BILL_TABLE.add(market);
        BILL_TABLE.add(travel);
    }
}
