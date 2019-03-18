package vc.com.icomon.camlibapi.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.util.Locale;

import vc.com.icomon.camlibapi.R;
import vc.com.icomon.camlibapi.actions.camera.CallbackOnPictureTaken;
import vc.com.icomon.camlibapi.actions.camera.CallbackStartCamera;
import vc.com.icomon.camlibapi.actions.camera.impl.ActionCameraApiTakeJPEGPictureCallback;
import vc.com.icomon.camlibapi.actions.camera.impl.SaveImage;
import vc.com.icomon.camlibapi.actions.camera.impl.WrapperOpenCamera;

import vc.com.icomon.camlibapi.utils.bitmap.HelperBitmap;
import vc.com.icomon.camlibapi.utils.camera.DefTypeCamera;
import vc.com.icomon.camlibapi.view.CameraPreview;

public class ActivityCamera extends AppCompatActivity implements CallbackStartCamera, CallbackOnPictureTaken {

    private WrapperOpenCamera wrapperOpenCamera;
    private CameraPreview cameraPreview;

    public static final int REQUEST_CODE                    = 0xff;
    public static final int RESULT_CODE_OK                  = Activity.RESULT_OK;
    public static final int RESULT_CODE_PROBLEM             = Activity.RESULT_CANCELED;
    public static final String BUNDLE_BYTE_ARRAY            = "BUNDLE_BYTE_ARRAY";
    public static final String BUNDLE_URI_PATH_IMAGE        = "BUNDLE_URI_PATH_IMAGE";
    public static final String BUNDLE_APP_ORIGIN            = "BUNDLE_APP_ORIGIN";
    public static final String DEFAULT_NAME_FOLDER_CAMERA   = "DEFFAULT_NAME_FOLDER_CAMERA";
    private String appName;

    private byte [] photo;

    private AlertDialog alertDialog;
    private ImageButton botaoTirarFoto;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        if (savedInstanceState != null) {
            photo = savedInstanceState.getByteArray(BUNDLE_BYTE_ARRAY);
        }
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null && bundle.keySet().contains(BUNDLE_APP_ORIGIN))
                appName = bundle.getString(BUNDLE_APP_ORIGIN);
            else
                appName = DEFAULT_NAME_FOLDER_CAMERA;
        }
        cameraPreview = findViewById(R.id.default_camera_api_preview_surface);
        botaoTirarFoto = findViewById(R.id.image_button_take_picture);
        botaoTirarFoto.setEnabled(false);
        wrapperOpenCamera = new WrapperOpenCamera(this);
        wrapperOpenCamera.startCameraWithReadAndWritePermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.putByteArray(BUNDLE_BYTE_ARRAY, photo);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            photo = savedInstanceState.getByteArray(BUNDLE_BYTE_ARRAY);
        }
    }

    public void takePicture(View view) {
        if (cameraPreview.getCamera() == null) {
            cameraPreview.tryOpenCamera(DefTypeCamera.FACING_BACK);
        }
        if (cameraPreview.getCamera() != null) {
            ActionCameraApiTakeJPEGPictureCallback takeJPEGPictureCallback = new ActionCameraApiTakeJPEGPictureCallback(this);
            cameraPreview.safeTakePicture(null, null, takeJPEGPictureCallback);
        }
    }

    private byte[] process(byte [] dataFromCamera, BitmapFactory.Options options) {
        Log.i("SIZE_OF_PHOTO_BC", String.format(Locale.getDefault()
                ,"%d", dataFromCamera.length));
        /**
         * Redimensionando a imagem que quero guardar para vincular a uma resposta
         * */
        Bitmap bitmap = HelperBitmap
                .resize(HelperBitmap.fromByArrayToBitmap(dataFromCamera, options), 640, 480).get();
        try {
            // Comprimindo em JPEG
            dataFromCamera = HelperBitmap.compressAndChangeQuality(bitmap, Bitmap.CompressFormat.JPEG, 70);
            Log.i("SIZE_OF_PHOTO_AC", String.format(Locale.getDefault()
                    ,"%d", dataFromCamera.length));
        }
        catch (IllegalArgumentException e) {
            Log.e("COMPRESS_IMAGE_ANSWER"
                    , e.getMessage() == null
                            ? "Nao foi possivel recuperar a mensagem de erro" : e.getMessage());
        }

        return dataFromCamera;
    }

    private void enviarUri(byte [] data) {
        Intent intent = new Intent();
        if (data != null) {
            Bundle bundle = new Bundle();
            Uri uri = SaveImage.saveImageOnExternalDir(data, appName);
            bundle.putParcelable(BUNDLE_URI_PATH_IMAGE, uri);
            intent.putExtras(bundle);
        }
        setResult(data != null ? RESULT_CODE_OK : RESULT_CODE_PROBLEM, intent);
        finish();
    }

    private void sendByteArray(byte [] data, BitmapFactory.Options options) {
        Intent intent = new Intent();
        if (data != null) {
            data = process(data, options);
            Bundle bundle = new Bundle();
            bundle.putByteArray(BUNDLE_BYTE_ARRAY, data);
            intent.putExtras(bundle);
        }
        setResult(data != null ? RESULT_CODE_OK : RESULT_CODE_PROBLEM, intent);
        finish();
    }

    /**
     * {@link CallbackOnPictureTaken}
     * */
    @Override
    public void callback(@Nullable byte[] dataFromCamera, Camera camera) {
        closeCamera();
        //enviarByteArray(dataFromCamera);
        enviarUri(dataFromCamera);
        // desabilito o botao porque apos a foto eu saiu dessa activit
        botaoTirarFoto.setEnabled(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] ==  PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case WrapperOpenCamera.REQUEST_ALL:
                case WrapperOpenCamera.REQUEST_CAMERA_PERMISSIONS:
                    recreate();
                    break;
            }
        }
        else {
            /**
             * Informar ao usuario que a permissao dele eh necessaria para o bom funcionamento
             * do APP
             **/
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setTitle("Permissão para acessar a câmera");
            alertDialogBuilder.setMessage("Para o funcionamento completo do App é necessário permitir o acesso a câmera");
            alertDialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            });
            alertDialog = alertDialogBuilder.create();
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });

            if(! isFinishing()) {
                alertDialog.show();
            }
        }
    }

    @Override
    public void configureCameraPreviewIfHasPermission() {
        /**
         * Acessar a SurfaceView que mostra a imagem da camera
         * */
        cameraPreview.tryOpenCamera(DefTypeCamera.FACING_BACK);
        if (cameraPreview.getCamera() != null)
            botaoTirarFoto.setEnabled(true);

    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onDestroy() {
        if (alertDialog != null && alertDialog.isShowing() && ! isFinishing()) {
            alertDialog.dismiss();
        }
        super.onDestroy();
    }

    private void closeCamera() {
        if (cameraPreview != null && cameraPreview.isCameraOpen()) {
            cameraPreview.turnOffCamera();
        }
    }
}
