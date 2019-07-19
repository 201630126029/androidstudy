package com.example.a2.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.a2.utils.MyUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class TCPServerService extends Service {
    private static final String TAG = "TCPServerService";
    /**
     * 启动的线程已经脱离服务的主线程了，
     */
    private boolean mIsServerDestroyed = false;
    private String[] mDefinedMessages = new String[]{
            "你好呀，哈哈",
            "请问你叫什么名字",
            "今天的天气不错",
            "你知道吗，我可以和多个人聊天哦",
            "给你讲个笑话吧，据说爱笑的人运气不会太差，不知道真假"
    };

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
        Log.d(TAG, "onCreate");
        new Thread(new TcpServer()).start();
    }

    public TCPServerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        mIsServerDestroyed=true;
        super.onDestroy();
    }

    private class TcpServer implements Runnable{
        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try{
                //建立服务器的端口
                serverSocket=new ServerSocket(8888);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "establish tcp server failed, port:8888");
                return ;
            }
            if (serverSocket!=null){
                Log.d(TAG, "服务器端口成功...");
            }
            //如果没有服务没有被杀死
            while(!mIsServerDestroyed){
                try{
                    //得到和客户端的连接端口
                    final Socket client = serverSocket.accept();
                    Log.d(TAG, "accept socket");
                    new Thread(){
                        @Override
                        public void run() {
                            try{
                                //开个新的线程，在这个新的线程里面进行和客户的交流
                                responseClient(client);
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "连接失败");
                }
            }
        }
    }

    /**
     * 进行和客户端的交流
     * @param client 和客户端连接的端口
     * @throws IOException
     */
    private void responseClient(Socket client) throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
        Log.d(TAG, out.toString());
        out.println("欢迎来到聊天室");
        out.flush();
        while(!mIsServerDestroyed){
            String str = in.readLine();
            Log.d(TAG, "msg from client:"+str);
            if (str == null){
                break;
            }
            int i = new Random().nextInt(mDefinedMessages.length);
            String msg = mDefinedMessages[i];
            out.println(msg);
            out.flush();
            Log.d(TAG, "send : "+msg);
        }
        Log.d(TAG, "client quit...");
        MyUtils.close(in);
        MyUtils.close(out);
        client.close();
    }
}
