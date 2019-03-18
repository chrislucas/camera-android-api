package vc.com.icomon.camlibapi.utils.camera;

import android.hardware.Camera;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.SOURCE)
@IntDef({DefTypeCamera.FACING_BACK, DefTypeCamera.FACING_FRONT})
public @interface DefTypeCamera {
    int FACING_BACK = Camera.CameraInfo.CAMERA_FACING_BACK;
    int FACING_FRONT = Camera.CameraInfo.CAMERA_FACING_FRONT;
}
