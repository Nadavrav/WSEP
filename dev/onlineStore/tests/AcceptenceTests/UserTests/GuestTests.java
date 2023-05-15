package AcceptenceTests.UserTests;

import AcceptenceTests.ProxyClasses.CreditCardProxy;
import Bridge.*;
import DomainLayer.Response;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GuestTests {

    Bridge bridge= Driver.getBridge();

    private final String StoreOwnerName = "StoreOwner";
    private final String StoreWorkerName = "StoreWorker";
    private final String BasicWorkerName = "BasicWorker";
    private final String password = "12345678";
    private final String storeName = "Super";
    private int storeId=-1;
    public void RunTests(){
        EnterStore();
        ExitStore();
        RegisterSuccess();
        RegisterFail_ExistingUserName();
        Login_Success();
        Login_Fail();
        Logout_Success();
        //TODO: MORE
    }
    @BeforeAll
    public void Setup()
    {
        int [] perms = {1,2,3,4,5,6,7,8,9,10,11};
        assertTrue(bridge.EnterMarket());
        assertTrue(bridge.Register(StoreOwnerName,password));
        assertTrue(bridge.Register(StoreWorkerName,password));
        assertTrue(bridge.Register(BasicWorkerName,password));
        assertTrue(bridge.Login(StoreOwnerName,password));
        storeId=bridge.OpenNewStore(storeName);
        assertNotEquals(-1,storeId);
        assertTrue(bridge.AddPermission(StoreWorkerName,storeId,perms));
        assertTrue(bridge.Logout());
        assertTrue(bridge.ExitMarket());
    }
    @Test
    public void EnterStore()
    {
        boolean r = bridge.EnterMarket();
        assertTrue(r);

        bridge.ExitMarket();
    }
    @Test
    public void ExitStore()
    {
        bridge.EnterMarket();
        boolean r = bridge.ExitMarket();
        assertTrue(r);
    }
    @Test
    public void ExitStoreAndReEnter()
    {
        bridge.EnterMarket();

        boolean r = bridge.ExitMarket();
        assertTrue(r);
        boolean r1 = bridge.EnterMarket();
        assertTrue(r1);

        bridge.ExitMarket();
    }

    /**
     * 1.3
     */
    @Test
    public void RegisterSuccess()
    {
        bridge.EnterMarket();

        boolean r = bridge.Register("Username","12345678");
        assertTrue(r);

        bridge.ExitMarket();
    }
    @Test
    public void RegisterFail_ExistingUserName()
    {
        bridge.EnterMarket();

        boolean r = bridge.Register("Username","12345678");
        assertTrue(r);
        boolean r1 = bridge.Register("Username","12345678");
        assertFalse(r1);

        bridge.ExitMarket();
    }

    /**
     * 1.4
     */
    @Test
    public void Login_Success()
    {
        bridge.EnterMarket();

        bridge.Register("Username","12345678");

        boolean r1 = bridge.Login("Username","12345678");
        assertTrue(r1);

        bridge.ExitMarket();
    }
    @Test
    public void Login_Fail()
    {
        bridge.EnterMarket();

        bridge.Register("Username","12345678");

        boolean r1 = bridge.Login("Username","1234567");
        assertFalse(r1);

        bridge.ExitMarket();
    }

    /**
     * 1.2
     */
    @Test
    public void Logout_Success()
    {
        bridge.EnterMarket();

        boolean r = bridge.Register("Username","12345678");
        boolean r1 = bridge.Login("Username","12345678");

        assertFalse(r1);

        bridge.ExitMarket();
    }
    @Test
    public void GetEmployeeInfo_Success()
    { //    TODO: IMPL WHEN SERVICE IMPL IS DONE
//        bridge.EnterMarket();
//
//        String [] WorkerInfo = {StoreOwnerName,"Etc",};
//        Response<String[]> r = bridge.GetEmployeeInfo(StoreOwnerName,storeName);
//        assertFalse(r.isError());
//        assertEquals(WorkerInfo, r.getValue());
//
//        bridge.ExitMarket();
    }
    @Test
    public void GetEmployeeInfo_Success_MultipleTimes()
    {//    TODO: IMPL WHEN SERVICE IMPL IS DONE
     //   bridge.EnterMarket();
//
     //   String [] OwnerInfo = {StoreOwnerName,"Etc",};
     //   String [] WorkerInfo = {StoreWorkerName,"Etc",};
     //   Response<String[]> r = bridge.GetEmployeeInfo(StoreOwnerName,storeName);
     //   assertFalse(r.isError());
     //   Response<String[]> r1 = bridge.GetEmployeeInfo(StoreWorkerName,storeName);
     //   assertFalse(r1.isError());
     //   assertEquals(OwnerInfo, r.getValue());
     //   assertEquals(WorkerInfo, r1.getValue());
//
     //   bridge.ExitMarket();
    }
    @Test
    public void GetEmployeeInfo_Fail_EmployeeDoesntExist()
    {//    TODO: IMPL WHEN SERVICE IMPL IS DONE
     //   bridge.EnterMarket();
//
     //   Response<String[]> r = bridge.GetEmployeeInfo(StoreOwnerName,storeName);
     //   assertFalse(r.isError());
     //   Response<String[]> r1 = bridge.GetEmployeeInfo(BasicWorkerName,storeName);
     //   assertTrue(r1.isError());
//
     //   bridge.ExitMarket();
    }
}
