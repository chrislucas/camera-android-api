package vc.icomon.com.sretrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by r028367 on 16/11/2017.
 */

public class RetrofitServiceGenerator {

    /**
     * A classe {@link GsonConverterFactory} é a Factory responsavel por processar o response
     * */
    public static <Clazz> Clazz getService(Class<Clazz> clazz, String baseUrl, MillisecondTimeOutRequest millisecondTimeOutRequest) {
        return getRetrofitInstance(baseUrl, millisecondTimeOutRequest).create(clazz);
    }

    /**
     * Esse metodo retornar uma instancia do Retrofit para realizar request HTTP. Essa instancia
     * foi configurada para fazer requests e receber como response um JSON que sera processado
     * pela classe {@link GsonConverterFactory}
     * */
    public static <Clazz> Clazz getService(Class<Clazz> clazz, String baseUrl) {
        return getRetrofitInstance(baseUrl).create(clazz);
    }
    /**
     *
     * Outro metodo que nos retorna uma instancia do Retrofit e espera que a resposa seja um JSON.
     * A classe {@link GsonConverterFactory} é a Factory responsavel por processar o response
     * */
    public static <Clazz> Clazz getService(Class<Clazz> clazz, Converter.Factory factory, String baseUrl) {
        return getRetrofitInstance(factory, baseUrl).create(clazz);
    }

    /**
     * Um pouco diferente dos metodos acima, esse metodo recebe como argumento uma instancia de
     * {@link Converter.Factory} que sera responsavel por chamar a classe que ira processar o response.
     * A classe responsavel por capturar o response deve ser preparada para lidar com o resultado esperado
     * seja JSON, XML, arquivo texto etc.
     * */
    public static <Clazz> Clazz getService(Class<Clazz> clazz, Converter.Factory factory
            , MillisecondTimeOutRequest timeOutRequest, String baseUrl) {
        return getRetrofitInstance(factory, timeOutRequest, baseUrl).create(clazz);
    }

    private static Retrofit getRetrofitInstance(String url, MillisecondTimeOutRequest millisecondTimeOutRequest) {
        OkHttpClient.Builder builderHttpClient = new OkHttpClient.Builder();
        builderHttpClient.connectTimeout(millisecondTimeOutRequest.getConnectionTimeoutInMillis(), TimeUnit.MILLISECONDS);
        builderHttpClient.readTimeout(millisecondTimeOutRequest.getReadTimeoutInMillis(), TimeUnit.MILLISECONDS);
        builderHttpClient.writeTimeout(millisecondTimeOutRequest.getWriteTimeoutInMillis(), TimeUnit.MILLISECONDS);
        OkHttpClient httpClient = builderHttpClient.build();
        Retrofit.Builder builderRetrofit = new Retrofit
                .Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create());
        return builderRetrofit.client(httpClient).build();
    }

    private static Retrofit getRetrofitInstance(String url) {
        OkHttpClient.Builder builderHttpClient = new OkHttpClient.Builder();
        builderHttpClient.connectTimeout(1, TimeUnit.MINUTES);
        builderHttpClient.readTimeout(30, TimeUnit.SECONDS);
        builderHttpClient.writeTimeout(30, TimeUnit.SECONDS);
        OkHttpClient httpClient = builderHttpClient.build();
        Retrofit.Builder builderRetrofit = new Retrofit
                .Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create());
        return builderRetrofit.client(httpClient).build();
    }

    private static Retrofit getRetrofitInstance(Converter.Factory factory, String url) {
        OkHttpClient.Builder builderHttpClient = new OkHttpClient.Builder();
        OkHttpClient httpClient = builderHttpClient.build();
        builderHttpClient.connectTimeout(1, TimeUnit.MINUTES);
        builderHttpClient.readTimeout(30, TimeUnit.SECONDS);
        builderHttpClient.writeTimeout(30, TimeUnit.SECONDS);
        Retrofit.Builder builderRetrofit = new Retrofit
                .Builder()
                .baseUrl(url)
                .addConverterFactory(factory);
        return builderRetrofit.client(httpClient).build();
    }


    private static Retrofit getRetrofitInstance(Converter.Factory factory, MillisecondTimeOutRequest timeOutRequest, String url) {
        OkHttpClient.Builder builderHttpClient = new OkHttpClient.Builder();
        OkHttpClient httpClient = builderHttpClient.build();
        builderHttpClient.connectTimeout(timeOutRequest.getConnectionTimeoutInMillis(), TimeUnit.MILLISECONDS);
        builderHttpClient.readTimeout(timeOutRequest.getReadTimeoutInMillis(), TimeUnit.MILLISECONDS);
        builderHttpClient.writeTimeout(timeOutRequest.getWriteTimeoutInMillis(), TimeUnit.MILLISECONDS);
        Retrofit.Builder builderRetrofit = new Retrofit
                .Builder()
                .baseUrl(url)
                .addConverterFactory(factory);
        return builderRetrofit.client(httpClient).build();
    }
}
