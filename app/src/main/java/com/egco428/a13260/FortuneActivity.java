package com.egco428.a13260;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import java.text.SimpleDateFormat;
import java.util.Calendar;;
import java.util.Random;

public class FortuneActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private long lastUpdate;
    Button actionbutton;
    boolean checked = false;
    private int count = 0;
    TextView resultFText, dateTimestamp;
    ImageView cookiesImage;
    public int randomTI;
    public static final String value = "value";
    public static final String value1 = "value1";
    public static final String value2 = "value2";


    final int[] imageId = {R.drawable.image1, R.drawable.image2, R.drawable.image3,
            R.drawable.image4, R.drawable.image5};
    String[] str_text = {"You will get A", "You're Lucky", "Don't Panic", "Something surprise you today", "Work Harder"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fortune);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();

        Toolbar myToolbar1 = (Toolbar) findViewById(R.id.my_toolbar1);
        setSupportActionBar(myToolbar1);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageButton mBack = (ImageButton) myToolbar1.findViewById(R.id.backButton);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        actionbutton = (Button) findViewById(R.id.actionButton);
        actionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(200);
                checked = true;
            }
        });
    }

    @Override
    public void onSensorChanged(final SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if (checked) {
                getAccelerometer(event);
            }
        }
    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;

        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = System.currentTimeMillis();

        if (accelationSquareRoot > 2) {
            if (actualTime - lastUpdate < 600) {
                return;
            }
            lastUpdate = actualTime;
            actionbutton.setText("SHAKING");
            Random r = new Random();
            randomTI = r.nextInt(imageId.length);
            count++;
        } else if (accelationSquareRoot < 0.75 && count > 2) {
            if (actualTime - lastUpdate < 50) {
                return;
            }
            lastUpdate = actualTime;
            setFortune();
        }
    }

    public void setFortune(){

        resultFText = (TextView) findViewById(R.id.resultFText);
        dateTimestamp = (TextView) findViewById(R.id.timestampText);
        cookiesImage = (ImageView) findViewById(R.id.cookiesImg);

        resultFText.setText(str_text[randomTI]);
        cookiesImage.setImageResource(imageId[randomTI]);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        final String formattedDate = df.format(c.getTime());

        dateTimestamp.setText(formattedDate);

        actionbutton.setText("SAVE");

        actionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                String inputValue = str_text[randomTI];
                String inputValue1 = formattedDate;
                String inputValue2 = String.valueOf(randomTI+1);
                intent.putExtra(value,inputValue);
                intent.putExtra(value1,inputValue1);
                intent.putExtra(value2,inputValue2);
                setResult(FortuneActivity.RESULT_OK, intent);
                finish();
                return;
            }
        });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener
                (this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        //will not get value from phone
    }
}



