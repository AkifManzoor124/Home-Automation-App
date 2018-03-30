package e.akifmanzoor.homeautomation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
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
    private Button desyncBtn;

    private Retrofit retrofit;
    private RestApi restApi;

    private TempSensor tempHumidData;
    private PhotoSensor photoSensor;


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
        desyncBtn = (Button) findViewById(R.id.desyncBtn);

        tempHumidData = new TempSensor("null","null","null");
        photoSensor = new PhotoSensor("null","null");


        //Dialog for Address Check
        AlertDialog.Builder checkAddressDialog = new AlertDialog.Builder(this);
        checkAddressDialog.setTitle("Check Address");
        checkAddressDialog.setMessage("Is this Address Correct: " + RestApi.ipAddress + "?");

        checkAddressDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        checkAddressDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        checkAddressDialog.create().show();

        //Definition of Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(RestApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        restApi = retrofit.create(RestApi.class);

        final Runnable dataRun = new Runnable() {
            @Override
            public void run() {
                try{
                    while(true) {
                        Thread.sleep(1000);
                        getSensorData(restApi.getTempSensorData(), restApi.getPhotoSensorData());
                    }
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        Thread getDataThread = new Thread(dataRun);
        getDataThread.start();

        //test code for syncService
        syncBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (isOnline()){
                    changeText();
                }else{
                    createToast("Please Connect to the Internet", Toast.LENGTH_SHORT);
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
            }
        });

        desyncBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                humidDisplayText.setText("-");
                tempDisplayText.setText("-");
                tempHumidStatusText.setText("-");
                photoDisplayText.setText("-");
                photoStatusDisplayText.setText("-");
            }
        });
    }

    private void changeText(){
        if(tempHumidData.getTempReading().contains("null") || tempHumidData.getHumidReading().contains("null")){
            humidDisplayText.setText("Please Connect the Sensor");
            tempDisplayText.setText("Please Connect the Sensor");
            tempHumidStatusText.setText("Offline");
        }else {
            tempDisplayText.setText(tempHumidData.getTempReading());
            humidDisplayText.setText(tempHumidData.getHumidReading());
            tempHumidStatusText.setText("Online");
        }

        if(photoSensor.getPhotoReading().contains("null")){
            photoDisplayText.setText("Please Connect the Sensor");
            photoStatusDisplayText.setText("Offline");
        }else{
            photoDisplayText.setText(photoSensor.getPhotoReading());
            photoStatusDisplayText.setText("Online");
        }
    }

    private void getSensorData(Call<TempSensor> tempData, Call<PhotoSensor> photoData){

        tempData.enqueue(new Callback<TempSensor>() {
            @Override
            public void onResponse(Call<TempSensor> call, Response<TempSensor> response) {
                tempHumidData.setData(response.body().getData());
                tempHumidData.setTempReading(response.body().getTempReading());
                tempHumidData.setHumidReading(response.body().getHumidReading());

            }
            @Override
            public void onFailure(Call<TempSensor> call, Throwable t) {
                t.printStackTrace();
                createToast("Server is Down", Toast.LENGTH_SHORT);
            }
        });

        photoData.enqueue(new Callback<PhotoSensor>() {
            @Override
            public void onResponse(Call<PhotoSensor> call, Response<PhotoSensor> response) {
                photoSensor.setData(response.body().getData());
                photoSensor.setPhotoReading(response.body().getPhotoReading());
            }
            @Override
            public void onFailure(Call<PhotoSensor> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private boolean isOnline(){
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

    private void createToast(CharSequence text, int length){
        Context context = getApplicationContext();
        CharSequence message = text;
        int duration = length;
        Toast toast = Toast.makeText(context,message, duration);
        toast.show();
    }
}
