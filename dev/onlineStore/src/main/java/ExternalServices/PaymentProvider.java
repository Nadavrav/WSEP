package ExternalServices;

import java.io.IOException;

public class PaymentProvider {

  private static PaymentProvider paymentProvider =null;
   public static PaymentProvider getInstance(){
    if(paymentProvider==null){
      synchronized (PaymentProvider.class){
        if(paymentProvider == null){
          paymentProvider= new PaymentProvider();
        }
      }
    }
    return paymentProvider;
  }
   public boolean handShake() throws Exception {
        String response;
        try{
            response = HTTPPostClient.send(HTTPPostClient.makeHandshakeParams());
        }catch (Exception e){
            throw new Exception("Payment Provider Does Not Exist");
        }
        return response.equals("OK");
    }


   public String pay(String holder, String cardNumber, String expireDate, int cvv, String id) throws Exception {
        String[] date = expireDate.split("/");
        try {
            String response = HTTPPostClient.send(HTTPPostClient.makePayParams(cardNumber, date[0], date[1], holder, String.valueOf(cvv), id));
            return response;
        }catch (IOException e){
            System.out.println("Time out !");
            return "-1";
        }

    }

    public String cancel_pay(String tId) throws Exception {

        return HTTPPostClient.send(HTTPPostClient.makeCancelPayParams(tId));
    }

  
  
  
  
}
