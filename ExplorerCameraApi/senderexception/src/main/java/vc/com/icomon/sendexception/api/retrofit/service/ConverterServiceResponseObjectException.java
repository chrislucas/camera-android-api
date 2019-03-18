package vc.com.icomon.sendexception.api.retrofit.service;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import vc.com.icomon.sendexception.api.DefaultMessageExceptionRequest;
import vc.com.icomon.sendexception.api.retrofit.converter.ConverterObjectException;

public class ConverterServiceResponseObjectException implements Converter<ResponseBody, DefaultMessageExceptionRequest> {
    @Override
    public DefaultMessageExceptionRequest convert(@NonNull ResponseBody value) throws IOException {
        return new ConverterObjectException().fromJsonToObject(value.string());
    }
}
