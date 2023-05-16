package ServiceLayer.ServiceObjects;

import ServiceLayer.ServiceObjects.ServiceProducts.ServiceProduct;

public class PurchaseRecord {
    private final ServiceProduct serviceProduct;
    private final String userName;

    public PurchaseRecord(ServiceProduct serviceProduct, String userName) {
        this.serviceProduct = serviceProduct;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public ServiceProduct getServiceProduct() {
        return serviceProduct;
    }
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof PurchaseRecord eq){
            return userName.equals(eq.getUserName()) && serviceProduct.equals(eq.getServiceProduct());
        }
        return false;
    }
}
