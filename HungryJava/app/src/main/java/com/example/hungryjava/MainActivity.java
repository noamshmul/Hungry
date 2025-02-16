package com.example.hungryjava;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    Button btnTestConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loginscreen);
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
        



        btnTestConnection = findViewById(R.id.btn);
        btnTestConnection.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FridgeScreen.class);
                startActivity(intent);
                /*
                new AlertDialog.Builder(MainActivity.this)
                        .setView(R.layout.pop_up_add_item_screen)
                        .setCancelable(false)
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .show();

                 */
            }
        });
    }
}