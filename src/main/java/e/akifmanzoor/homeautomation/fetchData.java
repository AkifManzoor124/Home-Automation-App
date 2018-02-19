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

public class fetchData extends AsyncTask<String,Void,JSONObject> {
    String urlStatus = "";
    @Override
    protected JSONObject doInBackground(String... url){
        try{
            urlStatus = url[0];

            URL connect = new URL(urlStatus);
            HttpURLConnection httpURLConnection = (HttpURLConnection) connect.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "", data = "";

            while(line != null){
                line = bufferedReader.readLine();
                data = data + line;
            }


            JSONObject object = new JSONObject(data);

            return object;

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
    protected void onPostExecute(JSONObject data){
        super.onPostExecute(data);
        MainActivity.getData(data);
    }
}
