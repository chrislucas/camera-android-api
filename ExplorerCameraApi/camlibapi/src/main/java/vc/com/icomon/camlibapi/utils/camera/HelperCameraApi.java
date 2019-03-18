package vc.com.icomon.camlibapi.utils.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.util.Log;


import java.lang.ref.WeakReference;

import vc.com.icomon.camlibapi.utils.bitmap.HelperBitmap;
import vc.com.icomon.camlibapi.utils.exception.HelperLogException;


/**
 * Created by C_Luc on 03/02/2018.
 */

public class HelperCameraApi {

    public static boolean checkCameraHardwareBack(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static boolean checkCameraHardwareFront(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
    }

    public static boolean checkIfExistsCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static Camera tryOpenCamera(Context context) {
        try {
            if(checkIfExistsCameraHardware(context)) {
                return Camera.open();
            }
        }
        catch (Exception e) {
            Log.e("EXCP_CAMERA", HelperLogException.getMessage(e));
        }
        return null;
    }

    public static Camera tryOpenCamera(Context context, @DefTypeCamera int type) {
        try {
            if(checkIfExistsCameraHardware(context)) {
                int numberOfCameras = Camera.getNumberOfCameras();
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                for (int i = 0; i < numberOfCameras; i++) {
                    Camera.getCameraInfo(i, cameraInfo);
                    if (cameraInfo.facing == type) {
                        return Camera.open(i);
                    }
                }
            }
        }
        catch (Exception e) {
            Log.e("EXCP_CAMERA", HelperLogException.getMessage(e));
        }
        return null;
    }

    public static WeakReference<Bitmap> generateBitmap(byte [] data, BitmapFactory.Options options) {
        return HelperBitmap.fromByArrayToBitmap(data, options);
    }
}
