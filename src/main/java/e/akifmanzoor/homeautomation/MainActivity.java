package e.akifmanzoor.homeautomation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button syncBtn = (Button) findViewById(R.id.syncBtn);
        syncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView humidDisplayText = (TextView) findViewById(R.id.humidDisplayText);
                humidDisplayText.setText("Not yet implemented");
            }
        });
    }
}
