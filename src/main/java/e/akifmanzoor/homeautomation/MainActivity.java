package e.akifmanzoor.homeautomation;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
**/

public class MainActivity extends AppCompatActivity {

    private static TextView humidDisplayText;
    private static TextView tempDisplayText;
    private static TextView photoDisplayText;
    private static Button syncBtn;
    private static fetchData process;
    private static boolean tempHumidStatus;
    private static boolean photoresistorStatus;
    private static Context context;

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

                if(isOnline()){
                    process = new fetchData();
                    process.execute("http://192.168.0.143:8080/");

                    if(tempHumidStatus == true){
                        process = new fetchData();
                        process.execute("http://192.168.0.143:8080/tempHumidData");
                    }
                    if(photoresistorStatus == true){
                        process = new fetchData();
                        process.execute("http://192.168.0.143:8080/photoState");
                    }
                }else{
                    Context context = getApplicationContext();
                    CharSequence text = "Please Connect to the Internet";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
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

    public static boolean isOnline(){
        ConnectivityManager cm;
        boolean isOnline = false;
        NetworkInfo netInfo;
        try{
            cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            netInfo = cm.getActiveNetworkInfo();
            isOnline = netInfo != null && netInfo.isAvailable() && netInfo.isConnected();
            return isOnline;

        }catch (Exception e){
            e.printStackTrace();
        }
        return isOnline;
    }

    public static void getData(JSONObject data){
        try{
            if(data.getString("tempHumidSensor").equals("\"deactivated\"")) {
                tempHumidStatus = false;
                tempDisplayText.setText("Please Start Temperature Sensor");
                humidDisplayText.setText("Please Start Humidity Sensor");
            }
            if(data.getString("photoSensor").equals("\"deactivated\"")){
                photoresistorStatus = false;
                photoDisplayText.setText("Please Start PhotoResistor Sensor");
            }

            if (data.getString("data").contains("photoResistor")) {
                String status = data.getString("photoStatus");
                photoDisplayText.setText(status);
            }
            if (data.getString("data").contains("tempHumid")) {
                String temp = data.getString("Temperature");
                String humid = data.getString("Humidity");
                humidDisplayText.setText(humid);
                tempDisplayText.setText(temp);
            }

        }catch (NullPointerException e){
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
