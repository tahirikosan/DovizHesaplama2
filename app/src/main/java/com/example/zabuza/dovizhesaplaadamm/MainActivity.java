package com.example.zabuza.dovizhesaplaadamm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String INT = "tlMiktar";
    private static final String INT1 = "angMiktar";
    private static final String INT2 = "dolarMiktar";
    private static final String INT3 = "euroMiktar";

    private RadioGroup radioGroup;
    private RadioButton radioDolar;
    private RadioButton radioEuro;
    private RadioButton radioAng;
    private RadioButton radioButton;

    private TextView tv_highScore;
    private TextView welcomeMsg;
    private TextView sonuc;
    private TextView sonuc2;
    private TextView tlText;
    private TextView dolarText;
    private TextView angText;
    private TextView euroText;

    private EditText numberInput;

    private Button satinAlButton;
    private Button bozdurButton;

    private String user_name;
    private String user_amount;
    private String user_TL;
    private String user_EU;
    private String user_DL;
    private String user_ANG;
    private String secilenPara;

    private float numberInt;
    private float tlMiktar = 0;
    private float dolarMiktar = 0;
    private float angMiktar = 0;
    private float euroMiktar = 0;
    private float katsayi;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        user_name = intent.getStringExtra(Intent.EXTRA_TEXT);
        user_TL = intent.getStringExtra("TL");
        user_EU = intent.getStringExtra("EURO");
        user_DL = intent.getStringExtra("DOLAR");
        user_ANG = intent.getStringExtra("ANG");

        tlMiktar = Float.parseFloat(user_TL);
        euroMiktar = Float.parseFloat(user_EU);
        dolarMiktar = Float.parseFloat(user_DL);
        angMiktar = Float.parseFloat(user_ANG);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioDolar = (RadioButton) findViewById(R.id.radioDolar);
        radioEuro = (RadioButton) findViewById(R.id.radioEuro);
        radioAng = (RadioButton) findViewById(R.id.radioAng);

        numberInput = (EditText) findViewById(R.id.numberInput);
       // numberInput.setText("0"); // Değer girilecek kısmın default değeri.Bu olmazsa program çöker.

        satinAlButton = (Button) findViewById(R.id.satinAl);
        bozdurButton = (Button) findViewById(R.id.bozdur);

        welcomeMsg = (TextView) findViewById(R.id.welcomeMsg);
        tlText = (TextView) findViewById(R.id.tlText);
        dolarText = (TextView)findViewById(R.id.dolarText);
        angText = (TextView) findViewById(R.id.angText);
        euroText = (TextView) findViewById(R.id.euroText);
        sonuc = (TextView) findViewById(R.id.sonucId);
        sonuc2 = (TextView) findViewById(R.id.sonucId2);
        tv_highScore = (TextView)findViewById(R.id.tv_highscore);

        welcomeMsg.setText("Welcome " + user_name);

        tv_highScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HighScoreActivity.class);
                startActivity(intent);
            }
        });

        satinAlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                satinAl(secilenPara, katsayi);
            }
        });

        bozdurButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bozdur(secilenPara, katsayi);
            }
        });

        //loadData();
        updateViews();
    }

    public void saveData(){

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.192.4.189/update.php/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JsonResponse> call = request.update(user_name, tlMiktar, euroMiktar, dolarMiktar, angMiktar);
        call.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if(response.code() == 200){
                    JsonResponse response1 = response.body();
                    Toast.makeText(getApplicationContext(),response1.getResponse().toString(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {
               // Toast.makeText(getApplicationContext(), "Kaydedilmedi", Toast.LENGTH_SHORT).show();
                //Burada bir sıkıntı var.Veriler güncelleniyor fakat response alınamıyor!
            }
        });
    }

/*
    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        tlMiktar = sharedPreferences.getFloat(INT,tlMiktar);
        angMiktar = sharedPreferences.getFloat(INT1,angMiktar);
        dolarMiktar = sharedPreferences.getFloat(INT2, dolarMiktar);
        euroMiktar = sharedPreferences.getFloat(INT3, euroMiktar);
    }*/


    public void updateViews(){
        tlText.setText("TürkL = " + tlMiktar);
        dolarText.setText("Dolar = " + dolarMiktar);
        angText.setText("ANG = " + angMiktar);
        euroText.setText("Euro = " + euroMiktar);
    }

//Seçilen radio buttonu ana buttona eşitler.
    public void checkButton(View v){

        int radioInt = radioGroup.getCheckedRadioButtonId();

        radioButton = findViewById(radioInt);

        Toast.makeText(this, "Selected Radio Button: "+  radioButton.getText(), Toast.LENGTH_SHORT).show();

    }

    public void satinAl(String secilenPara, float katsayi){

        if(secilenPara == "ANG"){
            if(tlMiktar >= (numberInt / katsayi)) {
                tlMiktar -= numberInt / katsayi;

                angMiktar += numberInt;

                Toast.makeText(this, "Satın alma işlemi başarılı!", Toast.LENGTH_SHORT).show();

                tlText.setText(tlMiktar + "  " + "TL");
                angText.setText(angMiktar + "  " + "ANG");
            }else{
                Toast.makeText(this, "Yeterli TL yok!", Toast.LENGTH_SHORT).show();
            }
        }
        else if(secilenPara == "USD"){
            if(tlMiktar >= ( numberInt / katsayi)){
                tlMiktar -= numberInt / katsayi;
                dolarMiktar += numberInt;

                Toast.makeText( this,"Satin alma işlemi başarılı!", Toast.LENGTH_SHORT).show();

                tlText.setText(tlMiktar + "  " + "TL");
                dolarText.setText(dolarMiktar + "  " + "$");
            }else{
                Toast.makeText(this, "Yeterli TL yok!", Toast.LENGTH_SHORT).show();
            }
        }
        else if(secilenPara == "EURO"){
            if(tlMiktar >= (numberInt * katsayi)){
                tlMiktar -=  numberInt * katsayi;
                euroMiktar += numberInt;

                Toast.makeText( this,"Satin alma işlemi başarılı!", Toast.LENGTH_SHORT).show();

                tlText.setText(tlMiktar + "  " + "TL");
                euroText.setText(euroMiktar + "  " + "£");
            }else{
                Toast.makeText(this, "Yeterli TL yok!", Toast.LENGTH_SHORT).show();
            }
        }
        saveData();
    }

    public void bozdur(String secilenPara, float katsayi){

        if(secilenPara == "ANG"){
            if(angMiktar >= numberInt){
                tlMiktar += numberInt / katsayi;
                angMiktar -= numberInt;

                Toast.makeText( this,"Satin alma işlemi başarılı!", Toast.LENGTH_SHORT).show();

                tlText.setText(tlMiktar + "  " + "TL");
                angText.setText(angMiktar + "  " + "ANG");
            }else{
                Toast.makeText(this, "Yeterli ANG yok!", Toast.LENGTH_SHORT).show();
            }
        }
        else if(secilenPara == "USD"){
            if(dolarMiktar >= numberInt){
                tlMiktar += numberInt / katsayi;
                dolarMiktar -= numberInt;

                Toast.makeText( this,"Satin alma işlemi başarılı!" , Toast.LENGTH_SHORT).show();

                tlText.setText(tlMiktar + "  " + "TL");
                dolarText.setText(dolarMiktar + "  " + "$");
            }else{
                Toast.makeText(this, "Yeterli Dolar yok!", Toast.LENGTH_SHORT).show();
            }
        }
        else if(secilenPara == "EURO"){
            if(euroMiktar >= numberInt){
                tlMiktar +=  numberInt * katsayi;
                euroMiktar -= numberInt;

                Toast.makeText( this,"Satin alma işlemi başarılı!", Toast.LENGTH_SHORT).show();

                tlText.setText(tlMiktar + "  " + "TL");
                euroText.setText(euroMiktar + "  " + "£");
            }else{
                Toast.makeText(this, "Yeterli Euro yok!", Toast.LENGTH_SHORT).show();
            }
        }
        saveData();
    }

//İşlem kısmı
    public void getRates(View view){

            try{
                numberInt = Integer.valueOf(numberInput.getText().toString());
                DownLoadData downLoadData = new DownLoadData();
                String url = "http://data.fixer.io/api/latest?access_key=28a5866d5d70f1072471bcad25e4fb5b&format=1";
                downLoadData.execute(url);

            }catch (Exception e){

            }
    }

//Verileri arka planda indirir.
    private class DownLoadData extends AsyncTask<String,Void,String>{


        @Override
        protected String doInBackground(String... strings) {

            String result ="";
            URL url;
            HttpURLConnection httpURLConnection;
            try{
                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while(data > 0){

                    char character = (char)data;
                    result += character;

                    data = inputStreamReader.read();
                }

                return result;
            }catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //System.out.println("alınan veri" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);

                String base = jsonObject.getString("base");
                //System.out.println("base: "+base);

                String rates = jsonObject.getString("rates");
                //System.out.println("rates: "+ rates);


                JSONObject jsonObject1 = new JSONObject(rates);

                double tl = jsonObject1.getDouble("TRY");
                int usd = jsonObject1.getInt("USD");
                double ang = jsonObject1.getDouble("ANG");

                if(radioButton == radioDolar){

                    secilenPara = "USD";
                    float x = (float) usd;
                    float y = (float)tl;
                    katsayi =  x / y;

                    float deger = numberInt / (float)usd;
                    float deger2 = numberInt / katsayi;
                    /*sonuc.setText(numberInt + " USD = " + deger+" EURO");
                    sonuc2.setText(numberInt + " USD = " + deger2+" TL");*/
                }
                else if(radioButton == radioEuro) {

                    secilenPara = "EURO";
                    katsayi = (float)tl;

                    float deger = numberInt / (float) tl;
                    float deger2 = numberInt / katsayi;
                   // sonuc.setText(numberInt + " TL = " + deger + " EURO");

                }else if(radioButton == radioAng) {

                    secilenPara = "ANG";
                    float x = (float) ang;
                    float y = (float) tl;
                    katsayi = x / y;

                    float deger = numberInt / (float)ang;
                    float deger2 = numberInt / katsayi;
                    /*sonuc.setText(numberInt + " ANG = " + deger+" EURO");
                    sonuc2.setText(numberInt + " ANG = " + deger2+" TL");*/
                }

            }catch (Exception e){

            }
        }
    }

}
