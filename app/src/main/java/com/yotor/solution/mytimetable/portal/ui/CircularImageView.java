package com.yotor.solution.mytimetable.portal.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.yotor.solution.mytimetable.R;


public class CircularImageView extends AppCompatImageView {
    private static float circularImageBorder = 2.0f;
    private final Paint bitmapPaint;
    private final Paint borderPaint;
    private final RectF destination;
    private final Matrix matrix;
    private final RectF source;

    public CircularImageView(Context context) {
        this(context, null);
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircularImageView);
        this.matrix = new Matrix();
        this.source = new RectF();
        this.destination = new RectF();
        this.bitmapPaint = new Paint();
        this.bitmapPaint.setAntiAlias(true);
        this.bitmapPaint.setFilterBitmap(true);
        this.bitmapPaint.setDither(true);
        this.borderPaint = new Paint();
        this.borderPaint.setColor(array.getColor(R.styleable.CircularImageView_boarder_color, Color.MAGENTA));
        this.borderPaint.setStyle(Paint.Style.STROKE);
        this.borderPaint.setStrokeWidth(array.getFloat(R.styleable.CircularImageView_boarder, circularImageBorder));
        this.borderPaint.setAntiAlias(true);
        array.recycle();
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        BitmapDrawable bitmapDrawable = null;
        if (drawable instanceof StateListDrawable) {
            if (drawable.getCurrent() != null) {
                bitmapDrawable = (BitmapDrawable) drawable.getCurrent();
            }
        } else if (drawable instanceof BitmapDrawable) {
            bitmapDrawable = (BitmapDrawable) drawable;
        }else if (drawable instanceof VectorDrawableCompat){
            Drawable f = drawable.getCurrent();
            bitmapDrawable = (BitmapDrawable)f.getCurrent();
        }
        if (bitmapDrawable != null) {
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null) {
                this.source.set(0.0f, 0.0f, (float) bitmap.getWidth(), (float) bitmap.getHeight());
                this.destination.set((float) getPaddingLeft(), (float) getPaddingTop(), (float) (getWidth() - getPaddingRight()), (float) (getHeight() - getPaddingBottom()));
                drawBitmapWithCircleOnCanvas(bitmap, canvas, this.source, this.destination);
            }
        }
    }

    public void drawBitmapWithCircleOnCanvas(Bitmap bitmap, Canvas canvas, RectF source, RectF dest) {
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        this.matrix.reset();
        this.matrix.setRectToRect(source, dest, Matrix.ScaleToFit.FILL);
        shader.setLocalMatrix(this.matrix);
        this.bitmapPaint.setShader(shader);
        canvas.drawCircle(dest.centerX(), dest.centerY(), dest.width() / 2.0f, this.bitmapPaint);
        canvas.drawCircle(dest.centerX(), dest.centerY(), (dest.width() / 2.0f) - (circularImageBorder / 2.0f), this.borderPaint);
    }
}

