package vc.com.icomon.camlibapi.actions.camera;

import android.hardware.Camera;

public interface CallbackOnPictureTaken {
    void callback(byte [] dataFromCamera, Camera camera);
}
