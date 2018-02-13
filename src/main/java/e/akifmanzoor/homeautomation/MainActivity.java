package e.akifmanzoor.homeautomation;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    public static TextView humidDisplayText;
    public static TextView tempDisplayText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        humidDisplayText = (TextView) findViewById(R.id.humidDisplayText);
        tempDisplayText = (TextView) findViewById(R.id.tempDisplayText);
        Button syncBtn = (Button) findViewById(R.id.syncBtn);

        syncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchData process = new fetchData();
                process.execute();
                notification();
            }
        });
    }

    public void notification(){
        // prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(this, MainActivity.class);
        // use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent =
                PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this,"1")
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle("Temperature/Humidity Sensor")
                        .setContentText("The Temperature is: " +
                                        "The Humidity is: ")
                        .setContentIntent(pIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, mBuilder.build());

    }

    public static void getTempSensorData(String[] parts){
        try{
            MainActivity.humidDisplayText.setText(parts[1]);
            MainActivity.tempDisplayText.setText(parts[0]);
        }catch (NullPointerException e){
            e.printStackTrace();

        }
    }
}
