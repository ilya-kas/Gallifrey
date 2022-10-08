package com.remotecontroller.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.remotecontroller.R;
import com.remotecontroller.connection.Connector;


public class MainActivity extends AppCompatActivity {
    private final Connector connector = Connector.INSTANCE;
    static MainActivity finder;
    private static Resources r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        r = getResources();
        finder = this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ImageButton settings = findViewById(R.id.b_settings);
        settings.setOnClickListener(v -> {
            Dialog updateInfo = new Dialog(this);         //диалог установки данных о подключении к серверу
            updateInfo.setContentView(R.layout.connection_dialog);
            EditText et_ip = updateInfo.findViewById(R.id.et_ip);
            EditText et_port = updateInfo.findViewById(R.id.et_port);

            et_ip.setText(connector.getIp());
            et_port.setText(""+connector.getPort());

            updateInfo.show();

            Button save = updateInfo.findViewById(R.id.b_save);
            save.setOnClickListener(v1 -> {
                connector.setIp(et_ip.getText().toString().trim());
                connector.setPort(Integer.parseInt(et_port.getText().toString().trim()));

                updateInfo.dismiss();
            });
        });

        Spinner typeSelector = findViewById(R.id.spinner_board);  //настройка выпадающего списка выбора режима
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Arduino Uno"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSelector.setAdapter(adapter);
        typeSelector.setSelection(0);

        Button connect = findViewById(R.id.b_connect);
        connect.setOnClickListener(v -> {
            Connector.INSTANCE.reconnect();
        });

        //connector.start();
    }

    public static void log(String msg){
        Log.d("prog",msg);
    }

    public static void setConnected(boolean connected){
        ImageView led = finder.findViewById(R.id.im_connected);
        if (connected)
            led.setImageResource(R.drawable.green_dot);
        else
            led.setImageResource(R.drawable.red_dot);
    }

    public static Resources getR(){
        return r;
    }
}