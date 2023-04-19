package AcceptenceTests.UserTests;

import Bridge.*;
import DomainLayer.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class CartTests {
    Bridge bridge= Driver.getBridge();

    @BeforeEach
    public void Setup()
    {
        bridge.EnterMarket();
       // bridge.AddProduct("Mega milk","Guaranteed to make bones stronger!");
       // bridge.AddProduct("Ultra milk","Bones made of metal now!");
       // bridge.AddProduct("Giga milk","bones made of diamond now!");
    }
    @AfterEach
    public void Breakdown()
    {
        bridge.ExitMarket();
    }
    @Test
    public void Save_Product()
    {

    }
}
