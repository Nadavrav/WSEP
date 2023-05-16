package ExternalServices;

public class PaymentProvider {
  
  public PaymentProvider(){
  }
  
  public boolean applyTransaction(double amount,int cardNumber){
    return true;
  }
  
  private boolean isValid(int cardNumber){
    return true;
  }
  
  
  
  
  
}
