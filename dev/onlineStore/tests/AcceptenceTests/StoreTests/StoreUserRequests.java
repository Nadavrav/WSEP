package AcceptenceTests.StoreTests;

import Bridge.Bridge;
import Bridge.Driver;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
public class StoreUserRequests {
    Bridge bridge= Driver.getBridge();
    public void RuntTests(){
        test1();
        //TODO: MORE
    }
    @Test
    public void test1(){
        String userName="Bobby";
        String password="BobbyIsKing331";
        assertFalse(bridge.login(userName,password).isError());
    }
}
