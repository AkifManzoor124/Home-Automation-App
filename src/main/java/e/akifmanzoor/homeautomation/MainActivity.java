package e.akifmanzoor.homeautomation;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity{

    private static TextView humidDisplayText;
    private static TextView tempDisplayText;
    private static TextView photoDisplayText;

    private static fetchData process;
    private Button syncBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        humidDisplayText = (TextView) findViewById(R.id.humidDisplayText);
        tempDisplayText = (TextView) findViewById(R.id.tempDisplayText);
        photoDisplayText = (TextView) findViewById(R.id.ldsDisplayText);
        syncBtn = (Button) findViewById(R.id.syncBtn);

        syncBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(isOnline()){

                    process = new fetchData();
                    process.execute("http://192.168.0.28:8080/");
                    process = new fetchData();
                    process.execute("http://192.168.0.28:8080/tempHumidData");
                    process = new fetchData();
                    process.execute("http://192.168.0.28:8080/photoState");

                } else{
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

    public boolean isOnline(){
        ConnectivityManager cm;
        boolean isOnline = false;
        NetworkInfo netInfo;
        try{
            cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            netInfo = cm.getActiveNetworkInfo();
            isOnline = netInfo != null && netInfo.isAvailable() && netInfo.isConnected();
            return isOnline;

        } catch (Exception e){
            e.printStackTrace();
        }
        return isOnline;
    }

    public static void getData(JSONObject data){
        try{
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

        } catch (NullPointerException e){
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
}