package vc.com.icomon.camlibapi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;



import java.io.IOException;
import java.util.List;
import java.util.Locale;

import vc.com.icomon.camlibapi.actions.camera.CameraControl;
import vc.com.icomon.camlibapi.actions.camera.impl.ActionOnChangeCameraOrientation;
import vc.com.icomon.camlibapi.utils.camera.DefTypeCamera;
import vc.com.icomon.camlibapi.utils.camera.HelperCameraApi;
import vc.com.icomon.camlibapi.utils.exception.HelperLogException;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback2, CameraControl {

    private Camera camera;
    private SurfaceHolder surfaceHolder;

    private boolean cameraOpen = false;
    private Context context;

    public boolean isCameraOpen() {
        return cameraOpen;
    }

    private ActionOnChangeCameraOrientation actionOnChangeCameraOrientation;

    private void init(Context context) {
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);
        this.surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        this.context = context;
    }

    public CameraPreview(Context context) {
        super(context);
        init(context);
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CameraPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CameraPreview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    /**
     * {@link SurfaceHolder.Callback2}
     * apos surfaceChanged
     * */
    @Override
    public void surfaceRedrawNeeded(SurfaceHolder holder) {
        if (holder != null) { }
    }
    /**
     * {@link SurfaceHolder.Callback2}
     *  Metodo executado apos o construtor
     * */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder != null) {}
    }

    private void test(SurfaceHolder holder) {
        SurfaceHolder surfaceHolder = getHolder();
        Canvas canvas = surfaceHolder.lockCanvas();
        surfaceHolder.unlockCanvasAndPost(canvas);
    }
    /**
     * {@link SurfaceHolder.Callback2}
     * Metodo executado apos {@link CameraPreview#surfaceCreated(SurfaceHolder)}
     * */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        turnOnCamera();
    }

    /**
     * {@link SurfaceHolder.Callback2}
     * */
    @Override
    public void surfaceRedrawNeededAsync(SurfaceHolder holder, Runnable drawingFinished) {
        if (holder != null) { }
    }

    public void tryOpenCamera(@DefTypeCamera int type) {
        this.camera = HelperCameraApi.tryOpenCamera(context, type);
    }

    public Camera getCamera() {
        return camera;
    }

    private void controlOrientationByRotation(int rotation) {
        switch (rotation) {
            case Surface.ROTATION_0:
                /**
                 * modificar a orientacao do objeto Parameters afeta a imagem a orientacao
                 * da foto tirada pelo usuario que pode ser capturada no callback na implementacao
                 * da class {@link Camera.PictureCallback#onPictureTaken(byte[], Camera)}
                 * */
                // camera.setDisplayOrientation(0);
                // modificar a orientacao da camera afeta a View que eh motrada para o usuario
                // parameters.setRotation(0);
                actionOnChangeCameraOrientation.onOrientationChanged(0);
                break;
            case Surface.ROTATION_90:
                actionOnChangeCameraOrientation.onOrientationChanged(90);
                break;
            case Surface.ROTATION_180:
                actionOnChangeCameraOrientation.onOrientationChanged(180);
                break;
            case Surface.ROTATION_270:
                actionOnChangeCameraOrientation.onOrientationChanged(270);
                break;
        }
    }

    private void setParameterMinSizeOfPicture(Camera.Parameters parameters) {
        List<Camera.Size> pictureSizes = parameters.getSupportedPictureSizes();
        Camera.Size maxPictureSize = pictureSizes.get(0);
        for(Camera.Size size : pictureSizes) {
            if (maxPictureSize.width > size.width && maxPictureSize.height > size.height)
                maxPictureSize = size;
        }

        /**
         * Definir a dimensao da imagem
         * */
        parameters.setPictureSize(maxPictureSize.width, maxPictureSize.height);
    }

    private void controlMinSizeOfPreview(Camera.Parameters parameters) {
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        Camera.Size maxPreviewSize = previewSizes.get(0);
        for(Camera.Size size : previewSizes) {
            if (maxPreviewSize.width > size.width && maxPreviewSize.height > size.height)
                maxPreviewSize = size;
        }
        /**
         *
         * */
        parameters.setPreviewSize(maxPreviewSize.width, maxPreviewSize.height);
    }

    private void controlCameraFrameCaptureRate(Camera.Parameters parameters) {
        /**
         * Definindo a taxa de Frame que a camera captura
         * */
        List<Integer> supportedPreviewFrameRates = parameters.getSupportedPreviewFrameRates();
        int max = supportedPreviewFrameRates.get(0);
        for(Integer i : supportedPreviewFrameRates) {
            if (max < i)
                max = i;
        }
        parameters.setPreviewFrameRate(max);
    }

    /**
     * {@link CameraControl}
     * */
    @Override
    public void turnOnCamera() {
        if (camera != null && !cameraOpen) {
            try {
                if (actionOnChangeCameraOrientation == null)
                    actionOnChangeCameraOrientation = new ActionOnChangeCameraOrientation(context, camera);
                startCameraPreview();
                cameraOpen = true;
                WindowManager windowManager=  (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                if (windowManager != null) {
                    Display display = windowManager.getDefaultDisplay();
                    Point dimension = new Point();
                    display.getSize(dimension);;
                    int rotation = display.getRotation();
                    Log.i("CAMERA_PREVIEW_ROTATION", String.format(Locale.getDefault()
                            ,"Rotation %d.\n D(%d, %d)"
                                , rotation == 0 ? 0 : rotation == 90 ? 90 : rotation == 270 ? 270 : 0
                                , dimension.x
                                , dimension.y
                            )
                    );
                    //
                    controlOrientationByRotation(rotation);
                    camera.stopPreview();
                    Camera.Parameters parameters = camera.getParameters();
                    parameters.setJpegQuality(100);

                    /**
                     * A documentacao em ingles deixa mais claro do que se eu reescreve-se em
                     * portuga.
                     *
                     * Call this when something has changed which has invalidated the
                     * layout of this view. This will schedule a layout pass of the view
                     * tree. This should not be called while the view hierarchy is currently in a layout
                     * pass ({@link #isInLayout()}. If layout is happening, the request may be honored at the
                     * end of the current layout pass (and then layout will run again) or after the current
                     * frame is drawn and the next layout occurs.
                     *
                     * <p>Subclasses which override this method should call the superclass method to
                     * handle possible request-during-layout errors correctly.</p>
                     */
                    requestLayout();
                    camera.setParameters(parameters);
                    camera.startPreview();
                }
                //camera.setParameters(parameters);
                Log.i("CAMERA_API", "Camera Ligada");
            }
            catch (IOException e) {
                Log.e("EXCP_SURFACE_CREATED", HelperLogException.getMessage(e));
            }
        }
    }

    /**
     * {@link CameraControl}
     * */
    @Override
    public void turnOffCamera() {
        if (camera != null && cameraOpen) {
            camera.unlock();
            camera.stopPreview();
            cameraOpen = false;
            camera.release();
        }
        camera = null;
    }

    /**
     * {@link CameraControl}
     * */
    @Override
    public void restartCamera() {
        camera.startPreview();
    }

    private void startCameraPreview() throws IOException {
        /**
         * Trava a camera para que ela seja utilizada por um unicp processo
         * */
        camera.lock();
        camera.setPreviewDisplay(surfaceHolder);
        camera.startPreview();
        cameraOpen = true;
        Log.i("START_CAMERA_PREVIEW", "START");
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    private void stopCameraPreview() {
        String msg = "NOT STOPPED";
        if (camera != null && cameraOpen) {
            camera.unlock();
            try {
                camera.setPreviewDisplay(null);
            }
            catch (IOException e) {
               Log.e("EXCP_STOP_CAMERA", HelperLogException.getMessage(e));
            }
            camera.stopPreview();
            cameraOpen = false;
           msg = "STOPPED";
        }
        Log.i("STOP_CAMERA_PREVIEW", msg);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        turnOffCamera();
    }

    public void safeTakePicture(@Nullable Camera.ShutterCallback shutterCallback
            , @Nullable Camera.PictureCallback raw, @NonNull Camera.PictureCallback pictureCallback) {
        if (camera != null && cameraOpen) {
            camera.takePicture(shutterCallback, raw, pictureCallback);
            cameraOpen = false;
        }
    }

    public void reverseCamera() {}

    private void activityFocus() {
        Camera.Parameters parameters = camera.getParameters();
        List<String> focusMode = parameters.getSupportedFocusModes();
        if (focusMode != null && focusMode.size() > 0) {
            if (focusMode.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }
            if(focusMode.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
        }
    }

    private void printParameters() {
        Camera.Parameters parameters = camera.getParameters();
        Log.i("PARAMETERS_CAMERA", parameters.getColorEffect());
        Log.i("PARAMETERS_CAMERA", parameters.getAntibanding());
        Log.i("PARAMETERS_CAMERA", parameters.getFlashMode());
        Log.i("PARAMETERS_CAMERA", parameters.getSceneMode());
        Log.i("PARAMETERS_CAMERA", parameters.getWhiteBalance());
        List<String> focusMode = parameters.getSupportedFocusModes();
        for (String mode : focusMode) {
            Log.i("PARAMETERS_CAMERA", String.format("Mode Focus: %s", mode));
        }
    }
}
