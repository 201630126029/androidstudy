package com.example.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.nio.file.AtomicMoveNotSupportedException;
import java.security.PublicKey;

public class CheatActivity extends AppCompatActivity {

    private boolean mAnswerIsTrue;
    private static final String EXTRA_ANSWER_IS_TRUE="com.example.geoquiz.answer_is_true";
    private TextView mAnswerTextView;
    private Button mShowAnswerButton;
    private static final String EXTRA_ANSWER_SHOWN="com.example.geoquiz.answer_shown";
    private static final String KEY_TOTAL_SHOWN="totalshown";
    @Override
//    boolean answerIstrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
//                intent.putExtra(EXTRA_ANSWER_IS_TRUE,answerIstrue);
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        mAnswerTextView=(TextView)findViewById(R.id.answer_text_view);
        mShowAnswerButton=(Button)findViewById(R.id.show_answer_button);
        final int mTotalShown = getIntent().getIntExtra(KEY_TOTAL_SHOWN, 0);
        if(mTotalShown >= 3){
            mShowAnswerButton.setEnabled(false);
        }
        mShowAnswerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mAnswerIsTrue){
                    mAnswerTextView.setText(R.string.true_button);
                }
                else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                setAnswerShownResult(true, mTotalShown+1);
            }
        });

    }
    public static Intent newIntent(Context packageContext, boolean answerIstrue, int mTotalShown){
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIstrue);
        intent.putExtra(KEY_TOTAL_SHOWN,mTotalShown );
        return intent;
    }

    private void setAnswerShownResult(boolean isAnswerShown, int mTotalShown){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        data.putExtra(KEY_TOTAL_SHOWN, mTotalShown);
        setResult(RESULT_OK, data);
    }

    public   static boolean wasAnswerShown(Intent result){
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN,false);
    }

}
