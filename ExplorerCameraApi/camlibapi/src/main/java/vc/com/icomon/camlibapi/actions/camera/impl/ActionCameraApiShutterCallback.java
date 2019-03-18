package vc.com.icomon.camlibapi.actions.camera.impl;

import android.hardware.Camera;
import android.util.Log;


/**
 *
 * Callback quando a imagem é capturada
 * Novamente a explicacao em ingles eh muito melhor
 *
 * Called as near as possible to the moment when a photo is captured
 * from the sensor.  This is a good opportunity to play a shutter sound
 * or give other feedback of camera operation.  This may be some time
 * after the photo was triggered, but some time before the actual data
 * is available.
 */

public class ActionCameraApiShutterCallback implements Camera.ShutterCallback {

    @Override
    public void onShutter() {
        Log.i("ON_SHUTTER", "ON_SHUTTER");
    }
}
