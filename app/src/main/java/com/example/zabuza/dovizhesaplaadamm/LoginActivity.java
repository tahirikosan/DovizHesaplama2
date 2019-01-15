package com.example.zabuza.dovizhesaplaadamm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    public static  final String EXTRA_TEXT = "Name";

    private EditText et_name, et_password;
    private Button loginButton;

    public String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_name = (EditText) findViewById(R.id.et_name);
        et_password = (EditText) findViewById(R.id.et_password);
        loginButton = (Button) findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_name = et_name.getText().toString();
                String user_password = et_password.getText().toString();

                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.1.45/login.php/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                RequestInterface request = retrofit.create(RequestInterface.class);
                Call<JsonResponse> call = request.login(user_name, user_password);
                call.enqueue(new Callback<JsonResponse>() {
                    @Override
                    public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                        if(response.code() == 200){
                            JsonResponse response1 = response.body();
                            name = response1.getResponse().toString();
                            Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
                            Intent intent  = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra(Intent.EXTRA_TEXT, name);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(), String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}
