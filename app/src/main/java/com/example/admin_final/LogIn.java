package com.example.admin_final;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LogIn extends AppCompatActivity {
    EditText user,pass;
    Button button;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        user=(EditText) findViewById(R.id.name);
        pass=(EditText) findViewById(R.id.pass);
        button=(Button) findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getText().toString().equals("admin") && pass.getText().toString().equals("12344321"))
                {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                }
                else {
                    Toast.makeText(LogIn.this, "Failed", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


}