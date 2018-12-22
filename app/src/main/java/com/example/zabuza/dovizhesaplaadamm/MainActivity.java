package com.example.zabuza.dovizhesaplaadamm;

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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {



    RadioGroup radioGroup;
    RadioButton radioDolar;
    RadioButton radioTl;
    RadioButton radioYen;
    RadioButton radioButton;

    TextView textView;
    TextView usdText;
/*
    TextView tryText;
    TextView cadText;
    TextView jpyText;
    TextView chfText;*/

    TextView sonuc;
    TextView sonuc2;
    EditText numberInput;
    Button cevirButton;

    int numberInt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioDolar = (RadioButton) findViewById(R.id.radioDolar);
        radioTl = (RadioButton) findViewById(R.id.radioTl);
        radioYen = (RadioButton) findViewById(R.id.radioYen);

        numberInput = (EditText) findViewById(R.id.numberInput);
        cevirButton = (Button) findViewById(R.id.cevir);

        sonuc = findViewById(R.id.sonucId);
        sonuc2 = findViewById(R.id.sonucId2);

        cevirButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                numberInt = Integer.valueOf(numberInput.getText().toString());
            }
        });

    }

//Seçilen radio buttonu ana buttona eşitler.
    public void checkButton(View v){

        int radioInt = radioGroup.getCheckedRadioButtonId();

        radioButton = findViewById(radioInt);

        Toast.makeText(this, "Selected Radio Button: "+  radioButton.getText(), Toast.LENGTH_SHORT).show();

    }



//İşlem kısmı
    public void getRates(View view){

        DownLoadData downLoadData = new DownLoadData();

        try{

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
                int yen = jsonObject1.getInt("JPY");
                double ang = jsonObject1.getDouble("ANG");

                if(radioButton == radioDolar){
                    float x = (float) usd;
                    float y = (float)ang;
                    float katsayi =  x / y;

                    float deger = numberInt / (float)usd;
                    float deger2 = numberInt / katsayi;
                    sonuc.setText(numberInt + " USD = " + deger+" EURO");
                    //sonuc.setText(numberInt + " TL = " + deger+" EURO");
                    sonuc2.setText(numberInt + " USD = " + deger2+" ANG");
                }
                else if(radioButton == radioTl) {

                    float x = (float) tl;
                    float y = (float) ang;
                    float katsayi = x / y;

                    float deger = numberInt / (float) tl;
                    float deger2 = numberInt / katsayi;
                    sonuc.setText(numberInt + " TL = " + deger + " EURO");
                    sonuc2.setText(numberInt + " TL = " + deger2 + " ANG");

                }

            }catch (Exception e){

            }
        }
    }

}
