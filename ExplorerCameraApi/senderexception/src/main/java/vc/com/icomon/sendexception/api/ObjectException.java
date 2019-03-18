package vc.com.icomon.sendexception.api;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ObjectException {

    public ObjectException(String jsonData, String userRegister, String idApplication, String methodName, String messageException) {
        this.jsonData = jsonData;
        this.userRegister = userRegister;
        this.idApplication = idApplication;
        this.methodName = methodName;
        this.messageException = messageException;
    }

    /**
     * Informacao que estava sendo enviada para o servico antes
     * de ocorrer o erro
     * */
    private String jsonData;

    /**
     * Registro do usuario que estava tentando fazer um request
     * */
    private String userRegister;

    /**
     * ID da aplicacao
     * */
    private String idApplication;

    /**
     * Nome do metodo que foi executado
     * */
    private String methodName;

    /**
     * Mensagem da excecao lancada
     * */
    private String messageException;

    public String getJsonData() {
        return jsonData;
    }

    public String getUserRegister() {
        return userRegister;
    }

    public String getIdApplication() {
        return idApplication;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getMessageException() {
        return messageException;
    }

    private String getFormattedCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("dd/MM/yyyy kk:mm:ss"
                        , Locale.getDefault());
        return simpleDateFormat.format(new Date(calendar.getTimeInMillis()));
    }

    public String getLog() {
         return idApplication +
                "," +
                getFormattedCurrentDate() +
                "," +
                userRegister +
                "," +
                messageException +
                "," +
                messageException;
    }
}
