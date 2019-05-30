package com.example.paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.view.DragEvent;
import android.view.View;

public class MyView extends View {
    public MyView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0xff0000ff);
        paint.setStyle(Paint.Style.STROKE);
        Path path = new Path();
        path.addCircle(150, 100, 100, Path.Direction.CCW);
        paint.setTextSize(26);
//        canvas.drawPath(path, paint);
        canvas.drawTextOnPath("活着就是为了改变世界", path, 0, 0, paint);

    }
}
