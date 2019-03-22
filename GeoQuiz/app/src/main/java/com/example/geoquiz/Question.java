package com.example.geoquiz;

public class Question {
    public int mTextResId;  //题目id
    private boolean mAnswerTrue; //答案是true还是false
    private boolean mAnswered;  //这题是否已答
    private boolean mAnswerShown;
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

    public boolean isAnswerShown() {
        return mAnswerShown;
    }

    public void setAnswerShown(boolean answerShown) {
        mAnswerShown = answerShown;
    }
}
