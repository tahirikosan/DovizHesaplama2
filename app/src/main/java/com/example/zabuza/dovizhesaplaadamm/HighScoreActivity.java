package com.example.zabuza.dovizhesaplaadamm;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HighScoreActivity extends AppCompatActivity {

    float katsayiANG;
    float katsayiEU = 5;
    float katsayiUSD;
    private ListView lv_userName;
    private ListView lv_userMoney;
    private float totalMoney;
    private Button score;
    private RequestQueue mQueue;

    private ArrayList<String> user_name = new ArrayList<String>();
    private ArrayList<String> user_money = new ArrayList<String>();
    private float [] user_moneyTemporary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        lv_userName = (ListView) findViewById(R.id.lv_users);
        lv_userMoney = (ListView) findViewById(R.id.lv_userMoney);
        score = (Button)findViewById(R.id.score);

        mQueue = Volley.newRequestQueue(this);

        getRates();

        score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParse();
                ArrayAdapter<String> adapterName = new ArrayAdapter<String>(getApplicationContext(), R.layout.userslistview,user_name);
                lv_userName.setAdapter(adapterName);
                ArrayAdapter<String> adapterMoney = new ArrayAdapter<String>(getApplicationContext(), R.layout.userslistview,user_money);
                lv_userMoney.setAdapter(adapterMoney);

            }
        });

    }
//Highscorerequrest'ten verileri çekiyor.
    private void jsonParse(){
        String url = "http://10.192.4.189/highScoreRequest.php/";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("users");
                            user_moneyTemporary = new float[jsonArray.length()];
                            if(user_name.isEmpty()){
                                for(int i = 0; i < jsonArray.length(); i++){
                                    JSONObject user = jsonArray.getJSONObject(i);
                                    String name = user.getString("name");
                                    String tl = user.getString("TL");
                                    String eu = user.getString("EU");
                                    String dl = user.getString("DL");
                                    String ang = user.getString("ANG");
                                    user_name.add(name);
                                    //Toplam bakiye hsaplanıyor.
                                    totalMoney += Float.valueOf(tl) + (katsayiEU * Float.valueOf(eu))
                                            + (Float.valueOf(dl) / katsayiUSD)
                                            + (Float.valueOf(ang) / katsayiANG);

                                    user_moneyTemporary[i] = totalMoney;

                                    if(i == jsonArray.length() - 1){ //Eğer son elemana gelmişsek dizi sıralama çalışacak.
                                        for(int k = 0; k < user_moneyTemporary.length ; k++) {
                                            for (int j = user_moneyTemporary.length - 1; j > k; j--) {
                                                if (user_moneyTemporary[k] < user_moneyTemporary[j]) {

                                                    String tmpString = user_name.get(k).toString();
                                                    float tmp = user_moneyTemporary[k];   //Dizi elemanının kaybolmaması için tmp değişkeninde sakladım.

                                                    user_name.set(k, user_name.get(j).toString());
                                                    user_moneyTemporary[k] = user_moneyTemporary[j];

                                                    user_name.set(j, tmpString);
                                                    user_moneyTemporary[j] = tmp;
                                                }
                                            }
                                        }
                                        for(int a = 0; a <user_moneyTemporary.length ;a++){
                                            user_money.add(String.valueOf(user_moneyTemporary[a]));
                                            Toast.makeText(HighScoreActivity.this, "son hali"+String.valueOf( user_moneyTemporary[a]), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalMoney = 0;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HighScoreActivity.this, "Hata", Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(jsonObjectRequest);
    }


    //fixer.io sitesinden json verilerini çekiyor.
    public void getRates(){

        try{
            DownLoadData downLoadData = new DownLoadData();
            String url = "http://data.fixer.io/api/latest?access_key=28a5866d5d70f1072471bcad25e4fb5b&format=1";
            downLoadData.execute(url);

        }catch (Exception e){

        }
    }

    //Verileri arka planda indirir.
    private class DownLoadData extends AsyncTask<String,Void,String> {


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

                float u = (float) usd;
                float t = (float)tl;

                katsayiUSD =  u / t;

                katsayiEU = (float)tl;

                float a = (float) ang;
                katsayiANG = a / t;

                Toast.makeText(HighScoreActivity.this,String.valueOf(usd)+"Veri akışı tamam.Score görünteleme yapılabilir.", Toast.LENGTH_SHORT).show();
                //Toast.makeText(HighScoreActivity.this,String.valueOf(100/katsayiUSD), Toast.LENGTH_SHORT).show();

               /* Toast.makeText(HighScoreActivity.this,String.valueOf(ang), Toast.LENGTH_SHORT).show();
                Toast.makeText(HighScoreActivity.this,String.valueOf(100/katsayiANG), Toast.LENGTH_SHORT).show();

                Toast.makeText(HighScoreActivity.this,String.valueOf(tl), Toast.LENGTH_SHORT).show();
                Toast.makeText(HighScoreActivity.this,String.valueOf(100*katsayiEU), Toast.LENGTH_SHORT).show();*/

            }catch (Exception e){

            }
        }
    }
}
