package test;
import com.payment.datapool.DataSource;
import com.payment.entitty.Account;
import com.payment.entitty.Bill;
import com.payment.entitty.PaymentHistory;
import com.payment.entitty.Schedule;
import com.payment.service.impl.AccountServiceImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
public class AccountServiceImplTest {

    private AccountServiceImpl accountService;
    private List<Account> accountTable;
    private List<Bill> billTable;
    private List<PaymentHistory> paymentHistoryTable;
    private List<Schedule> scheduleTable;

    @Before
    public void setUp() {
        accountService = new AccountServiceImpl();
        accountTable = DataSource.ACCOUNT_TABLE;
        billTable = DataSource.BILL_TABLE;
        paymentHistoryTable = DataSource.PAYMENT_HISTORY_TABLE;
        scheduleTable = DataSource.SCHEDULE_TABLE;
    }

    @After
    public void tearDown() {
        accountTable.clear();
        billTable.clear();
        paymentHistoryTable.clear();
        scheduleTable.clear();
        DataSource.ACCOUNT_ID_COUNTER = 0;
        DataSource.PAYMENT_ID_COUNTER = 0;
        DataSource.SCHEDULE_ID_COUNTER = 0;
    }

    @Test
    public void testLogin_Successful() throws Exception {
        Account account = new Account();
        account.setAccountName("hongnv");
        account.setPassword("123456");
        accountTable.add(account);
        Account loggedInAccount = accountService.login("hongnv", "123456");
        Assert.assertNotNull(loggedInAccount);
        Assert.assertEquals("hongnv", loggedInAccount.getAccountName());
        Assert.assertEquals("123456", loggedInAccount.getPassword());
    }

    @Test(expected = Exception.class)
    public void testLogin_Failed() throws Exception {
        accountService.login("hongnv", "9999999");
    }

    @Test
    public void testAddBalance_Successful() throws Exception {
        Account account = new Account();
        account.setAccountId(1);
        account.setBalance(100.0);
        accountTable.add(account);
        double newBalance = accountService.addBalance(1, 50.0);
        Assert.assertEquals(150.0, newBalance, 0.0);
    }

    @Test(expected = Exception.class)
    public void testAddBalance_AccountNotFound() throws Exception {
        accountService.addBalance(1, 50.0);
    }

    @Test
    public void testPayment_Successful() throws Exception {
        Account account = new Account();
        account.setAccountId(1);
        account.setBalance(200.0);
        accountTable.add(account);

        Bill bill1 = new Bill();
        bill1.setBillNo(1);
        bill1.setAmount(100.0);
        billTable.add(bill1);

        Bill bill2 = new Bill();
        bill2.setBillNo(2);
        bill2.setAmount(150.0);
        billTable.add(bill2);

        List<Long> billNos = Arrays.asList(1L, 2L);

        List<Long> success = accountService.payment(1, billNos);

        Assert.assertEquals(Arrays.asList(1L, 2L), success);

        Assert.assertEquals(200.0 - (100.0 + 150.0), account.getBalance(), 0.0);

        Assert.assertEquals(Bill.State.PAID, bill1.getState());
        Assert.assertEquals(Bill.State.PAID, bill2.getState());

        Assert.assertEquals(2, paymentHistoryTable.size());

        PaymentHistory paymentHistory1 = paymentHistoryTable.get(0);
        Assert.assertEquals(1L, paymentHistory1.getPaymentId());
        Assert.assertEquals(100.0, paymentHistory1.getAmount(), 0.0);
        Assert.assertEquals(1L, paymentHistory1.getBillNo());
        Assert.assertEquals(PaymentHistory.PaymentSate.PROCESSED, paymentHistory1.getState());
        Assert.assertEquals(1L, paymentHistory1.getAccountId());
        Assert.assertEquals(LocalDate.now(), paymentHistory1.getPaymentDate());

        PaymentHistory paymentHistory2 = paymentHistoryTable.get(1);
        Assert.assertEquals(2L, paymentHistory2.getPaymentId());
        Assert.assertEquals(150.0, paymentHistory2.getAmount(), 0.0);
        Assert.assertEquals(2L, paymentHistory2.getBillNo());
        Assert.assertEquals(PaymentHistory.PaymentSate.PROCESSED.PROCESSED, paymentHistory2.getState());
        Assert.assertEquals(1L, paymentHistory2.getAccountId());
        Assert.assertEquals(LocalDate.now(), paymentHistory2.getPaymentDate());
    }

    @Test
    public void testPayment_InsufficientBalance() throws Exception {
        Account account = new Account();
        account.setAccountId(1);
        account.setBalance(50.0);
        accountTable.add(account);

        Bill bill = new Bill();
        bill.setBillNo(1);
        bill.setAmount(100.0);
        billTable.add(bill);

        List<Long> billNos = Collections.singletonList(1L);

        List<Long> success = accountService.payment(1, billNos);
        Assert.assertEquals(Collections.singletonList(1L), success);
        Assert.assertEquals(1, paymentHistoryTable.size());

        PaymentHistory paymentHistory = paymentHistoryTable.get(0);
        Assert.assertEquals(1L, paymentHistory.getPaymentId());
        Assert.assertEquals(100.0, paymentHistory.getAmount(), 0.0);
        Assert.assertEquals(1L, paymentHistory.getBillNo());
        Assert.assertEquals(PaymentHistory.PaymentSate.FAILED, paymentHistory.getState());
        Assert.assertEquals(1L, paymentHistory.getAccountId());
        Assert.assertEquals(LocalDate.now(), paymentHistory.getPaymentDate());
    }

    @Test
    public void testAddSchedule() {
        Schedule schedule = new Schedule();
        schedule.setScheduleId(1);
        scheduleTable.add(schedule);
        Schedule addedSchedule = accountService.addSchedule(schedule);
        Assert.assertNotNull(addedSchedule);
        Assert.assertEquals(1, addedSchedule.getScheduleId());
    }

    @Test
    public void testViewPaymentHistory() {
        PaymentHistory paymentHistory1 = new PaymentHistory();
        paymentHistory1.setPaymentId(1);
        paymentHistory1.setAccountId(1);
        paymentHistoryTable.add(paymentHistory1);

        PaymentHistory paymentHistory2 = new PaymentHistory();
        paymentHistory2.setPaymentId(2);
        paymentHistory2.setAccountId(2);
        paymentHistoryTable.add(paymentHistory2);

        List<PaymentHistory> paymentHistoryList = accountService.viewPaymentHistory(1);

        Assert.assertEquals(1, paymentHistoryList.size());
        Assert.assertEquals(1L, paymentHistoryList.get(0).getPaymentId());
        Assert.assertEquals(1L, paymentHistoryList.get(0).getAccountId());
    }
}
