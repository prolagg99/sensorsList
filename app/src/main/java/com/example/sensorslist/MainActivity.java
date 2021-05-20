package com.example.sensorslist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    SensorManager mSensorManager;

    // Individual light and proximity sensors.
    private Sensor mSensorLight;
    private Sensor mSensorAccelerometer;
    private Sensor mSensorMagnetometer;
    private Sensor mSensorGyroscope;
    private Sensor mSensorStepDetecter;

    // EditTexts to display current sensor values
    EditText mTextAccSensor_X, mTextAccSensor_Y, mTextAccSensor_Z;
    EditText mTextMagSensor_X, mTextMagSensor_Y, mTextMagSensor_Z;
    EditText mTextGyroSensor_X, mTextGyroSensor_Y, mTextGyroSensor_Z;
    EditText mTextLightSensor_LUX, mTextNmbrOfSteps;
    TextView mTextLightSensor, mTextGyroSensor, mTextMagnSensor, mTextStepCounterSensor;
    Button btnStart, btnReset, btnStepDetecter, btnSkyhookActivity;
    ImageView btnTestActivity, btnConnectivityActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //call the error msg from String resources
        String sensor_error = getResources().getString(R.string.error_no_sensor);

        mTextAccSensor_X = (EditText) findViewById(R.id.acc_x);
        mTextAccSensor_Y = (EditText) findViewById(R.id.acc_y);
        mTextAccSensor_Z = (EditText) findViewById(R.id.acc_z);

        mTextMagSensor_X = (EditText) findViewById(R.id.mag_x);
        mTextMagSensor_Y = (EditText) findViewById(R.id.mag_y);
        mTextMagSensor_Z = (EditText) findViewById(R.id.mag_z);

        mTextGyroSensor_X = (EditText) findViewById(R.id.gyro_x);
        mTextGyroSensor_Y = (EditText) findViewById(R.id.gyro_y);
        mTextGyroSensor_Z = (EditText) findViewById(R.id.gyro_z);

        mTextLightSensor_LUX = (EditText) findViewById(R.id.light_lux);
        mTextNmbrOfSteps = (EditText) findViewById(R.id.nmbrOfSteps);

        mTextMagnSensor = (TextView) findViewById(R.id.id_magn);
        mTextGyroSensor = (TextView) findViewById(R.id.id_gyro);
        mTextLightSensor = (TextView) findViewById(R.id.id_light);
        mTextStepCounterSensor = (TextView) findViewById(R.id.id_stepCounter);

        btnReset = (Button) findViewById(R.id.btnReset);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStepDetecter = (Button) findViewById(R.id.btnStepDetecter);

        // activities buttons
        btnTestActivity= (ImageView) findViewById(R.id.btn_testActivity);
        btnConnectivityActivity = (ImageView) findViewById(R.id.btn_connectivityActivity);
        btnSkyhookActivity = (Button) findViewById(R.id.btn_skyhookActivity);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorStepDetecter = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        // to check if device support the sensor
        if(mSensorGyroscope == null)
            mTextGyroSensor.setText("Gyroscope: " + sensor_error);
        if(mSensorMagnetometer == null)
            mTextMagnSensor.setText("Magnetometer: " + sensor_error);
        if(mSensorLight == null)
            mTextLightSensor.setText("Lumination: " + sensor_error);
        if(mSensorStepDetecter == null)
            mTextStepCounterSensor.setText("stepCounter: " + sensor_error);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("acc : " , "clicked btn start" );

                if (mSensorLight != null) {
                    mSensorManager.registerListener(MainActivity.this, mSensorLight,
                            SensorManager.SENSOR_DELAY_NORMAL);
                }
                if (mSensorAccelerometer != null) {
                    mSensorManager.registerListener(MainActivity.this, mSensorAccelerometer,
                            SensorManager.SENSOR_DELAY_NORMAL);
                }
                if (mSensorMagnetometer != null) {
                    mSensorManager.registerListener(MainActivity.this, mSensorMagnetometer,
                            SensorManager.SENSOR_DELAY_NORMAL);
                }
                if (mSensorGyroscope != null) {
                    mSensorManager.registerListener(MainActivity.this, mSensorGyroscope,
                            SensorManager.SENSOR_DELAY_NORMAL);
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStop();
                clearForm((ViewGroup) findViewById(R.id.mainFather));
            }
        });

        btnStepDetecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSensorStepDetecter != null) {
                    mSensorManager.registerListener(MainActivity.this, mSensorStepDetecter,
                            SensorManager.SENSOR_DELAY_NORMAL);
                }
            }
        });

        btnTestActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTestActivity = new Intent(getApplicationContext(), TestActivity.class);
                startActivity(intentTestActivity);
            }
        });

        btnConnectivityActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentConnectivityActivity = new Intent(getApplicationContext(), ConnectivityActivity.class);
                startActivity(intentConnectivityActivity);
            }
        });

        btnSkyhookActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSkyhookActivity = new Intent(getApplicationContext(), SkyHookActivity.class);
                startActivity(intentSkyhookActivity);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        clearForm((ViewGroup) findViewById(R.id.mainFather));
//        if (mSensorLight != null) {
//            mSensorManager.registerListener(this, mSensorLight,
//                    SensorManager.SENSOR_DELAY_NORMAL);
//        }
//        if (mSensorAccelerometer != null) {
//            mSensorManager.registerListener(this, mSensorAccelerometer,
//                    SensorManager.SENSOR_DELAY_NORMAL);
//        }
//        if (mSensorMagnetometer != null) {
//            mSensorManager.registerListener(this, mSensorMagnetometer,
//                    SensorManager.SENSOR_DELAY_NORMAL);
//        }
//        if (mSensorGyroscope != null) {
//            mSensorManager.registerListener(this, mSensorGyroscope,
//                    SensorManager.SENSOR_DELAY_NORMAL);
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        steps = 0;
        mSensorManager.unregisterListener(this);
    }

    private int steps = 0;
    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.e("acc : " , "value : " + event.values[0]);

        int sensorType = event.sensor.getType();
        float currentValue = event.values[0];

        final double alpha = 0.8;
        double gravity[] = new double[3];
        double linear_acceleration_acc[] = new double[3];
        double linear_acceleration_gyro[] = new double[3];
        double linear_acceleration_magn[] = new double[3];

        if (sensorType ==  Sensor.TYPE_ACCELEROMETER){
            // Isolate the force of gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        // Remove the gravity contribution with the high-pass filter.
        linear_acceleration_acc[0] = event.values[0] - gravity[0];
        linear_acceleration_acc[1] = event.values[1] - gravity[1];
        linear_acceleration_acc[2] = event.values[2] - gravity[2];
        }

        if (sensorType ==  Sensor.TYPE_GYROSCOPE){
            linear_acceleration_gyro[0] = event.values[0];
            linear_acceleration_gyro[1] = event.values[1];
            linear_acceleration_gyro[2] = event.values[2];
        }

        if (sensorType ==  Sensor.TYPE_MAGNETIC_FIELD){
            linear_acceleration_magn[0] = event.values[0];
            linear_acceleration_magn[1] = event.values[1];
            linear_acceleration_magn[2] = event.values[2];
        }

        switch (sensorType) {
            // Event came from the light sensor.
            case Sensor.TYPE_LIGHT: {
                // Handle light sensor
                mTextLightSensor_LUX.setText(getResources().getString(
                        R.string.label_light, currentValue));
                break;
            }
            case Sensor.TYPE_STEP_DETECTOR: {
                steps++;
                mTextNmbrOfSteps.setText(Integer.toString(steps));
                break;
            }
            case Sensor.TYPE_ACCELEROMETER: {
                mTextAccSensor_X.setText(getResources().getString(R.string.x_axis, linear_acceleration_acc[0]));
                mTextAccSensor_Y.setText(getResources().getString(R.string.y_axis, linear_acceleration_acc[1]));
                mTextAccSensor_Z.setText(getResources().getString(R.string.z_axis, linear_acceleration_acc[2]));
                break;
            }
            case Sensor.TYPE_GYROSCOPE: {
                mTextGyroSensor_X.setText(getResources().getString(R.string.x_axis, linear_acceleration_gyro[0]));
                mTextGyroSensor_Y.setText(getResources().getString(R.string.y_axis, linear_acceleration_gyro[1]));
                mTextGyroSensor_Z.setText(getResources().getString(R.string.z_axis, linear_acceleration_gyro[2]));
                break;
            }
            case Sensor.TYPE_MAGNETIC_FIELD: {
                mTextMagSensor_X.setText(getResources().getString(R.string.x_axis, linear_acceleration_magn[0]));
                mTextMagSensor_Y.setText(getResources().getString(R.string.y_axis, linear_acceleration_magn[1]));
                mTextMagSensor_Z.setText(getResources().getString(R.string.z_axis, linear_acceleration_magn[2]));
                break;
            }
            default:
                // do nothing
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void clearForm(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText)view).setText("");
            }

            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0))
                clearForm((ViewGroup)view);
        }
    }
}

