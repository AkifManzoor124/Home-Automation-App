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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity{

    private static TextView humidDisplayText;
    private static TextView tempDisplayText;
    private static TextView photoDisplayText;
    private Button syncBtn;
    private Retrofit retrofit;
    private RestApi restApi;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        humidDisplayText = (TextView) findViewById(R.id.humidDisplayText);
        tempDisplayText = (TextView) findViewById(R.id.tempDisplayText);
        photoDisplayText = (TextView) findViewById(R.id.ldsDisplayText);
        syncBtn = (Button) findViewById(R.id.syncBtn);

        retrofit = new Retrofit.Builder()
                .baseUrl(RestApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        restApi = retrofit.create(RestApi.class);


        syncBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(isOnline()){
                    Call<TempSensor> tempData = restApi.getTempSensorData();
                    Call<photoSensor> photoData = restApi.getphotoSensorData();

                    tempData.enqueue(new Callback<TempSensor>() {
                        @Override
                        public void onResponse(Call<TempSensor> call, Response<TempSensor> response) {
                            TempSensor data = response.body();
                            if(data.getTempReading().contains("null") || data.getHumidReading().contains("null")){
                                tempDisplayText.setText("the temp/Humid Sensor");
                                humidDisplayText.setText("Please connect");
                            }else {
                                tempDisplayText.setText(data.getTempReading());
                                humidDisplayText.setText(data.getHumidReading());
                            }
                        }
                        @Override
                        public void onFailure(Call<TempSensor> call, Throwable t) {
                            t.printStackTrace();
                            //I should put this on my app that it failed
                        }
                    });

                    photoData.enqueue(new Callback<photoSensor>() {
                        @Override
                        public void onResponse(Call<photoSensor> call, Response<photoSensor> response) {
                            photoSensor data = response.body();
                            if(data.getPhotoReading().contains("null")){
                                photoDisplayText.setText("Please Connect");
                            }else {
                                photoDisplayText.setText(data.getPhotoReading());
                            }
                        }
                        @Override
                        public void onFailure(Call<photoSensor> call, Throwable t) {
                            t.printStackTrace();
                            //I should put this on my app that it failed
                        }
                    });

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

}