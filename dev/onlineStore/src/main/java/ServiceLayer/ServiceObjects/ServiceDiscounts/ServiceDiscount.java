package ServiceLayer.ServiceObjects.ServiceDiscounts;


import DomainLayer.Stores.Discounts.Discount;

public class ServiceDiscount {
    public final String description;
    public final int id;


    public ServiceDiscount(String description,int id){
        this.description=description;
        this.id=id;
    }

    public ServiceDiscount(Discount discount) {
        description=discount.getDescription();
        id=discount.getId();
    }

}
