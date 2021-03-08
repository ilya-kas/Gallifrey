package com.remotecontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Connector connector = new Connector(this);
        connector.start();

        Button example = findViewById(R.id.button);
        example.setOnClickListener(view -> {
            connector.sendMessage(new byte[]{10,9,8,7,6,5,4,3,2,1});
        });
    }
}