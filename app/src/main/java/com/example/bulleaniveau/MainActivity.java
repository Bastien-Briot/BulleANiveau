package com.example.bulleaniveau;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ImageView IV_Rectangle;
    private SensorManager mgr;
    private Sensor gravitySensor;
    private int screenWidth, screenHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IV_Rectangle = findViewById(R.id.IV_Rec);

        // Récupère la taille de l'écran
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        // Initisalisation du gravity sensor
        mgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        gravitySensor = mgr.getDefaultSensor(Sensor.TYPE_GRAVITY);
        mgr.registerListener(gravitySensorListener, gravitySensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    private final SensorEventListener gravitySensorListener = new SensorEventListener() {

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        @Override
        public void onSensorChanged(SensorEvent event) {

            if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
                // Change le text des TextView avec les informations du gravity sensor
                ((TextView)findViewById(R.id.txtValues)).setText("Y : " + String.format("%.1f", event.values[0]));
                ((TextView)findViewById(R.id.txtValues2)).setText("X : " + String.format("%.1f", event.values[1]));

                // Récupère les valeurs du gravity Sensor
                float y = Math.round(event.values[0]);
                float x = Math.round(event.values[1]);

                // Récupère les dimensions de l'image
                int imageViewWidth = IV_Rectangle.getWidth();
                int imageViewHeight = IV_Rectangle.getHeight();

                // Obtenir la position
                float imageViewX = IV_Rectangle.getX();
                float imageViewY = IV_Rectangle.getY();

                // Modifier la position de l'image
                IV_Rectangle.setX(imageViewX + x * 2);
                IV_Rectangle.setY(imageViewY + y * 2);

                // Test pour voir si l'image est en dehors de l'écran
                if (imageViewX < 0) {
                    IV_Rectangle.setX(0);
                }
                if (imageViewY < 0) {
                    IV_Rectangle.setY(0);
                }
                if (imageViewX + imageViewWidth > screenWidth) {
                    IV_Rectangle.setX(screenWidth - imageViewWidth);
                }
                if (imageViewY + imageViewHeight > screenHeight) {
                    IV_Rectangle.setY(screenHeight - imageViewHeight * 2);
                }

                // Ramène l'image au millieu si l'écran est stable
                if (Math.round(x) == 0 && Math.round(y) == 0) {
                    IV_Rectangle.setX((float) (screenWidth - imageViewWidth) / 2);
                    IV_Rectangle.setY((float) (screenHeight - imageViewHeight * 2) / 2);

                }
            }

        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mgr.registerListener(gravitySensorListener, gravitySensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mgr.unregisterListener(gravitySensorListener);
    }

}