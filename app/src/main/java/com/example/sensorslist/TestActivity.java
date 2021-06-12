package com.example.sensorslist;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.studioidan.httpagent.HttpAgent;
import com.studioidan.httpagent.JsonCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity implements SensorEventListener {
    SensorManager mSensorManager;

    // sensors.
    private Sensor mSensorAccelerometer;
    private Sensor mSensorMagnetometer;
    private Sensor mSensorGyroscope;

    LinearLayout meanLayout, accMeanField, magnMeanField, gyroMeanField;
    CheckBox checkbox_Acc, checkbox_Magn, checkbox_gyro;

    // TextViews to display current sensor values
    EditText mTextAccSensor_X, mTextAccSensor_Y, mTextAccSensor_Z;
    EditText mTextMagSensor_X, mTextMagSensor_Y, mTextMagSensor_Z;
    EditText mTextGyroSensor_X, mTextGyroSensor_Y, mTextGyroSensor_Z;

    Button btnStartColl, btnTest;
    TextView id_behavior;

    // texView to display mean value of each axis of each sensor
    EditText x_accMean, y_accMean, z_accMean;
    EditText x_magMean, y_magMean, z_magMean;
    EditText x_gyroMean, y_gyroMean, z_gyroMean;

    boolean accChecked, magnChecked, gyroChecked = false;
    int nmbrOfCheck = 0;
    double[] resultAcc;
    double[] resultGyro;
    double[] resultMagn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // make the layout that have the values of mean invisible till the process finish (after 20s)
        meanLayout = (LinearLayout) findViewById(R.id.meanLayout);
        accMeanField = (LinearLayout) findViewById(R.id.accMeanField);
        magnMeanField = (LinearLayout) findViewById(R.id.magnMeanField);
        gyroMeanField = (LinearLayout) findViewById(R.id.gyroMeanField);

        checkbox_Acc = (CheckBox) findViewById(R.id.checkbox_Acc);
        checkbox_Magn = (CheckBox) findViewById(R.id.checkbox_Magn);
        checkbox_gyro = (CheckBox) findViewById(R.id.checkbox_gyro);

        // display sensors data --------------------------------------------------------------------
        mTextAccSensor_X = (EditText) findViewById(R.id.acc_x);
        mTextAccSensor_Y = (EditText) findViewById(R.id.acc_y);
        mTextAccSensor_Z = (EditText) findViewById(R.id.acc_z);

        mTextMagSensor_X = (EditText) findViewById(R.id.mag_x);
        mTextMagSensor_Y = (EditText) findViewById(R.id.mag_y);
        mTextMagSensor_Z = (EditText) findViewById(R.id.mag_z);

        mTextGyroSensor_X = (EditText) findViewById(R.id.gyro_x);
        mTextGyroSensor_Y = (EditText) findViewById(R.id.gyro_y);
        mTextGyroSensor_Z = (EditText) findViewById(R.id.gyro_z);

        btnStartColl = (Button) findViewById(R.id.btnStartColl);
        btnTest = (Button) findViewById(R.id.btnTest);

        id_behavior = (TextView) findViewById(R.id.id_behavior);
        // for mean function  ----------------------------------------------------------------------
        x_accMean = (EditText) findViewById(R.id.x_accMean);
        y_accMean = (EditText) findViewById(R.id.y_accMean);
        z_accMean = (EditText) findViewById(R.id.z_accMean);

        x_magMean = (EditText) findViewById(R.id.x_magMean);
        y_magMean = (EditText) findViewById(R.id.y_magMean);
        z_magMean = (EditText) findViewById(R.id.z_magMean);

        x_gyroMean = (EditText) findViewById(R.id.x_gyroMean);
        y_gyroMean = (EditText) findViewById(R.id.y_gyroMean);
        z_gyroMean = (EditText) findViewById(R.id.z_gyroMean);

        disableEditText(mTextAccSensor_X);disableEditText(mTextAccSensor_Y);disableEditText(mTextAccSensor_Z);
        disableEditText(mTextMagSensor_X);disableEditText(mTextMagSensor_Y);disableEditText(mTextMagSensor_Z);
        disableEditText(mTextGyroSensor_X);disableEditText(mTextGyroSensor_Y);disableEditText(mTextGyroSensor_Z);

        disableEditText(x_accMean);disableEditText(y_accMean);disableEditText(z_accMean);
        disableEditText(x_magMean);disableEditText(y_magMean);disableEditText(z_magMean);
        disableEditText(x_gyroMean);disableEditText(y_gyroMean);disableEditText(z_gyroMean);


        final Handler handler = new Handler();
            btnStartColl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    for(int i=0; i<3; i++ ){
//                        accl_mean[i] =  0;
//                    }
                    clearForm((ViewGroup) findViewById(R.id.testFather));
                    clearForm((ViewGroup) findViewById(R.id.meanLayout));
                    accMeanField.setVisibility(View.INVISIBLE);
                    magnMeanField.setVisibility(View.INVISIBLE);
                    gyroMeanField.setVisibility(View.INVISIBLE);

                    if (accChecked) {
                        nmbrOfCheck++;
                        if (mSensorAccelerometer != null) {
                            mSensorManager.registerListener(TestActivity.this, mSensorAccelerometer,
                                    SensorManager.SENSOR_DELAY_NORMAL);
                        }
                    }
                    if (magnChecked) {
                        nmbrOfCheck++;
                        if (mSensorMagnetometer != null) {
                            mSensorManager.registerListener(TestActivity.this, mSensorMagnetometer,
                                    SensorManager.SENSOR_DELAY_NORMAL);
                        }
                    }
                    if (gyroChecked) {
                        nmbrOfCheck++;
                        if (mSensorGyroscope != null) {
                            mSensorManager.registerListener(TestActivity.this, mSensorGyroscope,
                                    SensorManager.SENSOR_DELAY_NORMAL);
                        }
                    }

                    if(nmbrOfCheck != 0) {
                        // disable the checkboxes when the collecting of data begin (that means one/all  sensor/s is/are checked)
                        btnStartColl.setEnabled(false);
                        checkbox_Acc.setEnabled(false);
                        checkbox_Magn.setEnabled(false);
                        checkbox_gyro.setEnabled(false);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                nmbrOfCheck = 0;
                                // enable the checkboxes when 20s of collecting done
                                btnStartColl.setEnabled(true);
                                checkbox_Acc.setEnabled(true);
                                checkbox_Magn.setEnabled(true);
                                checkbox_gyro.setEnabled(true);
                                // stop listening for all sensors after 20s
                                mSensorManager.unregisterListener(TestActivity.this);

                                Log.e("listSize after collect" , " value : " + acclDataCollection.size());
                                // calculate the mean after the collection of data done
                                double mean[] = new double[] {0,0,0};
                                for (ValuesOf_x_y_z d : acclDataCollection ){
                                    mean[0] += d.x;
                                    mean[1] += d.y;
                                    mean[2] += d.z;
                                }
                                Log.e("sum ", "value" + mean[0] + " " + mean[1] + " " + mean[2] );
                                mean[0] /= acclDataCollection.size();
                                mean[1] /= acclDataCollection.size();
                                mean[2] /= acclDataCollection.size();
                                Log.e("division ", "value" + mean[0] + " " + mean[1] + " " + mean[2] );

                                if(acclDataCollection.size() != 0){
                                    resultAcc = meanFun(acclDataCollection, acclDataCollection.size());
                                    x_accMean.setText(getResources().getString(R.string.x_axis, resultAcc[0]));
                                    y_accMean.setText(getResources().getString(R.string.y_axis, resultAcc[1]));
                                    z_accMean.setText(getResources().getString(R.string.z_axis, resultAcc[2]));
                                    accMeanField.setVisibility(View.VISIBLE);
                                    Log.e("the value", "value" + resultAcc[0]);
                                    // before clear data i should upload it to the dataBase
                                    acclDataCollection.clear();
                                }
                                Log.e("listSize after clean" , " value : " + acclDataCollection.size());

                                if(magnDataCollection.size() != 0){
                                    resultGyro = meanFun(magnDataCollection, magnDataCollection.size());
                                    x_magMean.setText(getResources().getString(R.string.x_axis, resultGyro[0]));
                                    y_magMean.setText(getResources().getString(R.string.y_axis, resultGyro[1]));
                                    z_magMean.setText(getResources().getString(R.string.z_axis, resultGyro[2]));
                                    magnMeanField.setVisibility(View.VISIBLE);
                                    Log.e("the value", "value" + resultAcc[0]);

                                    // before clear data i should upload it to the dataBase
                                    magnDataCollection.clear();
                                }
                                if(gyroDataCollection.size() != 0){
                                    resultMagn = meanFun(gyroDataCollection, gyroDataCollection.size());
                                    x_gyroMean.setText(getResources().getString(R.string.x_axis, resultMagn[0]));
                                    y_gyroMean.setText(getResources().getString(R.string.y_axis, resultMagn[1]));
                                    z_gyroMean.setText(getResources().getString(R.string.z_axis, resultMagn[2]));
                                    gyroMeanField.setVisibility(View.VISIBLE);
                                    Log.e("the value", "value" + resultAcc[0]);

                                    // before clear data i should upload it to the dataBase
                                    gyroDataCollection.clear();
                                }
                                HttpAgent.post("http://192.168.1.34:8080/WebServiceTest/Greeting")
                                        .queryParams("acc_x",resultAcc[0]+"","acc_y",resultAcc[1]+"","acc_z",resultAcc[2]+""
                                                ,"gyro_x",resultGyro[0]+"","gyro_y",resultGyro[1]+"","gyro_z",resultGyro[2]+"",
                                                "magn_x",resultGyro[0]+"","magn_y",resultGyro[1]+"","magn_z",resultGyro[2]+"")
                                        .goJson(new JsonCallback() {
                                            @Override
                                            protected void onDone(boolean success, JSONObject jsonObject) {
                                                    Log.e("msg", "value" + jsonObject);
                                                try {
                                                    Log.e("msg", "value " + jsonObject.get("Greeting"));
                                                    id_behavior.setText(jsonObject.get("Greeting").toString());
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
//                                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
//                                                    Toast.makeText(TestActivity.this, jsonArray.toString(), Toast.LENGTH_LONG).show();

                                            }
                                        });
                            }
                        }, 5000);
                    }
                }
            });

//            String.valueOf()

//        Log.e("the value", "value" + resultAcc[0]);
//             acc_x,acc_y,acc_z,gyro_x,gyro_y,gyro_z,magn_x,magn_y,magn_z
//        HttpAgent.post("http://192.168.1.35:8080/WebServiceTest/Greeting")
//                .queryParams("acc_x",resultAcc[0]+"","acc_y",resultAcc[1]+"","acc_z",resultAcc[2]+""
//                        ,"gyro_x",resultGyro[0]+"","gyro_y",resultGyro[1]+"","gyro_z",resultGyro[2]+"",
//                        "magn_x",resultGyro[0]+"","magn_y",resultGyro[1]+"","magn_z",resultGyro[2]+"")
//                .withBody("{name:popapp ,age:27}")
//                .goJson(new JsonCallback() {
//                    @Override
//                    protected void onDone(boolean success, JSONObject jsonObject) {
//                        Toast.makeText(TestActivity.this, jsonObject.toString(), Toast.LENGTH_LONG).show();
//                    }
//                });

        // till now doing nothing
        checkbox_gyro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
           }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        accMeanField.setVisibility(View.INVISIBLE);
        magnMeanField.setVisibility(View.INVISIBLE);
        gyroMeanField.setVisibility(View.INVISIBLE);
        clearForm((ViewGroup) findViewById(R.id.meanLayout));
        clearForm((ViewGroup) findViewById(R.id.testFather));
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this);
    }

    // arrayList for collecting data and calculate the mean function
    final ArrayList<ValuesOf_x_y_z> acclDataCollection = new ArrayList<>();
    final ArrayList<ValuesOf_x_y_z> magnDataCollection = new ArrayList<>();
    final ArrayList<ValuesOf_x_y_z> gyroDataCollection = new ArrayList<>();

    // tables for calculate the mean of each sensor (sum and then division)
    double accl_mean[] = new double [] {0,0,0};

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();

        final double alpha = 0.8;
        double gravity[] = new double[3];
        // tables for displaying data of sensors instead of using event.values directly
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

            // i used two methods for acce arrayList and array
//            for (int i=0; i<3; i++){
//                accl_mean[i] = accl_mean[i] + linear_acceleration_acc[i];
//            }

            // collected the data for 20s to set it in dataBase so i used arrayList
            acclDataCollection.add(new ValuesOf_x_y_z(linear_acceleration_acc[0],linear_acceleration_acc[1],linear_acceleration_acc[2]));
        }

        if (sensorType ==  Sensor.TYPE_MAGNETIC_FIELD){
            linear_acceleration_magn[0] = event.values[0];
            linear_acceleration_magn[1] = event.values[1];
            linear_acceleration_magn[2] = event.values[2];

            // collected the data for 20s to set it in dataBase so i used arrayList
            magnDataCollection.add(new ValuesOf_x_y_z(linear_acceleration_magn[0],linear_acceleration_magn[1],linear_acceleration_magn[2]));
        }

        if (sensorType ==  Sensor.TYPE_GYROSCOPE){
            linear_acceleration_gyro[0] = event.values[0];
            linear_acceleration_gyro[1] = event.values[1];
            linear_acceleration_gyro[2] = event.values[2];

            // collected the data for 20s to set it in dataBase so i used arrayList
            gyroDataCollection.add(new ValuesOf_x_y_z(linear_acceleration_gyro[0],linear_acceleration_gyro[1],linear_acceleration_gyro[2]));
        }

        switch (sensorType) {
            // Event came from the sensors.
            case Sensor.TYPE_ACCELEROMETER: {
                mTextAccSensor_X.setText(getResources().getString(R.string.x_axis, linear_acceleration_acc[0]));
                mTextAccSensor_Y.setText(getResources().getString(R.string.y_axis, linear_acceleration_acc[1]));
                mTextAccSensor_Z.setText(getResources().getString(R.string.z_axis, linear_acceleration_acc[2]));

//                x_accMean.setText(getResources().getString(R.string.x_axis, accl_mean[0]));
//                y_accMean.setText(getResources().getString(R.string.y_axis, accl_mean[1]));
//                z_accMean.setText(getResources().getString(R.string.z_axis, accl_mean[2]));
                break;
            }
            case Sensor.TYPE_MAGNETIC_FIELD: {
                mTextMagSensor_X.setText(getResources().getString(R.string.x_axis, linear_acceleration_magn[0]));
                mTextMagSensor_Y.setText(getResources().getString(R.string.y_axis, linear_acceleration_magn[1]));
                mTextMagSensor_Z.setText(getResources().getString(R.string.z_axis, linear_acceleration_magn[2]));
                break;
            }
            case Sensor.TYPE_GYROSCOPE: {
                mTextGyroSensor_X.setText(getResources().getString(R.string.x_axis, linear_acceleration_gyro[0]));
                mTextGyroSensor_Y.setText(getResources().getString(R.string.y_axis, linear_acceleration_gyro[1]));
                mTextGyroSensor_Z.setText(getResources().getString(R.string.z_axis, linear_acceleration_gyro[2]));
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

    public void onCheckboxAcc(View view) {
        if(accChecked){
            checkbox_Acc.setTextColor(Color.parseColor("#000000"));
        }else{
            checkbox_Acc.setTextColor(Color.parseColor("#ff547f"));
        }
        accChecked = !accChecked;
    }

    public void onCheckboxMagn(View view) {
        if(magnChecked){
            checkbox_Magn.setTextColor(Color.parseColor("#000000"));
        }else{
            checkbox_Magn.setTextColor(Color.parseColor("#ff547f"));
        }
        magnChecked = !magnChecked;
    }

    public void onCheckboxGyro(View view) {
        if(gyroChecked){
            checkbox_gyro.setTextColor(Color.parseColor("#000000"));
        }else{
            checkbox_gyro.setTextColor(Color.parseColor("#ff547f"));
        }
        gyroChecked = !gyroChecked;
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

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setCursorVisible(false);
    }

    private double[] meanFun (ArrayList<ValuesOf_x_y_z> dataCollection, int size) {
        double mean[] = new double[] {0,0,0};
        for (ValuesOf_x_y_z d : dataCollection ){
            mean[0] += d.x;
            mean[1] += d.y;
            mean[2] += d.z;
        }
        mean[0] /= size;
        mean[1] /= size;
        mean[2] /= size;
        return mean;
    }
}

class ValuesOf_x_y_z{
    double x,y,z;

    ValuesOf_x_y_z(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}


