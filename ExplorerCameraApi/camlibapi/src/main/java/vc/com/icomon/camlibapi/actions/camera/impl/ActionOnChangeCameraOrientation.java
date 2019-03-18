package vc.com.icomon.camlibapi.actions.camera.impl;

import android.content.Context;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.OrientationEventListener;


public class ActionOnChangeCameraOrientation extends OrientationEventListener {

    private Camera camera;

    private void init(Camera camera) {
        this.camera = camera;
    }

    public ActionOnChangeCameraOrientation(@NonNull Context context, @NonNull Camera camera) {
        super(context);
        init(camera);
    }

    public ActionOnChangeCameraOrientation(@NonNull Context context, @NonNull Camera camera, int rate) {
        super(context, rate);
        init(camera);

    }

    @Override
    public void onOrientationChanged(int degree) {
        //WindowManager windowManager=  (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        /**
         * Estudar a implementacao sugerida no comentario do metodo
         * {@link Camera.Parameters#setRotation(int)}
         * orientation = (orientation + 45) / 90 * 90;
         * */

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(0, info);
        int rotation = 0;
        /**
         *
         * 0,90,180,270
         * info.orientation
         * */
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            rotation = (info.orientation + degree) % 360;
            rotation = (360 - rotation) % 360;
        }
        else {
            // back-facing camera
            rotation = (info.orientation - degree + 360) % 360;
        }
        Log.i("ON_ORIENTATION_CHANGE"
                , String.format("Angulo: %d.\nOrientacao da Camera: %d\nCamera: %s\nRotacao: %d"
                        , degree
                        , info.orientation
                        , info.facing == Camera.CameraInfo.CAMERA_FACING_BACK ? "FACING_BACK" : "FACING_FRONT"
                        , rotation
                )
        );
        Camera.Parameters parameters = camera.getParameters();
        parameters.setRotation(rotation);
        camera.stopPreview();
        camera.setParameters(parameters);
        camera.setDisplayOrientation(rotation);
        camera.startPreview();
    }
}
