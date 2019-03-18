package vc.com.icomon.camlibapi.actions.camera.impl;


import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import vc.com.icomon.camlibapi.utils.exception.HelperLogException;
import vc.com.icomon.filemanager.FileManager;
import vc.com.icomon.filemanager.HelperAccessExternalFiles;


public class SaveImage {

    public SaveImage() { }

    public static Uri saveImageOnExternalDir(byte [] data, String folder) {
        Log.i("ON_PICTURE_TAKEN", String.format("SIZE_OF %d MB", data.length / (1024 * 1024)));
        Uri uri = HelperAccessExternalFiles.getExternalOutputMediaFileUri(HelperAccessExternalFiles.TypeOutputMediaFile.IMAGE, folder);
        if (uri != null) {
            try {
                String path = uri.getPath();
                Log.i("ON_PICTURE_TAKEN", String.format("Caminho: %s", path));
                File file = new File(path);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(data);
                fileOutputStream.close();
                fileOutputStream = null;
            }
            catch (Exception e) {
                Log.e("EXCP_ON_PICTURE_TAKEN", HelperLogException.getMessage(e));
            }
        }
        return uri;
    }

    public static Uri saveImageOnInternalDir(Context context, byte [] data, String path) {
        return FileManager.saveFileOnInternalDir(context, data, path);
    }
}
