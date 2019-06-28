package com.example.network2;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
    Отправка данных в HTTP  запросе методом GET
 передаются в адресной строке в виде:
 parameter_name1=value1&parameter_name2=value2&parameter_nameN=valueN&

 Эти параметры добавляются к адресной строке после "?"
    http://someadress.com/somescript?parameter_name1=value1&parameter_name2=value2&parameter_nameN=valueN
Пример
 HttpURLConnection cn = (HttpURLConnection) n.openConnection(new URL("http://someadress.com/somescript?parameter_name1=value1&parameter_name2=value2&parameter_nameN=valueN"));


 Отправка данных в HTTP запросе методом POST
 ------------------------------------------------------
 может передавать запрос больше 500 байт

 передаются в адресной строке в виде:
 parameter_name1=value1&parameter_name2=value2&parameter_nameN=valueN&

 Эти параметры добавляются к адресной строке после "?"
 http://someadress.com/somescript?parameter_name1=value1&parameter_name2=value2&parameter_nameN=valueN
 Пример
 HttpURLConnection cn = (HttpURLConnection) n.openConnection(new URL("http://someadress.com/somescript?parameter_name1=value1&parameter_name2=value2&parameter_nameN=valueN"));
 cn.setDoInput(true);
 cn.setDoOutput(true); //будем писать байты в тело HTTP запроса

 OutputStream out = cn.getOutputStream();
 String str = "parameter_name1=value1&parameter_name2=value2";
 byte[] a = str.getBytes("UTF8");
 out.write(a, 0, a.length);

 */


@TargetApi(23)
public class MainActivity extends AppCompatActivity {
    WebView webView;
    TextView tvContent;
    EditText etRequest;
    RadioGroup rgRequestOptions;
    private static ThrRequest thrRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
requestPermissions(new String[]{
            Manifest.permission.INTERNET,Manifest.permission.ACCESS_NETWORK_STATE
        },0);
        setContentView(R.layout.activity_main);
        //------Find widgets------------
        webView = findViewById(R.id.webView);
        tvContent = findViewById(R.id.tvContent);
        etRequest = findViewById(R.id.etURL);
        rgRequestOptions =findViewById(R.id.rgRequestOptions);

        Button btnRequest = findViewById(R.id.btnSend);
        //*********************************
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thrRequest = new ThrRequest();
                thrRequest.setWidgets(etRequest, tvContent, MainActivity.this);
                thrRequest.start();
            }
        });


        if(thrRequest != null) {
            thrRequest.setWidgets(etRequest, tvContent, MainActivity.this);
        }
    }

    class ThrRequest extends Thread
    {
        private TextView tvContent;
        private Activity activity;
        private EditText etRequest;

        public void setWidgets(EditText request, TextView tvContent, Activity activity)
        {
            this.tvContent = tvContent;
            this.etRequest = request;
            this.activity = activity;
        }

        @Override
        public void run() {
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            try {

                //-----Получение ссылки для активного подключения
                ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
                Network net = null;
                //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                net = cm.getActiveNetwork();
                //}
                //-----Получение объекта HttpURLConnection и отправка запроса
//                    HttpsURLConnection connection = (HttpsURLConnection) net.openConnection(new URL("https://rozetka.com.ua"));
//                HttpsURLConnection connection = (HttpsURLConnection) net.openConnection(new URL("https://itstep.zp.ua"));
                HttpURLConnection connection = (HttpURLConnection) net.openConnection(new URL("http://192.168.13.129:4000"));

                connection.setDoInput(true); // будем читать
                connection.setDoOutput(true); // будем писать

                connection.connect();

//                ----запись данных в POST в тело HTTP запроса
                OutputStream outputStream = connection.getOutputStream();
                String text = "string=" + etRequest.getText().toString();
                //protocol : command=upper&string=hell+world
                String query = "command=";
                //----формирование запроса--------
                switch (rgRequestOptions.getCheckedRadioButtonId())
                {
                    case R.id.rbUpper:
                        query += "upper";
                        break;
                    case R.id.rbLower:
                        query += "lower";
                        break;
                    case R.id.rbMirror:
                        query += "mirror";
                        break;
                    case R.id.rbCount:
                        query += "cnt";
                        break;

                }
                 query += "&" + text;

                byte[] queryBytes = query.getBytes();

                //---------отправка запроса--------------
                outputStream.write(queryBytes, 0, queryBytes.length);


                //-----Чтение полученного от сервера ответа
                Log.d("TAG" , "RESPONSE CODE: "+connection.getResponseCode());
                InputStream inputStream  = connection.getInputStream();
                ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
                byte[]b = new byte[1024];
                while (true)
                {
                    Log.d("Available : ", inputStream.available() + " b");
                    int cnt = inputStream.read(b,0,b.length);
//                        Log.d("Available : ", inputStream.available() + " b");
                    Log.d("Cnt : ", cnt + " b");
                    if(cnt == -1)break;
                    BAOS.write(b,0,cnt);
                }
                byte[] a = BAOS.toByteArray();
                Log.d("content size : ", a.length + " b");
                BAOS.reset();
                final String content = new String(a,0 ,a.length,"UTF8");
                Log.d("content", content);
                //------ отображае ответ
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//
                        webView.getSettings().setJavaScriptEnabled(true);
                        webView.loadDataWithBaseURL("", content, "text/html", "utf-8", "");
                    }
                });
                //------закрываем соединение
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("ERROR", e.getMessage());

            }
        }
    }
//    }
}
