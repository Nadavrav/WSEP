package com.okayjava.html.CommunicateToServer;

public class Alert {
    private boolean success;
    private boolean fail;
    private String message;

    private static Alert alert = null;
    public static Alert getInstance(){
        if (alert == null){
            alert = new Alert();
        }
        return alert;
    }

    private Alert(){
        success = false;
        fail = false;
        message = "";
    }

    private Alert(boolean success, boolean fail, String message){
        this.success = success;
        this.fail = fail;
        this.message = message;
    }

    public void reset(){
        success = false;
        fail = false;
        message = "";
    }

    public Alert copy(){
        return new Alert(this.success, this.fail, this.message);
    }

    public void setSuccess(boolean success) {
        this.success = success;
        this.fail = !success;
    }

    public boolean isFail() {
        return fail;
    }

    public void setFail(boolean fail) {
        this.fail = fail;
        this.success = !fail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static Alert getAlert() {
        return alert;
    }

    public boolean isSuccess(){
        return success;
    }


}
