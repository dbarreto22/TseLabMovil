package com.example.fakenews;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fakenews.apiModel.Hecho;
import com.example.fakenews.apiWeb.ApiClient;
import com.example.fakenews.apiWeb.ApiInterface;
import com.example.fakenews.prefs.SessionPrefs;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayHechosActivity extends AppCompatActivity {

    private ListView listViewHechos;
    private ProgressBar mProgressView;
    private TextView descripcion;
    private String url;
    private ApiInterface apiService;

    private String authorization;
    private String contentType;
    private String usuario;

    private String TAG = "Display Hechos Activity >>>>> ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_hechos);

        getSupportActionBar().setHomeButtonEnabled(true);

        listViewHechos = findViewById(R.id.listViewHechos);
        mProgressView = findViewById(R.id.progressBar);
        descripcion = findViewById(R.id.descripcion);

        try {
            url = ApiClient.getProperty("urlServidor",getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
            url = "https://r179-27-99-70.ir-static.anteldata.net.uy:8443/FakeNews-web/RESTServices/getHechos/";
        }

        showProgress(true);

        apiService = ApiClient.getClient(url).create(ApiInterface.class);

        authorization = "Bearer " + getApplicationContext().getSharedPreferences(SessionPrefs.PREFS_NAME, MODE_PRIVATE).getString(SessionPrefs.PREF_USER_TOKEN, null);
        usuario = getApplicationContext().getSharedPreferences(SessionPrefs.PREFS_NAME, MODE_PRIVATE).getString(SessionPrefs.PREFS_NAME, null);
        contentType = "application/json";

        //Realizar peticion al servidor y llenar el ListView de hechos
        Call<List<Hecho>> callHechos = apiService.getAllHechos();
        callHechos.enqueue(new Callback<List<Hecho>>() {

            @Override
            public void onResponse(Call<List<Hecho>> call, Response<List<Hecho>> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<Hecho> hechos = response.body();

                        ArrayAdapter<Hecho> adapter = new MiAdaptador(DisplayHechosActivity.this, R.id.listViewHechos, hechos);

                        adapter.setDropDownViewResource(R.layout.hecho_row); // hecho_row

                        listViewHechos.setAdapter(adapter);

                        showProgress(false);
                    } else {
                        Toast.makeText(DisplayHechosActivity.this, "Error: respuesta del servidor vacia: Intente más tarde", Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {

                    Toast.makeText(DisplayHechosActivity.this, "Error: no se ha podido recibir respuesta del servidor.", Toast.LENGTH_SHORT).show();
                    Log.i("Body error", response.errorBody().toString());

                    return;
                }
            }

            @Override
            public void onFailure(Call<List<Hecho>> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "Ha ocurrido un error mientras se realizaba la peticion", Toast.LENGTH_LONG).show();
                t.printStackTrace();
                Log.w(TAG, t.toString());
                Log.w(TAG,t.getMessage()+t.initCause(t).toString());
                return;
            }
        });
    }  // onCreate

    private void showProgress(boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);

        int visibility = show ? View.GONE : View.VISIBLE;
        listViewHechos.setVisibility(visibility);
        descripcion.setVisibility(visibility);
    }


    private class MiAdaptador extends ArrayAdapter<Hecho> {

        List<Hecho> lista = new ArrayList<>();

        public MiAdaptador(Context context, int resource, List<Hecho> objects) {
            super(context, resource, objects);
            lista = objects;
        }

        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();

            View row = inflater.inflate(R.layout.hecho_row, parent, false); // add hecho_row instead of activity_display_jehchos
// setear los atributos del hecho en textviews
            TextView titulo = row.findViewById(R.id.titulo);
            /*if (lista.get(position).getTitulo().isEmpty()){
                titulo.setText("Título del hecho vacío");
            } else {*/
                titulo.setText("Título del hecho: " + lista.get(position).getTitulo());
            //}

            TextView url = row.findViewById(R.id.url);
            /*if (lista.get(position).getUrl().isEmpty()){
                url.setText("URL del hecho vacía");
            } else {*/
                url.setText("Url: " + lista.get(position).getUrl());
            //}

            TextView calificacion = row.findViewById(R.id.calificacion);
            /*if (lista.get(position).getCalificacion().tipoCalificacionStr().isEmpty()){
                calificacion.setText("Calificación del hecho vacía");
            } else {*/
                calificacion.setText("Calificación: " + lista.get(position).getCalificacion());
            //}

            TextView fechaVerificacion = row.findViewById(R.id.fechaVerificacion);
            if (lista.get(position).getFechaFinVerificacion() == null ){
                fechaVerificacion.setText("Hecho sin fecha de verificación");
            } else {
                fechaVerificacion.setText("Fecha de calificación: " + new SimpleDateFormat("dd/MM/yyyy").format(lista.get(position).getFechaFinVerificacion()));
            }

            TextView estado = row.findViewById(R.id.estado);
            /*if (lista.get(position).getEstado().estadoStr().isEmpty()){
                estado.setText("Estado del hecho vacío");
            } else {*/
            estado.setText("Estado: " + lista.get(position).getEstado());
            //}
            return row;
        }
    }



}
