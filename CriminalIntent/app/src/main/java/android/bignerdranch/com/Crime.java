package android.bignerdranch.com;

import java.util.Date;
import java.util.UUID;

public class Crime {
    private UUID mId;  //直接是一个类，里面有
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    public Crime(){
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public Date getDate() {
        return mDate;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public String getTitle() {
        return mTitle;
    }

    public UUID getId() {
        return mId;
    }
}
