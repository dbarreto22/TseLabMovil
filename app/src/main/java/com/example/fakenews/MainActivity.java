package com.example.fakenews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.fakenews.R.id;
import com.example.fakenews.apiWeb.ApiInterface;
import com.example.fakenews.prefs.SessionPrefs;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import static com.example.fakenews.R.string.navigation_drawer_close;
import static com.example.fakenews.R.string.navigation_drawer_open;

public class MainActivity extends AppCompatActivity
        implements OnNavigationItemSelectedListener {

    private static final String TAG = "MainInActivity >>>> ";
    GoogleApiClient mGoogleApiClient;
    GoogleSignInClient mGoogleSignInClient;
    private ApiInterface restApi;
    private String displayName;
    TextView mensajeBienvenida;

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Verifico sesión iniciada
        if (!SessionPrefs.get(this).isLoggedIn()) {
            startActivity(new Intent(this, GoogleLoginActivity.class));
            finish();
            return;
        }
        //setContentView(R.layout.activity_login);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if((account != null) || !account.isExpired()){
            displayName = account.getDisplayName();
/*            mensajeBienvenida = findViewById(R.id.textBienvenida);
            mensajeBienvenida.setText("Bienvenido " + displayName);
*/        }

        //traer hechos y listar
        Toolbar toolbar = findViewById(id.toolbar);
        //setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,navigation_drawer_open,navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences preferencias = getApplicationContext().getSharedPreferences(SessionPrefs.PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_drawer,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        // pruebo id de menú opcion_uno
        if (id == R.id.listarHechos) {
            goToListarHechos();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.listarHechos) {
            goToListarHechos();
        } else if (id == R.id.opcion_dos) {
            goToSolicitarVerif();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void goToListarHechos(){
        Intent i = new Intent(this, DisplayHechosActivity.class);
        Log.w(TAG, "CALL DISPLAY HECHOS" + i.toString());
        startActivity(i);
    }

    public void goToSolicitarVerif(){
        Intent i = new Intent(this, SolicitudVerificacionActivity.class);
        startActivity(i);
    }
}
