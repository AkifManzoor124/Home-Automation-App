package e.akifmanzoor.homeautomation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
**/

public class MainActivity extends AppCompatActivity {

    public static TextView humidDisplayText;
    public static TextView tempDisplayText;
    public static TextView photoDisplayText;
    public static Button syncBtn;

    public static fetchData process;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        humidDisplayText = (TextView) findViewById(R.id.humidDisplayText);
        tempDisplayText = (TextView) findViewById(R.id.tempDisplayText);
        photoDisplayText = (TextView) findViewById(R.id.ldsDisplayText);

        syncBtn = (Button) findViewById(R.id.syncBtn);

        syncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchData process = new fetchData();
                process.execute("http://192.168.0.24:8080/tempHumidData");
                //Cannot do this, need to wait for the above process
                //process.execute("http://192.168.0.30/photoState");
            }
        });
    }

    /****
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
    ****/

    public static void getData(JSONObject data){
        try{
            String temp = data.getString("Temperature");
            String humid = data.getString("Humidity");
            //String status = data.getString("status");
            //MainActivity.photoDisplayText.setText(status);
            MainActivity.humidDisplayText.setText(temp);
            MainActivity.tempDisplayText.setText(humid);
        }catch (NullPointerException e){
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
