package com.example.geoquiz;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;
    public static final String KEY_INDEX = "index";
    private static final String TAG="QuizActivity";
    private static final String KEY_ANSWERED = "answered";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bunble)called");
        setContentView(R.layout.activity_main);
        if(savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            boolean answerIsAnswered[] = savedInstanceState.getBooleanArray(KEY_ANSWERED );
            for(int i=0; i<mQuestionBank.length; i++){
                mQuestionBank[i].setAnswered(answerIsAnswered[i]);
            }
        }

        mTrueButton = (Button)findViewById(R.id.true_button);
        mFalseButton=(Button)findViewById(R.id.false_button);
        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
        mPrevButton = (ImageButton)findViewById(R.id.prev_button);
        mTrueButton.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View v){
                checkAnswer(true);
            }
        });
        mFalseButton.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View v){
                checkAnswer(false);
            }
        });
        mNextButton = (ImageButton)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mCurrentIndex = (mCurrentIndex+1)%mQuestionBank.length;
                updateQuestion();
            }
        });
        updateQuestion();
        mQuestionTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mCurrentIndex = (mCurrentIndex+1)%mQuestionBank.length;
                updateQuestion();
            }
        });
        mPrevButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mCurrentIndex = (mCurrentIndex-1+mQuestionBank.length)%mQuestionBank.length;
                updateQuestion();
            }
        });
    }
    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans,true),
            new Question(R.string.question_mideast,false),
            new Question(R.string.question_africa,false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia,true)
    };
    private int mCurrentIndex=0;
    private void updateQuestion(){
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        checkIfAnswered();
    }
    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if(userPressedTrue == answerIsTrue){
            messageResId = R.string.correct_toast;
        }
        else{
            messageResId = R.string.incorrect_toast;
        }
        mQuestionBank[mCurrentIndex].setAnswered(true);
        checkIfAnswered();
        Toast.makeText(this,messageResId, Toast.LENGTH_SHORT).show();
    }
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart()called");
    }
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume()called");
    }
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause()called");
    }
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop()called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        boolean answerIsAnswered[]=new boolean[mQuestionBank.length];
        for(int i=0; i<mQuestionBank.length; i++){
            answerIsAnswered[i]=mQuestionBank[i].isAnswered();
        }
        savedInstanceState.putBooleanArray(KEY_ANSWERED,answerIsAnswered);
    }

    private void checkIfAnswered(){
        boolean answerIsAnswered = mQuestionBank[mCurrentIndex].isAnswered();
        if(answerIsAnswered == true){
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
        }
        else {
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
        }
    }
}
