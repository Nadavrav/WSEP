package Bridge;

public abstract class Driver {
    /**
     *
     * @return returns either the proxy or, when it is done, the bridge which calls the service layer
     */
    public static Bridge  getBridge(){
        return new ProxyBridge();
        //TODO: REPLACE PROXY WITH REAL DEAL
    }
}
