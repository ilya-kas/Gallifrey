package com.remotecontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] pins = new String[20];
        for (int i=0;i<20;i++)
            pins[i] = String.valueOf(i);

        Connector connector = new Connector(this);
        connector.start();

        Spinner pinSelector = findViewById(R.id.spinner);
        AtomicReference<ArrayAdapter<String>> adapter = new AtomicReference<>(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"0"}));
        adapter.get().setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pinSelector.setAdapter(adapter.get());
        Context context = this;

        RadioButton rb_a = findViewById(R.id.rb_a);
        rb_a.setOnClickListener(view -> {
            adapter.set(new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, (String[]) Arrays.copyOf(pins, 6)));
            adapter.get().setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            pinSelector.setAdapter(adapter.get());
        });

        RadioButton rb_d = findViewById(R.id.rb_d);
        rb_d.setOnClickListener(view -> {
            adapter.set(new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, pins));
            adapter.get().setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            pinSelector.setAdapter(adapter.get());
        });

        Button example = findViewById(R.id.send_button);
        example.setOnClickListener(view -> {
            byte pinType = 0;
            if (rb_a.isSelected())
                pinType = 1;
            else if (rb_d.isSelected())
                pinType = 2;
            if (pinType == 0)
                return;
            connector.sendMessage(new byte[]{pinType, (byte) pinSelector.getSelectedItemPosition()});
        });
    }
}