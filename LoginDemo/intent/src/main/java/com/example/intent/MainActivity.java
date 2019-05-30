package com.example.intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private Button mButton;
    final int distance=50;
    private Animation[] mAnimations = new Animation[4];
    private int[] images=new int[]{
            R.drawable.img01, R.drawable.img02,R.drawable.img03,
            R.drawable.img04,R.drawable.img05,R.drawable.img06,
            R.drawable.img07,R.drawable.img08,R.drawable.img09
    };
    private GestureDetector mDetector;
    private ViewFlipper flipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDetector = new GestureDetector(MainActivity.this, this);
        flipper = findViewById(R.id.flipper);
        for (int i = 0; i < images.length; i++) {
            ImageView image = new ImageView(this);
            image.setImageResource(images[i]);
            flipper.addView(image);
        }

        mAnimations[0]= AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        mAnimations[1]= AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        mAnimations[2]= AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        mAnimations[3]= AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
        getResources().getDimension()
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //from right to left
        if(e1.getX() - e2.getX() > distance){
            flipper.setInAnimation(mAnimations[2]);
            flipper.setOutAnimation(mAnimations[1]);
            flipper.showPrevious();
            return true;
        }else if(e2.getX()-e1.getX()>distance){
            flipper.setInAnimation(mAnimations[0]);
            flipper.setOutAnimation(mAnimations[3]);
            flipper.showNext();
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }
}
