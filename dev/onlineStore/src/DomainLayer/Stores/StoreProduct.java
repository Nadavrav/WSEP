package DomainLayer.Stores;

public class StoreProduct {
    String productId;
    public String getId() {
        return productId;
    }
    public static int getStoreIdByProductId(String productId) {
        int index = productId.indexOf('-');
        String storeId= productId.substring(0,index);
        return Integer.parseInt(storeId);

    }
    public static boolean isValidProductId(String productId) {
        int index = productId.indexOf('-');
        if(index<1 || index>= productId.length())
            return false;
        return checkIfNumber(productId.substring(0,index)) && checkIfNumber(productId.substring(index,productId.length()));
    }
    public static boolean checkIfNumber(String s){
        for(int i=0;i<s.length();i++){
            if(s.charAt(i)>'9'||s.charAt(i)< '0'){
                return false;
            }
        }
        return true;
    }
}
