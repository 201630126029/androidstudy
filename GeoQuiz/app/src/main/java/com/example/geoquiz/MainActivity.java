package com.example.geoquiz;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
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
    private int mCurrentIndex=0;
    private TextView mQuestionTextView;
    public static final String KEY_INDEX = "index";
    private static final String TAG="QuizActivity";
    private static final String KEY_ANSWERED = "answered";
    private int userAnswerCorrect=0;                                //用户答对的数量
    private static final String KEY_COREECT = "correct";
    private int userAnsweredNum=0;                                //用户已答的数量
    private Button mCheatButton ;
    private static final int REQUEST_CODE_CHEAT=0;
    private static final String KEY_ANSWER_SHOWN = "key_answer_shown";
    private TextView mCompileVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bunble)called");
        setContentView(R.layout.activity_main);
        if(savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            boolean answerIsAnswered[] = savedInstanceState.getBooleanArray(KEY_ANSWERED );
            boolean answerShown[]=savedInstanceState.getBooleanArray(KEY_ANSWER_SHOWN);
            for(int i=0; i<mQuestionBank.length; i++){
                mQuestionBank[i].setAnswered(answerIsAnswered[i]);
                mQuestionBank[i].setAnswerShown(answerShown[i]);
            }
        }

        mTrueButton = (Button)findViewById(R.id.true_button);
        mFalseButton=(Button)findViewById(R.id.false_button);
        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
        mPrevButton = (ImageButton)findViewById(R.id.prev_button);
        mNextButton = (ImageButton)findViewById(R.id.next_button);
        mCheatButton=(Button)findViewById(R.id.cheat_button);
        mCompileVersion = (TextView)findViewById(R.id.compile_version);
        String version = String.valueOf(Build.VERSION.SDK_INT);
        mCompileVersion.setText("API-Version:"+version);
        mTrueButton.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View v){
                checkAnswer(true);
                showRecored();
            }
        });
        mFalseButton.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View v){
                checkAnswer(false);
               showRecored();
            }
        });

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
        mCheatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean answerIsTrue=mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(MainActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
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

    private void updateQuestion(){
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        isButtonVisible();
    }
    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if(mQuestionBank[mCurrentIndex].isAnswerShown()){
            messageResId=R.string.judgement_toast;
        }
        else if(userPressedTrue == answerIsTrue){
            messageResId = R.string.correct_toast;
            userAnswerCorrect++;
        }
        else{
            messageResId = R.string.incorrect_toast;
        }
        mQuestionBank[mCurrentIndex].setAnswered(true);
        isButtonVisible();
        Toast.makeText(this,messageResId, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart()called");
    }
    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume()called");
    }
    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause()called");
    }
    @Override
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
        boolean answerShown[]=new boolean[mQuestionBank.length];
        for(int i=0; i<mQuestionBank.length; i++){
            answerIsAnswered[i]=mQuestionBank[i].isAnswered();
            answerShown[i]=mQuestionBank[i].isAnswerShown();
        }
        savedInstanceState.putBooleanArray(KEY_ANSWERED,answerIsAnswered);
        savedInstanceState.putBooleanArray(KEY_ANSWER_SHOWN,answerShown);
    }


    private void showRecored(){
        boolean allAnswered=true;
        String message=null;
        double correctMark=0;  //百分比形式的给分
        int correctAnswerNum=0;  //答对的题目数量
        for(int i=0; i<mQuestionBank.length; i++){
            if(mQuestionBank[i].isAnswered() == false){
                allAnswered=false;
                break;
            }
        }
        if(allAnswered==true){
            correctMark=(double)userAnswerCorrect/mQuestionBank.length;
            correctMark=(double)((int)(correctMark*10000)/100.0);
            message="正确率："+String.valueOf(correctMark)+"%";
            Toast.makeText(this, message,Toast.LENGTH_SHORT ).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return ;
        }
        if(requestCode == REQUEST_CODE_CHEAT){
            if(data == null){
                return ;
            }
            boolean mIsCheater=CheatActivity.wasAnswerShown(data);
            if(mIsCheater){
                mQuestionBank[mCurrentIndex].setAnswerShown(true);
                isButtonVisible();
                showRecored();
            }
        }
    }

    protected void isButtonVisible(){
        boolean mAnswerShown = mQuestionBank[mCurrentIndex].isAnswerShown();
        boolean mAnswered = mQuestionBank[mCurrentIndex].isAnswered();
        if(mAnswered || mAnswerShown){
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
        }
        else {
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
        }
    }
}
