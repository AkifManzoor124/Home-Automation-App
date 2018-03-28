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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.util.Log;

public class MainActivity extends AppCompatActivity {

    //variable definition
    private TextView humidDisplayText;
    private TextView tempDisplayText;
    private TextView photoDisplayText;
    private TextView tempHumidStatusText;
    private TextView photoStatusDisplayText;

    private Button syncBtn;
    private Retrofit retrofit;
    private RestApi restApi;

    private boolean sync = true;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Definition of texts
        humidDisplayText = (TextView) findViewById(R.id.humidDisplayText);
        tempDisplayText = (TextView) findViewById(R.id.tempDisplayText);
        photoDisplayText = (TextView) findViewById(R.id.ldsDisplayText);
        tempHumidStatusText = (TextView) findViewById(R.id.tempHumidDisplayText);
        photoStatusDisplayText = (TextView) findViewById(R.id.PhotoResistorDisplayText);
        syncBtn = (Button) findViewById(R.id.syncBtn);

        //Definition of Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(RestApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        restApi = retrofit.create(RestApi.class);

        //test code for syncService
        syncBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (isOnline()){
                    Runnable getDataRunnable = new Runnable() {
                        @Override
                        public void run() {
                            while(sync){
                                try {
                                    Thread.sleep(1000);
                                    getTempHumidData();
                                    getPhotoresistorData();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    };

                    Thread getDataThread = new Thread(getDataRunnable);
                    getDataThread.start();
                }else{
                    createToast("Please Connect to the Internet", Toast.LENGTH_SHORT);
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
            }
        });
    }


    public void getTempHumidData(){
        Call<TempSensor> tempData = restApi.getTempSensorData();

        tempData.enqueue(new Callback<TempSensor>() {
            @Override
            public void onResponse(Call<TempSensor> call, Response<TempSensor> response) {
                TempSensor data = response.body();

                if(data.getTempReading().contains("null") || data.getHumidReading().contains("null")){
                    humidDisplayText.setText("Please Connect Sensor");
                    tempDisplayText.setText("Please Connect Sensor");
                    tempHumidStatusText.setText("Deactive");
                }else {
                    tempDisplayText.setText(data.getTempReading());
                    humidDisplayText.setText(data.getHumidReading());
                    tempHumidStatusText.setText("Active");
                }
            }
            @Override
            public void onFailure(Call<TempSensor> call, Throwable t) {
                t.printStackTrace();
                createToast("Server is Down", Toast.LENGTH_SHORT);
            }
        });
    }

    public void getPhotoresistorData(){
        Call<PhotoSensor> photoData = restApi.getPhotoSensorData();

        photoData.enqueue(new Callback<PhotoSensor>() {
            @Override
            public void onResponse(Call<PhotoSensor> call, Response<PhotoSensor> response) {
                PhotoSensor data = response.body();


                if(data.getPhotoReading().contains("null")){
                    photoDisplayText.setText("Please Connect Sensor");
                    photoStatusDisplayText.setText("Deactive");
                }else{
                    photoDisplayText.setText(data.getPhotoReading());
                    photoStatusDisplayText.setText("Active");
                }


            }
            @Override
            public void onFailure(Call<PhotoSensor> call, Throwable t) {
                t.printStackTrace();
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
            isOnline = netInfo != null && netInfo.isAvailable() && netInfo.isConnected() && !netInfo.getTypeName().contains("MOBILE");
            return isOnline;

        } catch (Exception e){
            e.printStackTrace();
        }
        return isOnline;
    }

    public void createToast(CharSequence text, int length){
        Context context = getApplicationContext();
        CharSequence message = text;
        int duration = length;
        Toast toast = Toast.makeText(context,message, duration);
        toast.show();
    }
}