package Bridge;

public abstract class Driver {

    public static Bridge  getBridge(){
        return new ProxyBridge();
        //TODO: REPLACE PROXY WITH REAL DEAL
    }
}
