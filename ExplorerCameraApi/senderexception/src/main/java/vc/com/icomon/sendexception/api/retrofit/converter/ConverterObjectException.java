package vc.com.icomon.sendexception.api.retrofit.converter;

import vc.com.icomon.sendexception.api.DefaultMessageExceptionRequest;
import vc.com.icomon.sendexception.api.ObjectException;

public class ConverterObjectException {


    public String fromObjectToJson(ObjectException object) {
        String in = "";

        return in;
    }

    public DefaultMessageExceptionRequest fromJsonToObject(String json) {
        DefaultMessageExceptionRequest defaultMessageExceptionRequest = new DefaultMessageExceptionRequest("");
        return defaultMessageExceptionRequest;
    }

}
