package vc.com.icomon.camlibapi.actions.camera.impl;

import android.hardware.Camera;
import android.support.annotation.NonNull;

import vc.com.icomon.camlibapi.actions.camera.CallbackOnPictureTaken;

public class ActionCameraApiTakeRawPictureCallback implements Camera.PictureCallback {

    private CallbackOnPictureTaken callbackOnPictureTaken;

    public ActionCameraApiTakeRawPictureCallback(@NonNull CallbackOnPictureTaken callbackOnPictureTaken) {
        this.callbackOnPictureTaken = callbackOnPictureTaken;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        callbackOnPictureTaken.callback(data, camera);
        camera.startPreview();
    }
}
