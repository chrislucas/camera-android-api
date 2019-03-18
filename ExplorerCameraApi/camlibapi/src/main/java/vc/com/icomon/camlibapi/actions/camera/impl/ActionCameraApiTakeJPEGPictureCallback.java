package vc.com.icomon.camlibapi.actions.camera.impl;

import android.hardware.Camera;
import android.support.annotation.NonNull;

import vc.com.icomon.camlibapi.actions.camera.CallbackOnPictureTaken;


/**
 * Callback com um array de bytes com a imagem em formato JPEG
 * */

public class ActionCameraApiTakeJPEGPictureCallback implements Camera.PictureCallback {

    private CallbackOnPictureTaken callbackOnPictureTaken;

    public ActionCameraApiTakeJPEGPictureCallback(@NonNull CallbackOnPictureTaken callbackOnPictureTaken) {
        this.callbackOnPictureTaken = callbackOnPictureTaken;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        callbackOnPictureTaken.callback(data, camera);
        camera.startPreview();
    }
}
