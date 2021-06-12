package com.example.sensorslist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;


public class WifiActivity extends AppCompatActivity {

    // for wifi list
    TextView tvWifiList;
    WifiManager mainWifi;
    WifiReceiver receiverWifi;
    List<ScanResult> wifiList;
    StringBuilder sb = new StringBuilder();

    final private int REQUEST_CODE_ASK_PERMISSIONS = 13;
    Button btnCellId;

    ListView listView;
    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;


    class WifiReceiver extends BroadcastReceiver {

        // This method call when number of wifi connections changed
        public void onReceive(Context c, Intent intent) {

            sb = new StringBuilder();
            wifiList = mainWifi.getScanResults();
            sb.append("\n        Number Of Wifi connections :" + wifiList.size()+"\n\n");

//            if(wifiList.size() != 0){
//                Toast.makeText(c, "wifi available" + wifiList.get(1).toString(), Toast.LENGTH_SHORT).show();
//            }

            for(int i = 0; i < wifiList.size(); i++){
                sb.append(new Integer(i+1).toString() + ". ");
                sb.append("WiFi Name :" + (wifiList.get(i).SSID) + " ");
                sb.append("\n\n");
                sb.append("@Mac :" + (wifiList.get(i).BSSID) + ", ");
                sb.append("RSSI " + (wifiList.get(i).level) + " ");
                sb.append("\n\n");
//                ivWifiSignalList.setVisibility(View.VISIBLE);
//                levelOfSignal(wifiList.get(i).level, ivWifiSignalList);

                listItems.add("Name: " + wifiList.get(i).SSID);
            }
            tvWifiList.setText("");
            adapter.notifyDataSetChanged();
//            tvWifiList.setText(sb);
        }

    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connectivity);

        // for wifi list
        tvWifiList = (TextView) findViewById(R.id.tvWifiList);
        mainWifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);



        listView = (ListView) findViewById(R.id.wifiList);
        adapter =  new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);

        btnCellId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("CID " , "from button");

                // for wifi list
                // check if GPS turned OFF
                final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
                if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    buildAlertMessageNoGps();
                }
                if (mainWifi.isWifiEnabled() == false)
                {
                    // If wifi disabled then enable it
                    Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled",
                            Toast.LENGTH_LONG).show();
                    mainWifi.setWifiEnabled(true);
                }
                // for wifi list
                receiverWifi = new WifiReceiver();
                registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                mainWifi.startScan();
                tvWifiList.setText("Starting Scan...");

                // request permission in run time
                requestPermissions(new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.READ_PHONE_STATE},
                        REQUEST_CODE_ASK_PERMISSIONS);

                if (ActivityCompat.checkSelfPermission(WifiActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(WifiActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(WifiActivity.this,
                        Manifest.permission.READ_PHONE_STATE) !=
                        PackageManager.PERMISSION_GRANTED){
                    return;
                }
//                Excellent >-50 dBm
//                Good -50 to -60 dBm
//                Fair -60 to -70 dBm
//                Weak < -70 dBm

            }
        });
    }

    private void levelOfSignal(int level, ImageView ivSignalLevel) {
        if (level <= 0 && level >= -50) {
            //Best signal
            ivSignalLevel.setImageResource(R.drawable.ic_signal_wifi_4_bar_black_24dp);
        } else if (level < -50 && level >= -70) {
            //Good signal
            ivSignalLevel.setImageResource(R.drawable.ic_signal_wifi_3_bar_black_24dp);
        } else if (level < -70 && level >= -80) {
            //Low signal
            ivSignalLevel.setImageResource(R.drawable.ic_signal_wifi_2_bar_black_24dp);
        } else if (level < -80 && level >= -100) {
            //Very weak signal
            ivSignalLevel.setImageResource(R.drawable.ic_signal_wifi_1_bar_black_24dp);
        } else {
            // no signals
            ivSignalLevel.setImageResource(R.drawable.ic_signal_wifi_0_bar_black_24dp);
        }
    }
}
