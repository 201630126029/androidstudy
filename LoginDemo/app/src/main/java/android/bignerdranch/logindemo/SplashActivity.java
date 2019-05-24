package android.bignerdranch.logindemo;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /**
         * 这里其实不晓得什么意思
         */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /**
         * 这里原来还有Handle的postDelayed方法来执行一个线程
         */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, Login_opt.class);
                startActivity(mainIntent);
                finish();
               /* overridePendingTransition(R.anim.mainfadein,
                        R.anim.splashfadeout);*/
            }
        }, 3000);
    }
}
