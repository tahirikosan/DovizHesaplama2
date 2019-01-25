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

    public static final String EXTRA_TEXT = "Name";
    public static final String TL = "TL";
    public static final String EU = "EURO";
    public static final String DOLAR = "DOLAR";
    public static final String ANG = "ANG";

    private EditText et_name, et_password;
    private Button loginButton;

    private String name;
    private String user_TL;
    private String user_EU;
    private String user_DOLAR;
    private String user_ANG;

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

                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.192.4.189/login.php/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                RequestInterface request = retrofit.create(RequestInterface.class);
                Call<JsonResponse> call = request.login(user_name, user_password);
                call.enqueue(new Callback<JsonResponse>() {
                    @Override
                    public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {

                        if(response.code() == 200){
                            JsonResponse responseName = response.body();
                            JsonResponse responseTL = response.body();
                            JsonResponse responseEU = response.body();
                            JsonResponse responseDL = response.body();
                            JsonResponse responseANG = response.body();

                            name = responseName.getResponse().toString();
                            user_TL = responseTL.getResponse1().toString();
                            user_EU = responseEU.getResponse2().toString();
                            user_DOLAR = responseDL.getResponse3().toString();
                            user_ANG = responseANG.getResponse4().toString();

                           /* Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), user_TL, Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), user_EU, Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), user_DOLAR, Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), user_ANG, Toast.LENGTH_SHORT).show();*/

                            Intent intent  = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra(Intent.EXTRA_TEXT, name);
                            intent.putExtra(TL, user_TL);
                            intent.putExtra(EU, user_EU);
                            intent.putExtra(DOLAR, user_DOLAR);
                            intent.putExtra(ANG, user_ANG);
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
