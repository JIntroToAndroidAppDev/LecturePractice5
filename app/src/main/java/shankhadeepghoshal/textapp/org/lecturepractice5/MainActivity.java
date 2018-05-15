package shankhadeepghoshal.textapp.org.lecturepractice5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {

    private static final String webAddress = "http://127.0.0.17:8080/AndroidProgramming/Teaching/IT2015/Addition"; // make your own REST endpoint

    private EditText number1;
    private EditText number2;
    private Button calculateButton;
    private ExecutorService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.number1 = findViewById(R.id.TV1);
        this.number2 = findViewById(R.id.TV2);

        this.service = Executors.newSingleThreadExecutor();
        this.calculateButton = findViewById(R.id.button);
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */
    @Override
    protected void onResume() {
        super.onResume();
        calculateButton.setOnClickListener((view) -> {
            String number1 = this.number1.getText().toString();
            String number2 = this.number2.getText().toString();

            HashMap<String,String> requestBody = new HashMap<>();
            requestBody.put("NumberOne",number1);
            requestBody.put("NumberTwo",number2);

            HttpRequestMakingClass requestMakingClass = new HttpRequestMakingClass(requestBody,webAddress);
            try {
                String answer = this.service.submit(requestMakingClass).get();
                Toast.makeText(getApplicationContext(),answer,Toast.LENGTH_LONG).show();
            } catch (InterruptedException | ExecutionException e) {
                Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });
    }
}