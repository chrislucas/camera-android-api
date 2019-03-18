package vc.com.icomon.sendexception.api;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;

import java.util.List;
import java.util.Locale;

import vc.icomon.com.sretrofit.CallbackRequestAPI2;

public class DefaultImplCallbackRequestAPI2 implements CallbackRequestAPI2<DefaultMessageExceptionRequest> {

    private Context ctx;

    public DefaultImplCallbackRequestAPI2(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void onSuccessRequest(DefaultMessageExceptionRequest data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(String.format(Locale.getDefault()
                , "Relatório de erros. Código %d", data.getCode()));
        builder.setMessage(data.getMessage());
        if (ctx instanceof FragmentActivity) {
            if ( ( (FragmentActivity) ctx).isFinishing() ) {
                builder.create().show();
            }
        }
    }

    @Override
    public void onSuccessRequest(List<DefaultMessageExceptionRequest> data) {}

    @Override
    public void onFailureRequest(DefaultMessageExceptionRequest response) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Relatório de erros");
        builder.setMessage(response.getMessage());
        if (ctx instanceof FragmentActivity) {
            if ( ( (FragmentActivity) ctx).isFinishing() ) {
                builder.create().show();
            }
        }
    }
}
