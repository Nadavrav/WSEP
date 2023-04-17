package DomainLayer;

public class Response<T> {
    T value;
    String message;
    boolean isError;

    public Response(String message, boolean isError, T value){
        this.message = message;
        this.isError = isError;
        this.value = value;
    }

    public Response(String message, boolean isError){
        this.message = message;
        this.isError = isError;
        this.value = null;
    }

    public Response(T value){
        this.message = "";
        this.isError = false;
        this.value = value;
    }

    public T getValue(){
        return value;
    }

    public boolean isError(){
        return isError;
    }

    public String getMessage(){
        return message;
    }


}
