package e.akifmanzoor.homeautomation;

/**
 * Created by Akif Manzoor on 2018-02-26.
 */

public class TempSensor {

    private String data;
    private String tempReading;
    private String humidReading;

    public TempSensor(String data, String tempReading, String humidReading) {
        this.data = data;
        this.tempReading = tempReading;
        this.humidReading = humidReading;
    }

    public String getData() {
        return data;
    }

    public String getTempReading() {
        return tempReading;
    }

    public String getHumidReading() {
        return humidReading;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setTempReading(String tempReading) {
        this.tempReading = tempReading;
    }

    public void setHumidReading(String humidReading) {
        this.humidReading = humidReading;
    }
}
