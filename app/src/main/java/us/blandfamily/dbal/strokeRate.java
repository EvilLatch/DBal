package us.blandfamily.dbal;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class strokeRate extends AppCompatActivity implements SensorEventListener, LocationListener {
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private float smooth80 = 0.0f;
    private float smooth40 = 0.0f;
    private float threshold = 0.2f;
    private boolean wasAbove = false;
    private long lastHitTime = 0;
    private float strokeRate = 0.0f;
    private boolean firstHit = true;
    private long lastUpdate = 0;
    private TextView strokeRateText;
    private TextView speedText;
    private TextView strokeRate20;
    private float mpsToKPH = 1000 / 360;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stroke_rate);
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_GAME);
        strokeRateText = (TextView)findViewById(R.id.strokeRateText);
        speedText = (TextView)findViewById(R.id.speedText);
        //strokeRate20 = (TextView)findViewById(R.id.strokeRate20);
        lastUpdate = System.currentTimeMillis();
        LocationManager locationManager = (LocationManager) this .getSystemService(Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();


            if ((curTime - lastUpdate) > 20) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                smooth80 = 0.3f * smooth80 + 0.7f * y;
                smooth40 = 0.8f * smooth40 + 0.2f * y;
                long timeDelta = curTime - lastHitTime;

                //strokeRateText.setText(String.valueOf(y));
                if(!wasAbove && (smooth80 - smooth40) > threshold)
                {
                    //strokeRate80.setText(String.valueOf(smooth80));
                    //strokeRate20.setText(String.valueOf(smooth40));
                    // trigger a hit
                    wasAbove = true;

                    float strokeRate = (60.0f / ( (float) timeDelta)) * 1000.0f;
                    // update strokeRate
                    int intStrokeRate = (int) strokeRate;
                    if(!firstHit)
                    {
                        // Maybe reset the text color to active?
                        strokeRateText.setBackgroundColor(Color.WHITE);
                        strokeRateText.setText(String.valueOf(intStrokeRate));
                    }
                    else
                    {
                        firstHit = false;
                    }
                    lastHitTime = curTime;
                }
                else if(smooth40 > smooth80)
                {
                    wasAbove = false;
                }
                if(timeDelta > 4000)
                {
                    // Maybe also change the text color?
                    strokeRateText.setBackgroundColor(Color.DKGRAY);
                    firstHit = true;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        location.getLatitude();
        String speedTextOut = String.format("KPH: %.1f", location.getSpeed() * mpsToKPH);

        speedText.setText(speedTextOut);
        //Toast.makeText(context, "Current speed:" + location.getSpeed(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
