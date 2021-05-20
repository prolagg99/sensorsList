package com.example.sensorslist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.skyhookwireless.wps.IWPS;
import com.skyhookwireless.wps.WPSContinuation;
import com.skyhookwireless.wps.WPSLocation;
import com.skyhookwireless.wps.WPSLocationCallback;
import com.skyhookwireless.wps.WPSReturnCode;
import com.skyhookwireless.wps.WPSStreetAddressLookup;
import com.skyhookwireless.wps.XPS;

import java.util.List;
import java.util.Locale;

public class SkyHookActivity extends AppCompatActivity {

    private TextView tv;
    private ProgressBar progress;
    private IWPS xps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skyhook);
        setTitle(String.format(
                Locale.ROOT,
                "%s (XPS v%s)",
                getString(R.string.app_name),
                XPS.getVersion()));

        tv = findViewById(R.id.tv);
        progress = findViewById(R.id.progress);

        xps = new XPS(this);

        try {
            xps.setKey("eJwNwcENACAIBLC3w5BwISI8EXQp4-7aooE_cFdrBxtik0ElZhSuTFm5KWKJD_WsyPsAESsLVg");
        } catch (IllegalArgumentException e) {
            tv.setText("Put your API key in the source code");
        }

        ActivityCompat.requestPermissions(
                this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 0);
    }

    public void onClick(View view) {
        determineLocation();
    }

    private void determineLocation() {
        if (! hasLocationPermission()) {
            tv.setText("Permission denied");
            return;
        }

        tv.setVisibility(View.INVISIBLE);
        progress.setVisibility(View.VISIBLE);

        xps.getLocation(
                null,
                WPSStreetAddressLookup.WPS_FULL_STREET_ADDRESS_LOOKUP,
                true,
                new WPSLocationCallback() {
                    @Override
                    public void handleWPSLocation(final WPSLocation location) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText(String.format(
                                        Locale.ROOT,
                                        "%.7f %.7f +/-%dm\n\n%s\n\n%s \n\n%s \n\n%s \n\n%s",
                                        location.getLatitude(),
                                        location.getLongitude(),
                                        location.getHPE(),
                                        location.hasTimeZone() ? location.getTimeZone() : "No timezone",
                                        location.hasStreetAddress() ? location.getStreetAddress() : "No address",
                                        location.hasAltitude() ? location.getAltitude() : "No altitude",
                                        location.hasIp() ? location.getIp() : "No Ip",
                                        getCompleteAddressString(location.getLatitude(), location.getLongitude())
                                ));

                            }
                        });
                    }

                    @Override
                    public void done() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv.setVisibility(View.VISIBLE);
                                progress.setVisibility(View.INVISIBLE);
                            }
                        });
                    }

                    @Override
                    public WPSContinuation handleError(final WPSReturnCode returnCode) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText(returnCode.toString());
                            }
                        });

                        return WPSContinuation.WPS_CONTINUE;
                    }
                });
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction add", strReturnedAddress.toString());
            } else {
                Log.w("My Current loction add", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction add", "Canont get Address!");
        }
        return strAdd;
    }

    private boolean hasLocationPermission() {
        return checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }
}
