package vc.com.icomon.sendexception.api;

import vc.icomon.com.sretrofit.IResponse;
import vc.icomon.com.sretrofit.MessageCallback;

public class DefaultMessageExceptionRequest implements IResponse {

    private String message;

    private int code;

    public DefaultMessageExceptionRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
