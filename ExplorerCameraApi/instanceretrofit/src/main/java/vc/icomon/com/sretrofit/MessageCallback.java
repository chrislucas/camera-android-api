package vc.icomon.com.sretrofit;

/**
 * Created by r028367 on 07/03/2018.
 */

public abstract class MessageCallback {
    protected String message;
    public MessageCallback(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("Message: %s", message);
    }
}
