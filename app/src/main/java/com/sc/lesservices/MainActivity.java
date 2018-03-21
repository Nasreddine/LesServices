package com.sc.lesservices;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements ServiceCallbacks {

    private MonService monService;
    // créer un object ServiceConnection pour communiquer avec le service
    // onServiceConnected() est lancé quand la connection est établie
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            monService = ((MonService.MyActivityBinder) service).getMonService();
            monService.setCallbacks(MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            monService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        /*
        // lancer le service directement
        Intent intent_service = new Intent(this, MonService.class);
        startService(intent_service);
        */
    }

    // quand l'application et devenu vésible,
    // on lancer le service à travers un Intent

    @Override
    public void onStart() {
        super.onStart();
        Intent intent_service = new Intent(this, MonService.class);
        bindService(intent_service, serviceConnection, Context.BIND_AUTO_CREATE);

    }

    // quand l'activité devient unvisible,
    // on relache le lien avec le service
    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from service
        monService.setCallbacks(null); // unregister
        unbindService(serviceConnection);
    }

    // cette méthode permet de mettre à jour l'interface de l'activité
    // elle sera appelé par le service quand le coordonnées GPS sont récupérées.

    @Override
    public void updateActivityUI() {
        if (null != monService && null != monService.locationManager) {
            ((TextView) findViewById(R.id.latitude)).setText("" + monService.latitude);
            ((TextView) findViewById(R.id.longitude)).setText("" + monService.longitude);
        }
    }



}
