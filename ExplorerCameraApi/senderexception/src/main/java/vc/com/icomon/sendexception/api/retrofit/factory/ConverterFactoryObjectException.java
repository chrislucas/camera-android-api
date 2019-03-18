package vc.com.icomon.sendexception.api.retrofit.factory;

import android.support.annotation.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import vc.com.icomon.sendexception.api.retrofit.service.ConverterServiceRequestObjectException;
import vc.com.icomon.sendexception.api.retrofit.service.ConverterServiceResponseObjectException;

public class ConverterFactoryObjectException extends Converter.Factory {

    public ConverterFactoryObjectException() {
        super();
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new ConverterServiceResponseObjectException();
    }

    @Nullable
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new ConverterServiceRequestObjectException();
    }

    @Nullable
    @Override
    public Converter<?, String> stringConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return super.stringConverter(type, annotations, retrofit);
    }
}
