package com.example.dell.networktest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.
        OnClickListener{

    private static final int SHOW_RESPONSE = 0;

    private Button sendRequest;

    private TextView responseText;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SHOW_RESPONSE:
                    String responses = (String) msg.obj;
                    responseText.setText(responses);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendRequest = (Button) findViewById(R.id.send_request);
        responseText = (TextView) findViewById(R.id.response);
        sendRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.send_request){
            sendRequestWithURLConnection();
//            sendRequestWithHttpClient();
        }
    }

    private void sendRequestWithURLConnection(){
        new Thread(new Runnable() {
            /*
                对于Android4.0之上的环境下，不能在主线程中访问网络
                所以这里另新建了一个实现了Runnable接口的Http访问类
             */
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    URL url = new URL("https://www.youtube.com");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");//默认为GET
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    if(connection.getResponseCode() == HttpURLConnection.
                            HTTP_OK){
                        InputStream in = connection.getInputStream();
                        BufferedReader reader = new BufferedReader
                                (new InputStreamReader(in));
                        StringBuilder builder = new StringBuilder();
                        String line;
                        while((line = reader.readLine()) != null){
                            builder.append(line);
                        }
                        Message message = new Message();
                        message.what = SHOW_RESPONSE;
                        message.obj = builder.toString();
                        handler.sendMessage(message);
                    }
                    else{
                        Log.d("Boss","failed network" + connection.
                                getResponseCode());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    if(connection != null){
                        connection.disconnect();
                    }
                }

            }
        }).start();
    }

//    public void sendRequestWithHttpClient(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    HttpClient httpClient = new DefaultHttpClient();
//                    HttpGet httpGet = new HttpGet("www.baidu.com");
//                    HttpResponse httpResponse = httpClient.execute(httpGet);
//                    if(httpResponse.getStatusLine().getStatusCode() == 200){
//                        //请求和响应都成功
//                        HttpEntity httpEntity = httpResponse.getEntity();
//                        String response = EntityUtils.toString(httpEntity,
//                                "UTF-8");
//                        Message message = new Message();
//                        message.what = SHOW_RESPONSE;
//                        message.obj = response.toString();
//                        handler.sendMessage(message);
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
}
