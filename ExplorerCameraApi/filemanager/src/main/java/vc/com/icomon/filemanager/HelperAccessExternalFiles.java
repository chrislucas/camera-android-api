package vc.com.icomon.filemanager;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.IntDef;
import android.support.annotation.StringDef;
import android.util.Log;



import java.io.File;
import java.lang.annotation.Retention;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class HelperAccessExternalFiles {

    @IntDef({TypeOutputMediaFile.VIDEO , TypeOutputMediaFile.IMAGE})
    @Retention(SOURCE)
    public @interface TypeOutputMediaFile {
        int VIDEO = 1;
        int IMAGE = 2;
    }

    @Retention(SOURCE)
    @StringDef({TypeOfEnvironmentDir.DIR_PICTURES, TypeOfEnvironmentDir.DIR_MOVIES})
    public @interface TypeOfEnvironmentDir {
        String DIR_PICTURES = "Pictures";
        String DIR_MOVIES = "Movies";
    }

    public static Uri getExternalOutputMediaFileUri(@TypeOutputMediaFile int typeOutput, String folder) {
        if (typeOutput == TypeOutputMediaFile.IMAGE) {
           return Uri.fromFile(getExternalOutputMediaFile(typeOutput, TypeOfEnvironmentDir.DIR_PICTURES, folder));
        }

        else if (typeOutput == TypeOutputMediaFile.VIDEO)  {
           return Uri.fromFile(getExternalOutputMediaFile(typeOutput, TypeOfEnvironmentDir.DIR_MOVIES, folder));
        }
        return null;
    }

    /**
     * Forma de gerar o nome da pasta, tal que cada app tenha a sua pasta
     * String.format("%s/%s/%s", anyName. appName);
     * */

    private static File getExternalOutputMediaFile(@TypeOutputMediaFile int type, @TypeOfEnvironmentDir String typeOfEnv, String folder) {
        File externalMediaFileDir = new File(Environment.getExternalStoragePublicDirectory(typeOfEnv), folder);
        if (!externalMediaFileDir.exists()) {
            if (!externalMediaFileDir.mkdirs()) {
                Log.e("EXTERNAL_OUTPUT", "Erro ao criar o diretório");
                return null;
            }
        }
        File f = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.getDefault());
        long time = Calendar.getInstance().getTimeInMillis();
        Date date = new Date(time);
        String filename = simpleDateFormat.format(date);
        String path = externalMediaFileDir.getPath().concat(File.separator);
        switch (type) {
            case TypeOutputMediaFile.IMAGE:
                path = path.concat("IMG_".concat(filename).concat(".jpg"));
                break;
            case TypeOutputMediaFile.VIDEO:
                path = path.concat("VIDEO_".concat(filename).concat(".mp4"));
                break;
            default:
                return null;
        }
        return new File(path);
    }
}
