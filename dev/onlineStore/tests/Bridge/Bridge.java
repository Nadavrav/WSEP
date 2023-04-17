package Bridge;

import DomainLayer.Facade;
import DomainLayer.Response;

public interface Bridge {


    Response<?> login(String UserName, String Password);



}
