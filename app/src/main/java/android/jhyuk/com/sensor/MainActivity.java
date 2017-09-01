package android.jhyuk.com.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    SensorManager sm;
    Sensor s, l;
    Graph mGraph;
    Graph mGraph2;
    Graph mGraph3;
    TextView mText;
    float mark, result, temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        s = sm.getDefaultSensor(Sensor.TYPE_PRESSURE);
        l = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
//        mGraph = (Graph) findViewById(R.id.graph);
        mGraph2 = (Graph) findViewById(R.id.graph2);
        mGraph3 = (Graph) findViewById(R.id.graph3);
//        List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);
//        for(Sensor s : sensors){
//            StringBuilder sb = new StringBuilder();
//            sb.append(s.getName()).append("\n");
//            sb.append(s.getType()).append("\n");
//            sb.append(s.getVendor()).append("\n");
//            sb.append(s.getMinDelay()).append("\n");
//            sb.append(s.getResolution()).append("\n");
//            Log.e("SensorInfo", sb.toString());
//        }
        findViewById(R.id.mark).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mark = temp;
            }
        });
        findViewById(R.id.result).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result = temp;
                float m = (1013 - 954.61f) / 500;
                ((Button) findViewById(R.id.result)).setText(result + ":" + (result - mark) / m);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(this, s, SensorManager.SENSOR_DELAY_FASTEST);
        sm.registerListener(lightListener, l, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
//        float y = sensorEvent.values[1];
//        float z = sensorEvent.values[2];
//            mGraph.setPoint((int) x*10);
//            mGraph2.setPoint((int) y*10);
        Log.e("pressure", x + "");
        temp = (x - 1007);
        mGraph3.setPoint(temp * 100);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    SensorEventListener lightListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            Log.e("Light", sensorEvent.values[0]+"");
            mGraph2.setPoint(sensorEvent.values[0]/5);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
}
