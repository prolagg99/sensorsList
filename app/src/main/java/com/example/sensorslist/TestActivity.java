package com.example.sensorslist;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity implements SensorEventListener {
    LinearLayout display;


    // TextViews to display current sensor values
    TextView mTextAccSensor_X, mTextAccSensor_Y, mTextAccSensor_Z;
    TextView mTextMagSensor_X, mTextMagSensor_Y, mTextMagSensor_Z;
    TextView mTextGyroSensor_X, mTextGyroSensor_Y, mTextGyroSensor_Z;

    Button btnStartColl, btnTest, btnReset;
    TextView id_behavior;

    TextView x_accMean, y_accMean, z_accMean;
    TextView x_magMean, y_magMean, z_magMean;
    TextView x_gyroMean, y_gyroMean, z_gyroMean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // make the layout that have the values of mean invisible till the process finish (of the mean)
        LinearLayout display = (LinearLayout) findViewById(R.id.meanLayout);
        display.setVisibility(View.INVISIBLE);

        // display sensors data --------------------------------------------------------------------
        mTextAccSensor_X = (TextView) findViewById(R.id.acc_x);
        mTextAccSensor_Y = (TextView) findViewById(R.id.acc_y);
        mTextAccSensor_Z = (TextView) findViewById(R.id.acc_z);

        mTextMagSensor_X = (TextView) findViewById(R.id.mag_x);
        mTextMagSensor_Y = (TextView) findViewById(R.id.mag_y);
        mTextMagSensor_Z = (TextView) findViewById(R.id.mag_z);

        mTextGyroSensor_X = (TextView) findViewById(R.id.gyro_x);
        mTextGyroSensor_Y = (TextView) findViewById(R.id.gyro_y);
        mTextGyroSensor_Z = (TextView) findViewById(R.id.gyro_z);

        btnReset = (Button) findViewById(R.id.btnReset);
        btnStartColl = (Button) findViewById(R.id.btnStartColl);
        btnTest = (Button) findViewById(R.id.btnTest);

        id_behavior = (TextView) findViewById(R.id.id_behavior);
        // for mean function  ----------------------------------------------------------------------
        x_accMean = (TextView) findViewById(R.id.x_accMean);
        y_accMean = (TextView) findViewById(R.id.x_accMean);
        z_accMean = (TextView) findViewById(R.id.x_accMean);

        x_magMean = (TextView) findViewById(R.id.x_magMean);
        y_magMean = (TextView) findViewById(R.id.y_magMean);
        z_magMean = (TextView) findViewById(R.id.z_magMean);

        x_gyroMean = (TextView) findViewById(R.id.x_gyroMean);
        y_gyroMean = (TextView) findViewById(R.id.y_gyroMean);
        z_gyroMean = (TextView) findViewById(R.id.z_gyroMean);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void onCheckboxAcc(View view) {
    }

    public void onCheckboxMagn(View view) {
    }

    public void onCheckboxGyro(View view) {
    }
}
