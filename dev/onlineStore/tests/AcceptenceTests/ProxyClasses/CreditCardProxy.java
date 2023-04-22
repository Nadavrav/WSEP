package AcceptenceTests.ProxyClasses;

public class CreditCardProxy {
    private String CardNumber;
    private String CardDate;
    private String CVV;

    public CreditCardProxy() {
    }
    public void setReal()
    {
        this.CardDate = "1234 5678 9999 9999";
        this.CardDate = "12/28";
        this.CVV="012";
    }
    public void setFake() {
        this.CardDate = "0000 0000 0000 0000";
        this.CardDate = "00/00";
        this.CVV="000";
    }
}
