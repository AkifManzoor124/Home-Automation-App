package e.akifmanzoor.homeautomation;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class fetchData extends AsyncTask<Void,Void,String[]> {

    String[] parts = {"Down","Server"};
    @Override
    protected String[] doInBackground(Void... voids){
        try{
            URL url = new URL("http://192.168.0.30/TempHumidity");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "", data = "";

            while(line != null){
                line = bufferedReader.readLine();
                data = data + line;
            }

            JSONObject object = new JSONObject(data);
            parts[0] = object.getString("Temperature");
            parts[1] = object.getString("Humidity");

            return parts;


        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String[] data){
        super.onPostExecute(data);
        MainActivity.getTempSensorData(parts);
    }
}
