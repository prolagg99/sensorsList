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
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.CellIdentityLte;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import 	android.telephony.SignalStrength;
import 	android.telephony.CellIdentityGsm;
import 	android.telephony.CellInfoLte;
import 	android.telephony.CellInfoWcdma;
import 	android.telephony.CellIdentityWcdma;
import java.math.BigInteger;
import java.net.InetAddress;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import java.lang.reflect.Method;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

public class ConnectivityActivity extends AppCompatActivity {

    // for wifi list
    TextView tvWifiList;
    WifiManager mainWifi;
    WifiReceiver receiverWifi;
    List<ScanResult> wifiList;
    StringBuilder sb = new StringBuilder();

    final private int REQUEST_CODE_ASK_PERMISSIONS = 13;
    TextView tvCellIdCode1, tvWifiSignal, tvRouterIpAddress, tvRouterMacAddress,
            tvNetworkType, tvCellRSSI, tvCellIDCode2;
    ImageView ivWifiSignal, ivWifiSignalList;
    Button btnCellId;

    TelephonyManager mTelephonyManager;
    MyPhoneStateListener mPhoneStatelistener;
    int mSignalStrength = 0;

    ListView listView;
    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;



    class MyPhoneStateListener extends PhoneStateListener {

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {

            super.onSignalStrengthsChanged(signalStrength);
            mSignalStrength = signalStrength.getGsmSignalStrength();
            mSignalStrength = (2 * mSignalStrength) - 113; // -> dBm
            Log.e("gsm rssi", "value :" + mSignalStrength);
            tvCellRSSI.setText(getResources().getString(R.string.cell_RSSI, mSignalStrength));
        }
    }

    public class MobileInfoRecognizer {
        public String getCellInfo(CellInfo cellInfo) {
            String additional_info = null;
            if (cellInfo instanceof CellInfoGsm) {
                CellInfoGsm cellInfoGsm = (CellInfoGsm) cellInfo;
                CellIdentityGsm cellIdentityGsm = cellInfoGsm.getCellIdentity();
                additional_info = "cell identity GSM " + cellIdentityGsm.getCid() + "\n"
                        + "Mobile country code " + cellIdentityGsm.getMcc() + "\n"
                        + "Mobile network code " + cellIdentityGsm.getMnc() + "\n"
                        + "local area " + cellIdentityGsm.getLac() + "\n";
            } else if (cellInfo instanceof CellInfoLte) {
                CellInfoLte cellInfoLte = (CellInfoLte) cellInfo;
                CellIdentityLte cellIdentityLte = cellInfoLte.getCellIdentity();
                additional_info = "cell identity LTE " + cellIdentityLte.getCi() + "\n"
                        + "Mobile country code " + cellIdentityLte.getMcc() + "\n"
                        + "Mobile network code " + cellIdentityLte.getMnc() + "\n"
                        + "physical cell " + cellIdentityLte.getPci() + "\n"
                        + "Tracking area code " + cellIdentityLte.getTac() + "\n";
            } else if (cellInfo instanceof CellInfoWcdma){
                CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfo;
                CellIdentityWcdma cellIdentityWcdma = cellInfoWcdma.getCellIdentity();
                additional_info = "cell identity WCDMA " + cellIdentityWcdma.getCid() + "\n"
                        + "Mobile country code " + cellIdentityWcdma.getMcc() + "\n"
                        + "Mobile network code " + cellIdentityWcdma.getMnc() + "\n"
                        + "local area " + cellIdentityWcdma.getLac() + "\n";
            }
            return additional_info;
        }
    }

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

        tvCellIdCode1 = (TextView) findViewById(R.id.tvCellIDCode1);
        tvWifiSignal = (TextView) findViewById(R.id.tvWifiSignal);
        tvRouterIpAddress = (TextView) findViewById(R.id.tvRouterIpAddress);
        tvRouterMacAddress = (TextView) findViewById(R.id.tvRouterMacAddress);
        ivWifiSignal = (ImageView) findViewById(R.id.ivWifiSignal);
        btnCellId = (Button) findViewById(R.id.btnCellID);
        tvNetworkType = (TextView) findViewById(R.id.tvNetworkType);
        tvCellRSSI = (TextView) findViewById(R.id.tvCellRSSI);
        tvCellIDCode2 = (TextView) findViewById(R.id.tvCellIDCode2);


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

                if (ActivityCompat.checkSelfPermission(ConnectivityActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(ConnectivityActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(ConnectivityActivity.this,
                        Manifest.permission.READ_PHONE_STATE) !=
                        PackageManager.PERMISSION_GRANTED){
                            return;
                        }

                // get the cell ID & Location area code
                final TelephonyManager telephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                final GsmCellLocation location = (GsmCellLocation) telephony.getCellLocation();
//                Toast.makeText(ConnectivityActivity.this,"cellID " + location.getCid()
//                        + " LAC " + location.getLac(), Toast.LENGTH_LONG).show();
                if (location != null) {
                    tvCellIdCode1.setText(getResources().getString(R.string.cell_id, location.getLac(), location.getCid()));
//                    msg.setText("LAC: " + location.getLac() + " CID: " + location.getCid());
                }else {
//                    msg.setText("NULL");
                }

                // get information about connection (GSM / LTE / WCDMA--> technolgie of multiplixing used by 3G)
                MobileInfoRecognizer mobileInfoRecognizer = new MobileInfoRecognizer();
                List<CellInfo> cellInfos = telephony.getAllCellInfo();
                String additional_info = mobileInfoRecognizer.getCellInfo(cellInfos.get(0));
                Log.e("connection information" , " value " + additional_info);
                tvCellIDCode2.setText(additional_info);

                // RSSI of cell
                mPhoneStatelistener = new MyPhoneStateListener();
//                mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                telephony.listen(mPhoneStatelistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

//                CellInfoGsm cellinfogsm = (CellInfoGsm)telephony.getAllCellInfo().get(0);
//                CellSignalStrengthGsm cellSignalStrengthGsm = cellinfogsm.getCellSignalStrength();
//                cellSignalStrengthGsm.getDbm();
//                Log.e("RSSI for GSM" , "value :" +  cellSignalStrengthGsm.getDbm());


                // get the SSID & RSSI & ip_@ of wireless network wifi
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();

                String ssid = wifiInfo.getSSID();
                int rssi = wifiInfo.getRssi();
                tvWifiSignal.setText(getResources().getString(R.string.wifi_signal, ssid, rssi));
//                Toast.makeText(ConnectivityActivity.this,"SSID " + ssid + " RSSI " + rssi, Toast.LENGTH_LONG).show();

                // get the Mac @ of router
                String BSSID = wifiInfo.getBSSID();
                Toast.makeText(ConnectivityActivity.this,"BSSID " + BSSID, Toast.LENGTH_LONG).show();
                tvRouterMacAddress.setText(getResources().getString(R.string.mac_address, BSSID));

                // get the ip_@ of wifi
                final DhcpInfo dhcp = wifiManager.getDhcpInfo();
                int ipAddress = dhcp.gateway;
                ipAddress = (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) ?
                        Integer.reverseBytes(ipAddress) : ipAddress;
                byte[] ipAddressByte = BigInteger.valueOf(ipAddress).toByteArray();
                try {
                    InetAddress myAddr = InetAddress.getByAddress(ipAddressByte);
                    tvRouterIpAddress.setText(getResources().getString(R.string.ip_address, myAddr.getHostAddress()));
//                    Toast.makeText(ConnectivityActivity.this,"ip address " + myAddr.getHostAddress(), Toast.LENGTH_LONG).show();
                }catch (Exception e){
                }

                // get the signal level and display it as icon
                int numberOfLevels = 5;
                int level = WifiManager.compareSignalLevel(wifiInfo.getRssi(), numberOfLevels);
//                Toast.makeText(ConnectivityActivity.this,"level " + level, Toast.LENGTH_LONG).show();

                levelOfSignal(level, ivWifiSignal);


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

//        part[0] = "Signalstrength:"  _ignore this, it's just the title_
//
//        parts[1] = GsmSignalStrength
//
//        parts[2] = GsmBitErrorRate
//
//        parts[3] = CdmaDbm
//
//        parts[4] = CdmaEcio
//
//        parts[5] = EvdoDbm
//
//        parts[6] = EvdoEcio
//
//        parts[7] = EvdoSnr
//
//        parts[8] = LteSignalStrength
//
//        parts[9] = LteRsrp
//
//        parts[10] = LteRsrq
//
//        parts[11] = LteRssnr
//
//        parts[12] = LteCqi
//
//        parts[13] = gsm|lte|cdma
//
//        parts[14] = _not really sure what this number is_
