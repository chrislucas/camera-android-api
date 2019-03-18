package vc.com.icomon.camlibapi.actions.camera;

import android.app.Activity;


public interface CallbackStartCamera {
    void configureCameraPreviewIfHasPermission();
    Activity getActivity();
}
