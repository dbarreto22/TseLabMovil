package com.example.fakenews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fakenews.apiModel.AddHechoBody;
import com.example.fakenews.apiModel.DTRespuesta;
import com.example.fakenews.apiWeb.ApiClient;
import com.example.fakenews.apiWeb.ApiInterface;
import com.example.fakenews.prefs.SessionPrefs;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SolicitudVerificacionActivity extends AppCompatActivity {

    private String TAG = "Solicitud Activity >>>>>> ";
    private EditText tituloHecho;
    private EditText urlHecho;
    private Button enviarDatos;

    private ApiInterface apiService;
    private DTRespuesta resp;

    private String url;
    private String authorization;
    private String contentType;

    private String urlRequest;
    private String tituloRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!SessionPrefs.get(this).isLoggedIn()) {
            startActivity(new Intent(this, GoogleLoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_solicitud_verificacion);

        getSupportActionBar().setHomeButtonEnabled(true);

        // Ajustar teclado
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        tituloHecho = findViewById(R.id.tituloHecho);
        urlHecho = findViewById(R.id.urlHecho);
        enviarDatos = findViewById(R.id.enviar);

        enviarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Enviar datos al servidor
                try {
                    url = ApiClient.getProperty("urlServidor",getApplicationContext());
                } catch (IOException e) {
                    e.printStackTrace();
                    url = "http://r179-27-99-70.ir-static.anteldata.net.uy:8080/FakeNews-web/RESTServices/citizen/";
                }

                apiService = ApiClient.getClient(url).create(ApiInterface.class);
                authorization = "Bearer " + getApplicationContext().getSharedPreferences(SessionPrefs.PREFS_NAME, MODE_PRIVATE).getString(SessionPrefs.PREF_USER_TOKEN, null);
                contentType = "application/json";

                urlRequest = urlHecho.getText().toString();
                tituloRequest = tituloHecho.getText().toString();
                Call<DTRespuesta> c = apiService.crearHecho(authorization, contentType, new AddHechoBody(tituloRequest, urlRequest));
                c.enqueue(new Callback<DTRespuesta>() {

                    @Override
                    public void onResponse(Call<DTRespuesta> call, Response<DTRespuesta> response) {

                        if (response.isSuccessful()) {
                                if (!(response.body().getResultado() == "OK")){
                                    Log.w(TAG, "response resultado ok: " + response.toString());
                                    Toast.makeText(SolicitudVerificacionActivity.this, "Noticia ingresada correctamente", Toast.LENGTH_LONG).show();
                                    goMostrarHechos();
                                } else {
                                    Log.w(TAG, "response resultado error: " + response.toString());
                                    Toast.makeText(SolicitudVerificacionActivity.this, "No se pudo ingresar la noticia", Toast.LENGTH_LONG).show();
                                    goHome();
                                }
                        } else {
                            Log.w(TAG, "response error: " + response.errorBody().toString());
                            Log.w(TAG, "RESPONSE: "+ response.toString());
                            Toast.makeText(SolicitudVerificacionActivity.this, "Error: no se ha podido recibir respuesta del servidor.", Toast.LENGTH_SHORT).show();
                            goHome();
                        }
                    }

                    @Override
                    public void onFailure(Call<DTRespuesta> call, Throwable t) {
                        Log.w(TAG, "response onFailure: " + t.getCause());
                        Log.w(TAG, "response onFailure: " + t.toString());
                        Toast.makeText(getApplicationContext(), "Ha ocurrido un error mientras se realizaba la petición", Toast.LENGTH_LONG).show();
                        goHome();
                    }
                });
            }
        });

/*


//*/
    }

    private void goMostrarHechos() {
        Intent intent = new Intent(this, com.example.fakenews.DisplayHechosActivity.class);
        startActivity(intent);
    }

    private void goHome() {
        Intent intent = new Intent(this, com.example.fakenews.MainActivity.class);
        startActivity(intent);
    }
}
