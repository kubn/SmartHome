package com.blogger.kubn.android12;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final static String TAG = "myLog";

    TextView textView;
    Button button, button2;
    ListView listView;
    private Context context;
    List<ScanResult> scanResults;
    List<String> stringList;
    ArrayAdapter<String> arrayAdapter;
    WifiManager wifiManager;
    SoundPool soundPool;
    int soundPoolZv = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate(Bundle savedInstanceState)");
        textView = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.listView);
        stringList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(1)
                    .build();
        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }
        soundPoolZv = soundPool.load(this, R.raw.sirena,1);

    }

    @Override
    public void onClick(View v) {
    boolean problem = false;

        try {
            wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        } catch (Exception e) {
            // Handle the exception
        }

        boolean scanStarted = wifiManager.startScan();

        if (scanStarted) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            scanResults = wifiManager.getScanResults();
            if (scanResults != null) {
                stringList.clear();
                for (ScanResult scanResult : scanResults) {
                    String ssid = scanResult.SSID;
                    stringList.add(ssid);
                    Log.d(TAG, " ssid = " + ssid);
                    //Log.d(TAG, " ssid.substring(0,2) " + ssid.substring(0,2));
                    if (ssid.substring(0,3).equals("ESP")) {
                        Log.d(TAG, " problem = true;");
                        problem = true;
                    }
                    String bssid = scanResult.BSSID;
                    Log.d(TAG, " bssid = " + bssid);
                    int level = scanResult.level;
                    Log.d(TAG, " level = " + level);

                    // Do something with the SSID, BSSID, and level
                }
                arrayAdapter = new ArrayAdapter<>(
                        this, R.layout.simple_list_item_1, stringList);
                listView.setAdapter(arrayAdapter);
            }
        } else {
            Log.d(TAG, " scanStarted = false");
        }
         if (problem && v.getId() == R.id.button2) {
            soundPool.play(soundPoolZv, 1.0f, 1.0f, 0, 1, 1.0f);
            problem = false;
        } else  {
            soundPool.stop(soundPoolZv);
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        if(soundPool != null) {
            soundPool.release();
            soundPool = null;
            Log.d(TAG, "onDestroy soundPool1");
        }
        Log.i(TAG, "Service: onDestroy");

    }
}