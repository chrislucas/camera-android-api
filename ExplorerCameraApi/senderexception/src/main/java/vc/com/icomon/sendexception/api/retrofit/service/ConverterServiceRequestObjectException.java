package vc.com.icomon.sendexception.api.retrofit.service;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;
import vc.com.icomon.sendexception.api.ObjectException;
import vc.com.icomon.sendexception.api.retrofit.converter.ConverterObjectException;
import vc.icomon.com.sretrofit.MediaTypeConstant;

public class ConverterServiceRequestObjectException implements Converter<ObjectException, RequestBody> {

    @Override
    public RequestBody convert(@NonNull ObjectException value) {
        return RequestBody.create(MediaType.parse(MediaTypeConstant.MEDIA_TYPE_JSON)
                , new ConverterObjectException().fromObjectToJson(value));
    }
}
