package vc.com.icomon.sendexception.api.retrofit.endpoint;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import vc.com.icomon.sendexception.api.DefaultMessageExceptionRequest;
import vc.com.icomon.sendexception.api.ObjectException;

public interface EndpointSendException {

    @POST("Enviar/Excecao")
    Call<DefaultMessageExceptionRequest> sendObjectException(@Body ObjectException object);
}
