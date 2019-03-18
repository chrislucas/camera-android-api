package vc.icomon.com.sretrofit;

import java.util.List;

/**
 * Interface responsavel por enviar uma mensagem
 * */

public interface CallbackRequestAPI2<Response extends IResponse> {
    void onSuccessRequest(Response data);
    void onSuccessRequest(List<Response> data);
    void onnFailureRequest(Response response);
}
