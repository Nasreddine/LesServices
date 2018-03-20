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
    }

    @Override
    public void onStart(){
        super.onStart();
        Intent intent_service = new Intent(this, MonService.class);
        bindService(intent_service, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
            // Unbind from service
            monService.setCallbacks(null); // unregister
            unbindService(serviceConnection);
    }

    @Override
    public void updateActivityUI() {
        if (null != monService && null != monService.locationManager) {
            ((TextView) findViewById(R.id.latitude)).setText("" + monService.latitude);
            ((TextView) findViewById(R.id.longitude)).setText("" + monService.longitude);
        }
    }

}
