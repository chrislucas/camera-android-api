package vc.icomon.com.sretrofit;

import java.util.List;

/**
 * Created by r028367 on 07/03/2018.
 */

public interface CallbackRequestAPI<T, Message extends MessageCallback> {
    void onSuccessRequest(T data);
    void onSuccessRequest(List<T> data);
    void onFailureRequest(Message message);
}
