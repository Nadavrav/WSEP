package AcceptenceTests.UserTests;

import Bridge.*;
import DomainLayer.Response;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GuestTests {

    Bridge bridge= Driver.getBridge();

    private final String StoreFounder = "StoreFounder";
    private final String StoreManagerName = "StoreManager";
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
    }
    @BeforeAll
    public void Setup()
    {
        //  int [] perms = {1,2,3,4,5,6,7,8,9,10,11};
        assertTrue(bridge.EnterMarket());
        assertTrue(bridge.Register(StoreFounder,password));
        assertTrue(bridge.Register(StoreManagerName,password));
        assertTrue(bridge.Register(BasicWorkerName,password));
        assertTrue(bridge.Login(StoreFounder,password));
        storeId=bridge.OpenNewStore(storeName);
        assertNotEquals(-1,storeId);
        assertTrue(bridge.AppointManager(StoreManagerName,storeId));
        //assertTrue(bridge.AddPermission(StoreManagerName,storeId,perms));
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

        boolean r = bridge.Register("Username1","12345678");
        assertTrue(r);
        boolean r1 = bridge.Register("Username1","12345678");
        assertFalse(r1);

        bridge.ExitMarket();
    }
    @Test
    public void Login_Success()
    {
        bridge.EnterMarket();

        boolean r1 = bridge.Login(StoreFounder,password);
        assertTrue(r1);

        boolean r2 = bridge.Logout();

        bridge.ExitMarket();
    }
    @Test
    public void Login_Fail()
    {
        bridge.EnterMarket();

        boolean r1 = bridge.Login(StoreFounder,"11111111");
        assertFalse(r1);

        bridge.ExitMarket();
    }
    @Test
    public void Logout_Success()
    {
        bridge.EnterMarket();

        boolean r1 = bridge.Login(StoreFounder,password);

        assertTrue(r1);

        boolean r2 = bridge.Logout();
        assertTrue(r2);

        bridge.ExitMarket();
    }

    //TODO might need to remove
    /*@Test
    public void GetEmployeeInfo_Success()
    { //
        bridge.EnterMarket();

        String [] WorkerInfo = {StoreFounder,"Etc",};
        Response<String[]> r = bridge.GetEmployeeInfo(StoreFounder,storeId);
        assertFalse(r.isError());
        assertEquals(WorkerInfo, r.getValue());

        bridge.ExitMarket();
    }
    @Test
    public void GetEmployeeInfo_Success_MultipleTimes()
    {//
        bridge.EnterMarket();

        String [] OwnerInfo = {StoreFounder,"Etc",};
        String [] WorkerInfo = {StoreManagerName,"Etc"};
        Response<String[]> r = bridge.GetEmployeeInfo(StoreFounder,storeId);
        assertFalse(r.isError());
        Response<String[]> r1 = bridge.GetEmployeeInfo(StoreManagerName,storeId);
        assertFalse(r1.isError());
        assertEquals(OwnerInfo, r.getValue());
        assertEquals(WorkerInfo, r1.getValue());

        bridge.ExitMarket();
    }
    @Test
    public void GetEmployeeInfo_Fail_EmployeeDoesntExist()
    {//
        bridge.EnterMarket();

        Response<String[]> r = bridge.GetEmployeeInfo(StoreFounder,storeId);
        assertFalse(r.isError());
        Response<String[]> r1 = bridge.GetEmployeeInfo(BasicWorkerName,storeId);
        assertTrue(r1.isError());

        bridge.ExitMarket();
    }*/
}
