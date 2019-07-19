package com.example.a2.socket;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.a2.R;
import com.example.a2.utils.MyUtils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TcpClientActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "TCPClientActivity";
    private static final int MSG_RECEIVE_NEW_MSG = 1;
    private static final int MSG_SOCKET_CONNECTED = 2;

    private Button mSendButton;
    private TextView mMessageTextView;
    private EditText mMessageEditText;

    private PrintWriter mPrintWriter;
    private Socket mClientSocket;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_RECEIVE_NEW_MSG:
                    mMessageTextView.setText(mMessageTextView.getText() + (String) msg.obj);
                    break;
                case MSG_SOCKET_CONNECTED:
                    mSendButton.setEnabled(true);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcp_client);
        Log.d(TAG, "onCreate");
        mMessageTextView = findViewById(R.id.msg_container);
        mSendButton = findViewById(R.id.send);
        mSendButton.setOnClickListener(this);
        mMessageEditText = findViewById(R.id.msg);
        Intent intent = new Intent(this, TCPServerService.class);
        startService(intent);
        Log.d(TAG, "onCreate");
        new Thread() {
            @Override
            public void run() {
                connectTCPServer();
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        if (mClientSocket != null) {
            try {
                //连接的关闭要先把流给杀死，然后才能关闭连接
                mClientSocket.shutdownInput();
                mClientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        super.onDestroy();
    }

    private void connectTCPServer() {
        Socket socket = null;
        while (socket == null) {
            try {
                socket = new Socket("localhost", 8888);
                mClientSocket = socket;

                mPrintWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                        socket.getOutputStream()
                )), true);
                mHandler.sendEmptyMessage(MSG_SOCKET_CONNECTED);

            } catch (IOException e) {
                e.printStackTrace();
                SystemClock.sleep(1000);
                Log.d(TAG, "connect server failed, retry...");
            }

        }
        Log.d(TAG, "connect server success. ");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    mClientSocket.getInputStream()
            ));
            
            Log.d(TAG, br.toString());
            while (!TcpClientActivity.this.isFinishing()) {
                Log.d(TAG, "qian");
                String msg = br.readLine();
                Log.d(TAG, "hou");
                Log.d(TAG, "receiver:" + msg);
                if (msg != null) {
                    String time = fromatDateTime(System.currentTimeMillis());
                    final String showMsg = "server " + time + ":" + msg + "\n";
                    mHandler.obtainMessage(MSG_RECEIVE_NEW_MSG, showMsg)
                            .sendToTarget();
                }
            }
            Log.d(TAG, "quit...");
            MyUtils.close(mPrintWriter);
            MyUtils.close(br);
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String fromatDateTime(long time) {
        return new SimpleDateFormat("(HH:mm:ss)").format(new Date(time));
    }


    @Override
    public void onClick(View v) {
        if (v == mSendButton) {
            Log.d(TAG, "点击了");
            final String msg = mMessageEditText.getText().toString();
            if (!TextUtils.isEmpty(msg) && mPrintWriter != null) {
                new Thread(){
                    @Override
                    public void run() {
                        mPrintWriter.println(msg);
                    }
                }.start();
                mMessageEditText.setText("");
                String time = fromatDateTime(System.currentTimeMillis());
                final String showedMsg = "self" + time + ":" + msg + "\n";
                mMessageTextView.setText(mMessageTextView.getText() + showedMsg);
            }
        }
    }
}
