package com.jym.sandbox.seekbar.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.widget.SeekBar;

/**
 * Created by Jimmy on 2016/5/19 0019.
 */
public class SubsectionDrawable extends Drawable {

    private SeekBar seekBar;
    private int dots;
    private Paint unselectedLinePaint;
    private Paint unselectedCirclePaint;

    public SubsectionDrawable(SeekBar seekBar, int dots, int lineColor, int circleColor) {
        this.seekBar = seekBar;
        this.dots = dots;

        unselectedLinePaint = new Paint();
        unselectedLinePaint.setAntiAlias(true);
        unselectedLinePaint.setColor(lineColor);
        unselectedLinePaint.setStrokeWidth(toPix(5));

        unselectedCirclePaint = new Paint();
        unselectedCirclePaint.setAntiAlias(true);
        unselectedCirclePaint.setColor(circleColor);
        unselectedCirclePaint.setStrokeWidth(toPix(5));
    }

    public void draw(Canvas canvas) {
        float height = toPix(15) / 2;
        float width = getBounds().width();
        float radius = toPix(5);
        canvas.drawLine(0, height, width, height, unselectedLinePaint);
        float x = 0;
        for (int i = 0; i < dots; i++) {
            canvas.drawCircle(x, height, radius, unselectedCirclePaint);
            x += width / (dots - 1);
        }
    }

    public void setAlpha(int alpha) {

    }

    public void setColorFilter(ColorFilter colorFilter) {

    }

    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    private float toPix(int size) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, seekBar.getContext().getResources().getDisplayMetrics());
    }
}
