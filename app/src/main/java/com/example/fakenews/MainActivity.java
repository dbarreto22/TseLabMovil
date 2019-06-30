package com.example.fakenews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fakenews.R.id;
import com.example.fakenews.apiModel.LoginBody;
import com.example.fakenews.apiModel.LoginResponse;
import com.example.fakenews.apiWeb.ApiClient;
import com.example.fakenews.apiWeb.ApiInterface;
import com.example.fakenews.prefs.SessionPrefs;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.fakenews.R.string.navigation_drawer_close;
import static com.example.fakenews.R.string.navigation_drawer_open;

public class MainActivity extends AppCompatActivity
        implements OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = "MainInActivity >>>> ";
    private static final int RC_SIGN_IN = 9001;

    GoogleApiClient mGoogleApiClient;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount account;
    private String token_fire;

    String url = "https://r179-27-99-70.ir-static.anteldata.net.uy:8443/FakeNews-web/RESTServices/";
    private ApiInterface restApi;

    TextView nombreGoogle;
    Button logout;
    SignInButton signInButton;

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        restApi = ApiClient.getClient(url).create(ApiInterface.class);
        logout = findViewById(id.logout);
        signInButton = findViewById(R.id.sign_in_button);
        nombreGoogle = findViewById(R.id.textViewNombre);
        signInButton.setOnClickListener(this);
        logout.setOnClickListener(this);


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();


        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        // Verifico sesión iniciada
        if (SessionPrefs.get(this).isLoggedIn()) {
            signInButton.setVisibility(View.INVISIBLE);
            logout.setVisibility(View.VISIBLE);
            nombreGoogle.setVisibility(View.VISIBLE);
            /*startActivity(new Intent(this, GoogleLoginActivity.class));
            finish();
            return;*/
        } else {
            signInButton.setVisibility(View.VISIBLE);
            logout.setVisibility(View.INVISIBLE);
            nombreGoogle.setVisibility(View.INVISIBLE);
        }

        Toolbar toolbar = findViewById(id.toolbar);

        DrawerLayout drawer = findViewById(id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,navigation_drawer_open,navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //SharedPreferences preferencias = getApplicationContext().getSharedPreferences(SessionPrefs.PREFS_NAME, Context.MODE_PRIVATE);






        // Obtengo token firebase
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token_fire = task.getResult().getToken();

                        // Log and toast
                        Log.d(TAG, token_fire);
                    }
                });


    } //OnCreate

    @Override
    public void onClick(View v){

        switch (v.getId()){
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.logout:
                signOut();
                break;
        }
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_main_drawer,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.listarHechos) {
            goToListarHechos();
        } else if (id == R.id.opcion_dos) {
            goToSolicitarVerif();
        } else if (id == R.id.nav_inicio) {
            goToMain();
        } else if (id == R.id.nav_logout){
            signOut();
        } else if (id == R.id.nav_suscripcion){
            goToSuscripcion();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.listarHechos) {
            goToListarHechos();
        } else if (id == R.id.opcion_dos) {
            goToSolicitarVerif();
        } else if (id == R.id.nav_inicio) {
            goToMain();
        } else if (id == R.id.nav_logout){
            signOut();
        } else if (id == R.id.nav_suscripcion){
            goToSuscripcion();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    public void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
        signInButton.setVisibility(View.VISIBLE);
        logout.setVisibility(View.INVISIBLE);
        nombreGoogle.setText("");
        SessionPrefs.get(this).logOut();
        Log.i(TAG,"Logged out");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.w(TAG, "Google signInResult: ok=" + account.getDisplayName());
            String idToken = account.getIdToken();
            String mail = account.getEmail();
            Toast.makeText(MainActivity.this, "Sesión iniciada", Toast.LENGTH_SHORT).show();
            sendIdTokenMail(idToken, mail);
            updateUIOk(account);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void updateUIOk(GoogleSignInAccount account) {
        nombreGoogle.setVisibility(View.VISIBLE);
        nombreGoogle.setText(account.getDisplayName());
        signInButton.setVisibility(View.INVISIBLE);
        logout.setVisibility(View.VISIBLE);
    }

    private void sendIdTokenMail(String idtoken, final String mail){

        Call<LoginResponse> loginCall = restApi.login(new LoginBody(mail, idtoken, token_fire));
        loginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse
                    (Call<LoginResponse> call, Response<LoginResponse> response) {

                // Ocultar la barra de progreso
                // showProgress(false);

                if (response.isSuccessful()){
                    Log.w(TAG, "login response successful");
                    if (response.body() != null){
                        if (!(response.body().toString().contains("Error"))){

                            // Guardar token de sesion y userID en preferencias
                            SessionPrefs.get(MainActivity.this).guardarToken(response.body().getJwt(), mail);
                            Log.w(TAG, "response = " + response.body());

                        }
                        else {
                            String error = response.body().toString();
                            Log.w(TAG, "response error: " + error);
                        }

                    }else{

                        String error = "Error: Sin respuesta del servidor";
                        // showLoginError(error);
                        Log.w(TAG, "response error 2" + error);
                    }
                }
                // Procesar errores
                else {

                    String error = response.body().toString();
                    Log.w(TAG, "Error en el pedido al servidor: " + response.toString());

                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.w(TAG, "Falla causada por: " + t.getCause());

            }

        });

    }


    public void goToListarHechos(){
        Intent i = new Intent(this, DisplayHechosActivity.class);
        //Log.w(TAG, "CALL DISPLAY HECHOS" + i.toString());
        startActivity(i);
    }

    public void goToSolicitarVerif(){
        Intent i = new Intent(this, SolicitudVerificacionActivity.class);
        startActivity(i);
    }

    public void goToSuscripcion(){
        Intent i = new Intent(this, SuscripcionActivity.class);
        startActivity(i);
    }

    public void goToMain(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult){
        Log.d(TAG, "onConnectionFailed: " + connectionResult);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

}
