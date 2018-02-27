package e.akifmanzoor.homeautomation;

/**
 * Created by Akif Manzoor on 2018-02-27.
 */

public class photoSensor {

    private String data;
    private String photoReading;

    public photoSensor(String data, String photoReading) {
        this.data = data;
        this.photoReading = photoReading;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPhotoReading() {
        return photoReading;
    }

    public void setPhotoReading(String photoReading) {
        this.photoReading = photoReading;
    }
}
