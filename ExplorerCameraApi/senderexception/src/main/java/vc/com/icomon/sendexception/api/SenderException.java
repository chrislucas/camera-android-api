package vc.com.icomon.sendexception.api;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vc.com.icomon.sendexception.api.helpers.text.HelperText;
import vc.com.icomon.sendexception.api.retrofit.endpoint.EndpointSendException;
import vc.com.icomon.sendexception.api.retrofit.factory.ConverterFactoryObjectException;
import vc.icomon.com.sretrofit.CallbackRequestAPI2;
import vc.icomon.com.sretrofit.MillisecondTimeOutRequest;
import vc.icomon.com.sretrofit.RetrofitServiceGenerator;

public class SenderException {


    public static void buildMessageAndSendIt(Context ctx, String url, String re, String idApplication
            , String json, String method, String messageException) {

        ObjectException o = new ObjectException(json, re, idApplication, method, messageException);
        DefaultImplCallbackRequestAPI2 callbackException = new DefaultImplCallbackRequestAPI2(ctx);
        send(callbackException, ctx, url, o);
    }

    private static Call<DefaultMessageExceptionRequest> send(final CallbackRequestAPI2<DefaultMessageExceptionRequest> callbackRequestAPI
            , final Context ctx, String baseURL, final ObjectException object) {

        MillisecondTimeOutRequest millisecondTimeOutRequest = new MillisecondTimeOutRequest(80000
                ,60000,6000);

        EndpointSendException endpointSendException = RetrofitServiceGenerator.getService(EndpointSendException.class
                , new ConverterFactoryObjectException(), millisecondTimeOutRequest, baseURL);

        Call<DefaultMessageExceptionRequest> call = endpointSendException.sendObjectException(object);

        call.enqueue(new Callback<DefaultMessageExceptionRequest>() {
            @Override
            public void onResponse(@NonNull Call<DefaultMessageExceptionRequest> call
                    , @NonNull Response<DefaultMessageExceptionRequest> response) {
                int code = response.code();
                DefaultMessageExceptionRequest defaultMessageExceptionRequest = response.body();
                if (defaultMessageExceptionRequest != null) {
                    defaultMessageExceptionRequest.setCode(code);
                    Exception e = new Exception(String.format(Locale.getDefault()
                            , "Código de resposta%d", code));
                    writeLogFile(ctx, object, e);
                }
                callbackRequestAPI.onSuccessRequest(defaultMessageExceptionRequest);
            }

            @Override
            public void onFailure(@NonNull Call<DefaultMessageExceptionRequest> call
                    , @NonNull Throwable t) {
                DefaultMessageExceptionRequest message;
                String s = writeLogFile(ctx, object, t);
                if (t instanceof IOException) {
                    message = new DefaultMessageExceptionRequest("Problemas com a conexão com a internet.\n" + s);
                }
                else if (t instanceof JSONException) {
                    message = new DefaultMessageExceptionRequest("Problemas em realizar a conversão dos dados do auto.\n" + s);
                }
                else {
                    message = new DefaultMessageExceptionRequest("Problemas em enviar o auto de constatação.\n" + s);
                }
                callbackRequestAPI.onFailureRequest(message);
                /**
                 * TODO Escrever num arquivo CSV
                 **/
                writeLogFile(ctx, object, t);
            }
        });
        return call;
    }

    public static String writeLogFile(Context ctx, ObjectException object, Throwable t) {
        // Vamos criar uma pasta com o nome do APP...
        String appName = getAppName(ctx);
        String name = HelperText.removeAccents(appName).toLowerCase().replace(" ", "_");

        // Na pasta DOCUMENTS do aparelho do usuario
        File dirDocuments = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        // Criar um objeto file
        File file = new File(dirDocuments, "logs_icomon".concat(File.separator).concat(name));
        // Verificar se a pasta existe
        if (!file.exists()) {
            // senao crie-a
            boolean flag = file.mkdirs();
            Log.i("CREATE_LOG_FILE", String.valueOf(flag));
        }
        String line = object.getLog() + "," + t.getMessage() + "," + appName + "\n";
        try {
            String path = file.getPath()
                    .concat(File.separator)
                    .concat(name.concat("_log"))
                    .concat(".csv");
            Log.i("WRITE_LOG_FILE", path);
            File f = new File(path);
            FileWriter fw = new FileWriter(f, true);
            fw.write(line);
            fw.close();
        }
        catch (IOException e) {
            Log.e("EXCP_WRITE_LOG_FILE", e.getMessage());
        }
        return line;
    }


    private static String getAppName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        return applicationInfo.labelRes == 0
                ? applicationInfo.nonLocalizedLabel.toString() : context.getString(applicationInfo.labelRes);
    }
}
