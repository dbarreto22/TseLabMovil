package com.example.fakenews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.fakenews.apiModel.DTRespuesta;
import com.example.fakenews.apiModel.MailBody;
import com.example.fakenews.apiWeb.ApiClient;
import com.example.fakenews.apiWeb.ApiInterface;
import com.example.fakenews.prefs.SessionPrefs;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuscripcionActivity extends AppCompatActivity implements View.OnClickListener {

    Button suscripcion;

    private String url;
    private ApiInterface apiService;

    private String authorization;
    private String contentType;
    private String usuario;


    private String TAG = "Suscripción Activity >>>> ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_susctripcion);

        getSupportActionBar().setHomeButtonEnabled(true);


        suscripcion = findViewById(R.id.suscripcion);
        suscripcion.setOnClickListener(this);





    }

    @Override
    public void onClick(View v) {
        suscribir();
    }

    private void suscribir() {

        try {
            url = ApiClient.getProperty("urlServidor",getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
            url = "https://r179-27-99-70.ir-static.anteldata.net.uy:8443/FakeNews-web/RESTServices/suscripcion/";
        }
        apiService = ApiClient.getClient(url).create(ApiInterface.class);

        authorization = "Bearer " + getApplicationContext().getSharedPreferences(SessionPrefs.PREFS_NAME, MODE_PRIVATE).getString(SessionPrefs.PREF_USER_TOKEN, null);
        usuario = getApplicationContext().getSharedPreferences(SessionPrefs.PREF_USER_EMAIL, MODE_PRIVATE).getString(SessionPrefs.PREFS_NAME, null);
        contentType = "application/json";

        //Realizar peticion de suscripción al servidor
        Call<DTRespuesta> suscr = apiService.suscripcion(authorization,contentType,new MailBody(usuario));
        suscr.enqueue(new Callback<DTRespuesta>() {

            @Override
            public void onResponse(Call<DTRespuesta> call, Response<DTRespuesta> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (!(response.body().getResultado() == "OK")){
                            Log.w(TAG, "response suscripción ok: " + response.toString());
                            Toast.makeText(SuscripcionActivity.this, "Suscripción ingresada", Toast.LENGTH_LONG).show();
                            goHome();
                        } else {
                            Log.w(TAG, "response resultado error: " + response.toString());
                            Toast.makeText(SuscripcionActivity.this,"No se pudo ingresar la noticia", Toast.LENGTH_LONG).show();
                            goHome();
                        }
                    } else {
                        Toast.makeText(SuscripcionActivity.this, "Por favor, intente más tarde", Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {

                    Toast.makeText(SuscripcionActivity.this, "Error: no se ha podido recibir respuesta del servidor.", Toast.LENGTH_SHORT).show();
                    Log.i("Body error", response.errorBody().toString());

                    return;
                }
            }

            @Override
            public void onFailure(Call<DTRespuesta> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "Ha ocurrido un error mientras se realizaba la peticion", Toast.LENGTH_LONG).show();
                t.printStackTrace();
                Log.w(TAG, t.toString());
                Log.w(TAG,t.getMessage()+t.initCause(t).toString());
                return;
            }
        });
    }

    private void goHome() {
        Intent intent = new Intent(this, com.example.fakenews.MainActivity.class);
        startActivity(intent);
    }
}
