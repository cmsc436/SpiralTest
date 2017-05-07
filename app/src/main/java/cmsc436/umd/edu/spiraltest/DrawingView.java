package cmsc436.umd.edu.spiraltest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {
    private Path drawPath;
    private Paint drawPaint, canvasPaint;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;
    private boolean pause;

    public DrawingView(Context context, AttributeSet attrs){
        super(context,attrs);
        setupDrawing();
    }

    public void setDrawPaintSize(int size) {
        drawPaint.setStrokeWidth(size);
    }

    private void setupDrawing(){
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(ContextCompat.getColor(getContext(),R.color.colorPrimary436));
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(40);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        //i know, switch cases lmao
        if (!pause) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    drawPath.moveTo(touchX, touchY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    drawPath.lineTo(touchX, touchY);
                    break;
                case MotionEvent.ACTION_UP:
                    drawCanvas.drawPath(drawPath, drawPaint);
                    drawPath.reset();
                    break;
                default:
                    return false;
            }

            invalidate();
        }

        return true;
    }

    // for when the timer goes up on the spiral test
    public void pause() {
        pause = true;
    }

    public void clear() {
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
    }

    public void displayScore(float score) {
        Paint scoreColor = new Paint();
        scoreColor.setColor(Color.BLACK);
        scoreColor.setTextSize(80);
        drawCanvas.drawText("Score: " + score, (float)drawCanvas.getWidth()/8,(float)drawCanvas.getHeight()*7/8,scoreColor);
    }
}

