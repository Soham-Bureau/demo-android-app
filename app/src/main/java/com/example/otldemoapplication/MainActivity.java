package com.example.otldemoapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;
import java.util.concurrent.Executors;

import id.bureau.auth.BureauAuth;

class AsyncUtils {
    public static void scheduleTask(Runnable task) {
        Executors.newSingleThreadExecutor().submit(task);
    }
}

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button test = findViewById(R.id.button);
        final MainActivity activity = this;
        test.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AsyncUtils.scheduleTask(new Runnable() {
                    @Override
                    public void run() {
                        final String correlationId = UUID.randomUUID().toString();
                        BureauAuth bureauAuth = new BureauAuth.Builder()
                                .mode(BureauAuth.Mode.Sandbox)
                                .clientId("16c47108-e5c9-4457-83db-cc3556061d6b")
                                .build();

                        Log.i("BureauAuth", correlationId);

                        String resultMessage = "";
                        TextView textView = findViewById(R.id.editTextNumber);

                        try {
                            BureauAuth.AuthenticationStatus authenticationStatus = bureauAuth.authenticate(getApplicationContext(), correlationId,Long.parseLong(  textView.getText().toString()));
                            resultMessage = correlationId + ": " + authenticationStatus.getMessage();
                            if (authenticationStatus == BureauAuth.AuthenticationStatus.Completed){
                                activity.runOnUiThread(() -> Toast.makeText(getApplicationContext(), correlationId + ": " + authenticationStatus.getMessage(), Toast.LENGTH_LONG).show());
                            }
                        } catch (RuntimeException e) {
                            resultMessage = correlationId + ": " + e.getMessage();
                        }

                        Log.i("BureauAuth", resultMessage);


                    }
                });
            }
        });
    }
}