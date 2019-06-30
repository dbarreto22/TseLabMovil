package com.example.fakenews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleLoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, GoogleApiClient.ConnectionCallbacks {

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SignInActivity >>>> ";
    GoogleApiClient mGoogleApiClient;
    SignInButton signInButton;
    Button signOutButton;
    Button toMain;
    TextView statusTextView;
    GoogleSignInClient mGoogleSignInClient;
    String url = "https://r179-27-99-70.ir-static.anteldata.net.uy:8443/FakeNews-web/RESTServices/";
    private ApiInterface restApi;
    private String token_fire;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login);
        restApi = ApiClient.getClient(url).create(ApiInterface.class);

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

        signInButton = findViewById(R.id.sign_in_button);
        signOutButton = findViewById(R.id.sign_out_button);
        statusTextView = findViewById(R.id.textView);
        toMain = findViewById(R.id.button_to_main);
        toMain.setOnClickListener(this);
        signInButton.setOnClickListener(this);
        signOutButton.setOnClickListener(this);


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
                        Toast.makeText(GoogleLoginActivity.this, token_fire, Toast.LENGTH_SHORT).show();
                    }
                });


    } // onCreate

    @Override
    protected void onStart(){
        super.onStart();
        //  Toast.makeText(getApplicationContext(),"Now onStart() calls", Toast.LENGTH_LONG).show(); //onStart Called
        if (!SessionPrefs.get(this).isLoggedIn()) {
            signInButton.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.INVISIBLE);
        } else {
            signInButton.setVisibility(View.INVISIBLE);
            signOutButton.setVisibility(View.VISIBLE);
            //startActivity(new Intent(this, MainActivity.class));
            //finish();
            //return;
        }


    }



    public void onClick(View v){
        switch (v.getId()){
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.button_to_main:
                goToMain();
                break;
        }
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
        signOutButton.setVisibility(View.INVISIBLE);
        new SessionPrefs(GoogleLoginActivity.this).logOut();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        //if (requestCode == RC_SIGN_IN){
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
            sendIdTokenMail(idToken, mail);
            //
            // updateUI(account);
            Intent intent = new Intent(this, com.example.fakenews.MainActivity.class);
            startActivity(intent);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            // updateUI(null);
        }
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
                            SessionPrefs.get(GoogleLoginActivity.this).guardarToken(response.body().getJwt(), mail);
                            Log.w(TAG, "response = " + response.body());
                            // Ir al menu principal (main activity)

                        }
                        else {
                            String error = response.body().toString();
                            // showLoginError(error);
                            Log.w(TAG, "response error del ojete: " + error);
                        }

                    }else{

                        String error = "Error: Usuario o contraseña incorrecta";
                        // showLoginError(error);
                        Log.w(TAG, "response error 2" + error);
                    }
                }
                // Procesar errores
                else {

                    String error = response.body().toString();
                    /*Log.w(TAG, "response: " + error + response.headers());
                    Log.w(TAG, "más error" + response.toString());
                    Log.w(TAG, "response error: " + error + response.code());//+ " " +
                    Log.w(TAG, "error" + response.errorBody().toString() + response.message() + response.raw());*/
                            //showLoginError(error);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.w(TAG, "response error on failure: " + t + t.getCause());

            }

            /*@Override
            public void onFailure(Call<String> call, Throwable t) {
                /*showProgress(false);
                showLoginError(t.getMessage());
                Log.w(TAG, "response error on failure: " + t + t.getCause());
            }*/
        });

    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult){
        Log.d(TAG, "onConnectionFailed: " + connectionResult);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    public void goToMain(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


}
