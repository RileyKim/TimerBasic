package com.taeksukim.android.threadbasic;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button btnStart, btnStop;
    boolean flag = false;
    Thread thread;
    //핸들이 메시지에 담겨오는 what에 대한 정의
    public static final int SET_TEXT = 100;

    int temp = 0;

    //메시지를 받는 서버를 오픈
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SET_TEXT :

                    textView.setText(msg.arg1+"");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomThread customThread = new CustomThread();
                if (flag) {
                    Toast.makeText(MainActivity.this, "실행중입니다",Toast.LENGTH_SHORT).show();
                }else {
                    flag = true;
                    thread = new CustomThread();
                    thread.start();
                }
                }


        });
        btnStop = (Button) findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopProgram();
            }
        });


    }



    public void runProgram(){
        flag = true;
    }

    public void stopProgram(){
        flag = false;

    }


    class CustomThread extends Thread {
        @Override
        public void run(){
            int sec = 0;
            //thread 안에서 무한반복일때는
            //thread를 중단 시킬 수 있는 키값을 꼭 세팅해서
            //메인 thread가 종료시에 같이 종료될 수 있도록 해야한다.
            //이유 : 경우에 따라 interrupt로 thread가 종료되지 않을 수 있기 때문에.
            while (flag) {
                Message msg = new Message();
                msg.what = SET_TEXT;
                msg.arg1 = sec;
                handler.sendMessage(msg);
                sec++;
                //handler.sendEmptyMessage(SET_TEXT);
            try {
                Thread.sleep(1000);
            }catch(Exception e){
                e.printStackTrace();
            }

            }
        }
    }

    @Override
    protected void onDestroy() {
        flag = false;
        thread.interrupt();
        super.onDestroy();
    }
}

