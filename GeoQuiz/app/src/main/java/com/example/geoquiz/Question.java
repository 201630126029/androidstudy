package com.example.geoquiz;

public class Question {
    public int mTextResId;
    private boolean mAnswerTrue;
    private boolean mAnswered;
    public Question(int textResId, boolean answerTrue){
        mAnswerTrue = answerTrue;
        mTextResId  = textResId;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public boolean isAnswered() {
        return mAnswered;
    }

    public void setAnswered(boolean answered) {
        mAnswered = answered;
    }
}
