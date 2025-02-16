package com.example.hungryjava;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    Button btnTestConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.homescreen);
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
        int code = 1;
        String id = "1";
        String pass = "3";
        HashMap<String, Object> k = new HashMap<>();
        k.put("Check", 2);
        HashMap<String, Object> res = Parser.parseRequest(code, id, pass, k);

        int code1 = (Integer)res.get("code");
        



        btnTestConnection = (Button) findViewById(R.id.testButton);
        btnTestConnection.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Example for intents                           Current Activity, Next Activity
                Intent fridge = new Intent(MainActivity.this, FridgeScreen.class);
                startActivity(fridge);//Starting activity
            }
        });
    }
}