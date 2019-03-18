package vc.com.icomon.camlibapi.actions.camera.impl;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;

import vc.com.icomon.camlibapi.actions.camera.CallbackStartCamera;

public class WrapperOpenCamera {

    public static final int REQUEST_CAMERA_PERMISSIONS      = 0x01;
    public static final int REQUEST_ALL                     = 0x04;

    private Activity activity;
    private CallbackStartCamera callbackStartCamera;

    public WrapperOpenCamera(CallbackStartCamera callbackStartCamera) {
        this.callbackStartCamera = callbackStartCamera;
        this.activity = callbackStartCamera.getActivity();
    }

    private void startCamera() {
        String [] permissions = {Manifest.permission.CAMERA};
        check(permissions, REQUEST_CAMERA_PERMISSIONS);
    }

    public void startCameraWithReadAndWritePermission() {
        String [] permissions = {
            Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION
        };
        check(permissions, REQUEST_ALL);
    }

    private void check(String permissions [], int requestCode) {
        ArrayList<String> list = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED)
                list.add(permission);
        }
        if (list.size() > 0) {
            permissions = new String[list.size()];
            for (int i = 0; i < list.size(); i++)
                permissions[i] = list.get(i);
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        }
        else {
            callbackStartCamera.configureCameraPreviewIfHasPermission();
        }
    }
}
