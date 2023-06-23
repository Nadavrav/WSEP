package ExternalServices;

import java.util.List;

public class Supplier {


  private static Supplier supplier =null;
  public static Supplier getInstance(){
    if(supplier==null){
      synchronized (PaymentProvider.class){
        if(supplier == null){
          supplier = new Supplier();
        }
      }
    }
    return supplier;
  }
  public boolean handShake() throws Exception {
    String response;
    try{
      response = HTTPPostClient.send(HTTPPostClient.makeHandshakeParams());
    }catch (Exception e){
      throw new Exception("Suplier Does Not Exist");
    }
    return response.equals("OK");
  }


  public String supply(String name, String address, String city, String country, String zip) throws Exception {

    String response = HTTPPostClient.send(
            HTTPPostClient.makeSupplyParams(name, address, city, country, zip)
    );

    if(isNumeric(response)){
      return response;
    }
    return "-1";

  }

  public boolean cancel_supply(String transaction_id) throws Exception{

    String response = HTTPPostClient.send(
            HTTPPostClient.makeCancelSupplyParams(transaction_id)
    );

    return !response.equals("-1");
  }

  public static boolean isNumeric(String strNum) {
    if (strNum == null) {
      return false;
    }
    try {
      double d = Double.parseDouble(strNum);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }



  ////////////////// to delete /////////////////
  public boolean isValidAddress(String address){
    return true;
  }
  
  public boolean supplyProducts(List<String> products){
    return true;
  }
  
  
}
