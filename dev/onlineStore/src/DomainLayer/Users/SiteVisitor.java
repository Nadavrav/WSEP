package DomainLayer.Users;

import DomainLayer.Stores.StoreProduct;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class SiteVisitor {
    private static AtomicInteger VisitorID_GENERATOR = new AtomicInteger(1);
    protected static LinkedList<AtomicInteger> FreeVisitorID= new LinkedList<>();
    private Cart cart;
    private int visitorId;

    public SiteVisitor() throws Exception {
        try {
            this.visitorId = getNewVisitorId();
            cart = new Cart();
        }catch (Exception e){
            throw new Exception();
        }
    }
//    public SiteVisitor(int visitorId) throws Exception {//to do
//        if (checkVisitorId(visitorId)) {
//            throw new Exception("")
//        }
//    }
    public SiteVisitor(SiteVisitor other){
        visitorId=other.getVisitorId();
        cart= other.getCart();
    }
    private int getNewVisitorId() {
        if (FreeVisitorID.size() != 0) {
            return FreeVisitorID.removeFirst().get();
        }
        return VisitorID_GENERATOR.getAndIncrement();
    }

 //-------------- FreeVisitorID---------------------
    public static void ExitSiteVisitor(int id) throws Exception {//1.2
        if(!checkVisitorId(id)){
            throw new Exception("this id for user not exist or not online");
        }
        AtomicInteger atomicId = new AtomicInteger(id);
        FreeVisitorID.add(atomicId);
    }
    public static boolean checkVisitorId(int id){
        if(FreeVisitorID.contains(id)||VisitorID_GENERATOR.get()< id){//the user is not exist or not online
            return false;
        }
        return true;
    }






    public void addProductToCart(int storeId, StoreProduct product) {//2.3
        cart.addProductToCart(storeId,product);
    }

    public String cartToString() {
        return cart.cartToString();
    }

    //-----------getter / setter-----------------
    private Cart getCart() {
        return cart;
    }

    public int getVisitorId(){
        return visitorId;
    }

    public void setVisitorId(int visitorId) {
        this.visitorId = visitorId;
    }


}
