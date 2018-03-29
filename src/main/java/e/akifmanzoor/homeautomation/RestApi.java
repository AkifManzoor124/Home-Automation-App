package e.akifmanzoor.homeautomation;

import retrofit2.http.GET;
import retrofit2.Call;

/**
 * Created by Akif Manzoor on 2018-02-26.
 */

public interface RestApi {

    String ipAddress = "192.168.0.23";
    String port = "8080";
    String BASE_URL = "http://"+ipAddress+":"+port+"/sensors/";

    @GET("tempSensor")
    Call<TempSensor> getTempSensorData();

    @GET("photoSensor")
    Call<PhotoSensor> getPhotoSensorData();
}
