package com.example.networking;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


/**
 Сетевое программирование для мобильных устройств Android
 --------------------------------------------------------

 Для выполнения сетевых операций в манивест нужно внести разрешения
 <uses-permission android:name="android.permission.INTERNET"/>
 <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>

 Для получения информации о состоянии сети используется класс android.net.ConnectivityManager

 java.lang.Object
    |
    +---android.net.ConnectivityManager

 для получения ссылки на android.net.ConnectivityManager
 метод класса android.Context:
    T getSystemService(Class<T> ServiceClass);
 пример:
 ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);


 Методы класса ConnectivityManager:

 void addDefaultNetworkActiveListener(ConnectivityManager.OnNetworkActiveListener l) - добавляет обработчик событий котороый будеит получать уведомленя о том
 что сеть по умолчанию доступна к обмену данными, готовность сети означает что настало удобное время для обмена данными по сети,
 метод доступен с api 23

 Network getActiveNetwork(); - возвращает объект Network т.е. объект сетевого подключения который соответствует текущей активной сети передачи данных
 т.е. сети которая будет использоваться для исходящих сетевых соеденений, если нет доступных соеденений то метод вернет null

 NetworkInfo getActiveNetworkInfo() - возвращает информацию о текущем активном сетевом подключении если в системе нет активного подключения метод верент null

 Network[] getAllNetworks() - возвращает массив объектов Network т.е. объектов сетевых подключений которые доступны в системе

 NetworkInfo getNetworkInfo(Network network) - возвращает объект с информацией о сетевом подключении для конкретного объекта Network


 Получение информации о сетевом подключении Класс android.net.NetworkInfo
 NetworkInfo.State getState() - возвращает текущее состояние сети (CONNECTING, CONNECTED, DISCONNECTING, DISCONNECTED)
 int getType() - код типа сети
 String getTypeName()

 boolean isAvailable() - доступен
 boolean isConnected() - соеденен

 String getReason() - если соеденение не удалось то этот возвращает строку с информацией почему

 Пример:

 ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
 if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
    Network N= cm.getActiveNetwork();
 }
 NetworkInfo NI = cm.getActiveNetworkInfo();


 Сетевое подключение класс android.net.Network
 ---------------------------------------------
 предоставляе сетевое подключение по которому можно передавать данные

 URLConnection openConnection(URL url) - создает соединение в виде объекта производного класса URLConnection
 URLConnection openConnection(URL url, Proxy proxy)- создает соединение в виде объекта производного класса URLConnection
 позволяет указать параметры для прокси сервера
 InetAddress[] getAllByName(String host) - возвращает массив объектов ip адресс каждый из которых это ip социированный с указаным именем хоста
 InetAddress getByName(String host) -получение ip по имени хоста

 URLConnection
 -------------
 InputStream getInputStream()
 OutputStream getOutputStream()

 void setDoInput(boolean) - true если собираемся читать
 void setDoOutput(boolean)- true если собираемся писать

 */

@TargetApi(23)
public class MainActivity extends AppCompatActivity {
    WebView webView;
    private static ThrRequest thrRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.webView);
        TextView tvContent = findViewById(R.id.tvContent);
        EditText etURL = findViewById(R.id.etURL);
    if(thrRequest == null) {
        thrRequest = new ThrRequest();
        thrRequest.setWidgets(etURL, tvContent, MainActivity.this);
        thrRequest.start();
    }
    else
    {
        thrRequest.setWidgets(etURL, tvContent, MainActivity.this);
    }

    }

    class ThrRequest extends Thread
    {
        private TextView tvContent;
        private Activity activity;
        private EditText etUrl;

        public void setWidgets(EditText edtUrl, TextView tvContent, Activity activity)
        {
            this.tvContent = tvContent;
            this.etUrl = etUrl;
            this.activity = activity;
        }

        @Override
        public void run() {
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                try {

//
//            Network[] allNetworks = cm.getAllNetworks();
//            for (Network N : allNetworks) {
//                NetworkInfo NI = cm.getNetworkInfo(N);
//                Log.d("NETWORK INFO", "Type  name: " + NI.getTypeName());
//                Log.d("NETWORK INFO", "Subtype name: " + NI.getSubtypeName());
//                Log.d("NETWORK INFO", "State: " + NI.getState().toString());
//                Log.d("NETWORK INFO", "Extra Info: " + NI.getExtraInfo());
//                Log.d("NETWORK INFO", "Available: " + NI.isAvailable());
//                Log.d("NETWORK INFO", "Connected: " + NI.isConnected());
//                Log.d("NETWORK INFO", "+++++++++++++++++++++++++");
//            }

//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                Network N = cm.getActiveNetwork();
//            }
//                    //-----Получение ссылки для активного подключения
//                    ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
//                    Network net = null;
//                    //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                        net = cm.getActiveNetwork();
//                    //}
//                    //-----Получение объекта HttpURLConnection и отправка запроса
////                    HttpsURLConnection connection = (HttpsURLConnection) net.openConnection(new URL("https://rozetka.com.ua"));
//                    HttpURLConnection connection = (HttpURLConnection) net.openConnection(new URL("http://10.3.11.10/test.php?"+
//                            "lastname=Gates&"+
//                            "firstname=Bill&"+
//                            "pswd=qwerty&"+
//                            "js=on"+
//                            "color=green"+
//                            "gender=male"+
//                            "weekday=3"));
//                    connection.setDoInput(true);
//
//                    connection.connect();
//
//                    //-----Чтение полученного от сервера ответа
//                    InputStream inputStream = connection.getInputStream();
//
//                    ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
//                    byte[]b = new byte[1024];
//                    while (true)
//                    {
//                        Log.d("Available : ", inputStream.available() + " b");
//                        int cnt = inputStream.read(b,0,b.length);
////                        Log.d("Available : ", inputStream.available() + " b");
//                        Log.d("Cnt : ", cnt + " b");
//                        if(cnt == -1)break;
//                        BAOS.write(b,0,cnt);
//                    }
//                    byte[] a = BAOS.toByteArray();
//                    Log.d("content size : ", a.length + " b");
//                    BAOS.reset();
//                    final String content = new String(a,0 ,a.length,"UTF8");
//                    Log.d("content", content);
//                    //------ отображае ответ
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
////                            Log.d("content", content);
////                            tvContent.setText(content);
//                            webView.getSettings().setJavaScriptEnabled(true);
//                            webView.loadDataWithBaseURL("", content, "text/html", "utf-8", "");
//                        }
//                    });
//                    //------закрываем соединение
//                    connection.disconnect();

                    //-----Получение ссылки для активного подключения
                    ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
                    Network net = null;
                    //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    net = cm.getActiveNetwork();
                    //}
                    //-----Получение объекта HttpURLConnection и отправка запроса
//                    HttpsURLConnection connection = (HttpsURLConnection) net.openConnection(new URL("https://rozetka.com.ua"));
                    HttpURLConnection connection = (HttpURLConnection) net.openConnection(new URL("http://10.3.203.3:4000"));

                    connection.setDoInput(true); // будем читать
                    connection.setDoOutput(true); // будем писать

                    connection.connect();

                    //----запись данных в POST в тело HTTP запроса
                    OutputStream outputStream = connection.getOutputStream();
                    String query = "command=upper&string=hello";
                    byte[] queryBytes = query.getBytes();
                    outputStream.write(queryBytes, 0, queryBytes.length);
            //-----Чтение полученного от сервера ответа
                    InputStream inputStream = connection.getInputStream();

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
//                            Log.d("content", content);
//                            tvContent.setText(content);
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
