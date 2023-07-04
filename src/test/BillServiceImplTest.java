package test;
import com.payment.datapool.DataSource;
import com.payment.entitty.Bill;
import com.payment.entitty.Bill.State;
import com.payment.service.impl.BillServiceImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDate;
import java.util.List;

public class BillServiceImplTest {
    private BillServiceImpl billService;
    private List<Bill> billTable;

    @Before
    public void setUp() {
        billService = new BillServiceImpl();
        billTable = DataSource.BILL_TABLE;
    }

    @After
    public void tearDown() {
        billTable.clear();
        DataSource.BILL_NO_COUNTER = 0;
    }

    @Test
    public void testAddBill() {
        Bill bill = new Bill();
        bill.setAmount(100.0);
        bill.setDueDate(LocalDate.of(2023, 7, 1));
        bill.setType("WATER");
        bill.setProvider("SAVACO HCMC");
        bill.setState(State.NOT_PAID);
        Bill addedBill = billService.addBill(bill);
        Assert.assertNotNull(addedBill);
        Assert.assertEquals(1, addedBill.getBillNo());
        Assert.assertTrue(billTable.contains(addedBill));
    }

    @Test
    public void testUpdateBill() {
        Bill bill = new Bill();
        bill.setAmount(100.0);
        bill.setDueDate(LocalDate.of(2023, 7, 1));
        bill.setType("INTERNET");
        bill.setProvider("VNPT");
        bill.setState(State.PAID);
        billTable.add(bill);
        bill.setAmount(150.0);
        bill.setState(State.PAID);

        Bill updatedBill = billService.updateBill(bill);
        Assert.assertNotNull(updatedBill);

        // Verify that the properties of the updated Bill object match the modified values
        Assert.assertEquals(150.0, updatedBill.getAmount(), 0.0);
        Assert.assertEquals(State.PAID, updatedBill.getState());
    }

    @Test
    public void testDeleteBill() {
        Bill bill = new Bill();
        bill.setBillNo(1);
        billTable.add(bill);
        boolean result = billService.deleteBill(1);
        Assert.assertTrue(result);
        Assert.assertFalse(billTable.contains(bill));
        result = billService.deleteBill(2);
        Assert.assertFalse(result);
    }

    @Test
    public void testFindBill() {
        Bill bill1 = new Bill();
        bill1.setBillNo(1);
        bill1.setAmount(100.0);
        bill1.setDueDate(LocalDate.of(2023, 7, 1));
        bill1.setType("Electric");
        bill1.setProvider("EVN HCMC");
        bill1.setState(State.NOT_PAID);
        billTable.add(bill1);

        Bill bill2 = new Bill();
        bill2.setBillNo(2);
        bill2.setAmount(200.0);
        bill2.setDueDate(LocalDate.of(2023, 7, 2));
        bill2.setType("Water");
        bill2.setProvider("SAVACO HCMC");
        bill2.setState(State.NOT_PAID);
        billTable.add(bill2);
        List<Bill> foundBills = billService.findBill(1,1, "Electricity", null, null, State.NOT_PAID, null);
        Assert.assertEquals(1, foundBills.size());
        Bill foundBill = foundBills.get(0);
        Assert.assertEquals(1, foundBill.getBillNo());
        Assert.assertEquals(100.0, foundBill.getAmount(), 0.0);
        Assert.assertEquals(LocalDate.of(2023, 7, 1), foundBill.getDueDate());
        Assert.assertEquals("Electricity", foundBill.getType());
        Assert.assertEquals("EVN HCMC", foundBill.getProvider());
        Assert.assertEquals(State.NOT_PAID, foundBill.getState());
    }
}
