package com.sanfulou.qrcode2020.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.github.florent37.viewanimator.ViewAnimator;
import com.sanfulou.qrcode2020.R;
import com.sanfulou.qrcode2020.utils.FocusBoxUtils;

import static com.sanfulou.qrcode2020.utils.ImageUtils.drawableToBitmap;

public class CameraFocusBoxView extends View {

    private int MIN_FOCUS_BOX_WIDTH;
    private int MIN_FOCUS_BOX_HEIGHT;
    private final Paint paint;
    private final int maskColor;
    private final int frameColor;
    private final int cornerColor;
    private Bitmap bitmapLeftTop;
    private Bitmap bitmapLeftBottom;
    private Bitmap bitmapRightTop;
    private Bitmap bitmapRightBottom;
    private Bitmap bitmapRightBottomResize;
    private int wBitmap, hBitmap;
    private Paint paintScan = new Paint();
    private int mPosX = 0;
    private boolean runAnimation = false;
    private boolean showLine = false;
    private Handler handler;
    private Runnable refreshRunnable;
    private boolean isGoingDown = false;
    private int mWidth;
    private Rect box;
    private static Point ScrRes;

    @SuppressLint("ClickableViewAccessibility")
    public CameraFocusBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.MIN_FOCUS_BOX_WIDTH = (int) FocusBoxUtils.pxFromDp(context, 18f);
        this.MIN_FOCUS_BOX_HEIGHT = (int) FocusBoxUtils.pxFromDp(context, 30f);
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Resources resources = getResources();
        this.maskColor = resources.getColor(R.color.focus_box_mask);
        this.frameColor = Color.TRANSPARENT;
        this.cornerColor = resources.getColor(R.color.focus_box_corner);
        this.bitmapRightTop = rotateBitmap(drawableToBitmap(getResources().getDrawable(R.drawable.scan)), 90);
        this.bitmapRightBottom = rotateBitmap(drawableToBitmap(getResources().getDrawable(R.drawable.scan)), 180);
        this.bitmapLeftBottom = rotateBitmap(drawableToBitmap(getResources().getDrawable(R.drawable.scan)), 270);
        this.bitmapLeftTop = rotateBitmap(drawableToBitmap(getResources().getDrawable(R.drawable.scan)), 0);
        // this.bitmapRightBottomResize = rotateBitmap(drawableToBitmap(getResources().getDrawable(R.drawable.coner)), 45);
        this.wBitmap = (bitmapRightTop.getWidth() / 2);
        this.hBitmap = (bitmapRightTop.getHeight() / 2);
        //this.setOnTouchListener(getTouchListener());
        initScan();
        this.startAnimation();

    }

    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private Rect getBoxRect() {

        if (this.box == null) {

            ScrRes = FocusBoxUtils.getScreenResolution(getContext());

            int width = ScrRes.x * 6 / 7;
            int height = ScrRes.y / 9;

            width = width == 0
                    ? MIN_FOCUS_BOX_WIDTH
                    : Math.max(width, MIN_FOCUS_BOX_WIDTH);

            height = height == 0
                    ? MIN_FOCUS_BOX_HEIGHT
                    : Math.max(height, MIN_FOCUS_BOX_HEIGHT);

            int left = (ScrRes.x - width) / 2;
            int top = (ScrRes.y - height) / 5;

            this.box = new Rect(left, top, left + width, (top + height) * 2);
        }

        return box;
    }

    public Rect getBox() {
        return box;
    }

    private void updateBoxRect(int dW, int dH) {
        int newWidth = (this.box.width() + dW > ScrRes.x - 4 || this.box.width() + dW < this.MIN_FOCUS_BOX_WIDTH)
                ? 0
                : this.box.width() + dW;

        int newHeight = (this.box.height() + dH > ScrRes.y - 4 || this.box.height() + dH < this.MIN_FOCUS_BOX_HEIGHT)
                ? 0
                : this.box.height() + dH;

        int leftOffset = (ScrRes.x - newWidth) / 2;

        int topOffset = (ScrRes.y - newHeight) / 4;

        if (newWidth < this.MIN_FOCUS_BOX_WIDTH || newHeight < this.MIN_FOCUS_BOX_HEIGHT)
            return;

        if (topOffset + newHeight > (ScrRes.y / 2)) {
            return;
        }
        if (newHeight < this.MIN_FOCUS_BOX_WIDTH * 3) {
            return;
        }
        if (newWidth < (this.MIN_FOCUS_BOX_WIDTH) * 3) {
            return;
        }
        this.box = new Rect(leftOffset, topOffset, leftOffset + newWidth, topOffset + newHeight);
    }

    boolean test = false;

    @SuppressLint("CanvasSize")
    @Override
    public void onDraw(Canvas canvas) {
        Rect frame = getBoxRect();
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        paint.setColor(maskColor);
        canvas.drawRect(0, 0, width, frame.top, paint);

        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);

        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);

        canvas.drawRect(0, frame.bottom + 1, width, height, paint);

        paint.setAlpha(0);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(frameColor);
        //->Top left
        canvas.drawRect(frame.left, frame.top, frame.right + 1, frame.top + 2, paint);
        //->Top Right
        canvas.drawRect(frame.left, frame.top + 2, frame.left + 2, frame.bottom - 1, paint);
        //->Bottom left
        canvas.drawRect(frame.right - 1, frame.top, frame.right + 1, frame.bottom - 1, paint);
        //->Bottom Right
        canvas.drawRect(frame.left, frame.bottom - 1, frame.right + 1, frame.bottom + 1, paint);

        paint.setColor(cornerColor);

        //->Top left
        int center = (wBitmap / 3) + 2;
        if (bitmapCrop != null) {

            canvas.drawBitmap(bitmapCrop, frame.left, frame.top, paint);
        }
        canvasScan(canvas, frame);
//        if (showLine) {
//            return;
//        }
        canvas.drawBitmap(bitmapLeftTop, frame.left - center, frame.top - center, paint);
        //->Top Right
        canvas.drawBitmap(bitmapRightTop, frame.right - (wBitmap * 2) + center, frame.top - center, paint);
        //->Bottom left
        canvas.drawBitmap(bitmapLeftBottom, frame.left - center, frame.bottom - (hBitmap * 2) + center, paint);
        //->Bottom Right
        canvas.drawBitmap(bitmapRightBottom, frame.right - (wBitmap * 2) + center, frame.bottom - (hBitmap * 2) + center, paint);
        //->Bottom Right Resize
//        canvas.drawBitmap(bitmapRightBottomResize, frame.right - (wBitmap * 2) + center / 2, frame.bottom - (hBitmap * 2) + center / 2, paint);
//        //->Scan


    }

    private void canvasScan(Canvas canvas, Rect frame) {
        mWidth = frame.right;
        if (showLine) {
            canvas.drawLine(mPosX, frame.top, mPosX, frame.bottom - 1, paintScan);
        }
        if (runAnimation) {
            handler.postDelayed(refreshRunnable, 0);
        }
    }

    private void initScan() {
        paintScan.setColor(getResources().getColor(R.color.bg));
        paintScan.setStrokeWidth(3);
        handler = new Handler();
        refreshRunnable = this::refreshView;
    }

    public void startAnimation() {
        runAnimation = true;
        showLine = true;
        mPosX = getBoxRect().left;
        this.invalidate();
    }

    public void stopAnimation() {
        runAnimation = false;
        showLine = false;
        test = false;
        this.bitmapCrop = null;
        reset();
        this.invalidate();
    }

    private void reset() {
        mPosX = 0;
        isGoingDown = true;
    }

    private void refreshView() {
        Rect frame = getBoxRect();
        if (isGoingDown) {
            mPosX += 9;
            if (mPosX > mWidth) {
                mPosX = mWidth;
                isGoingDown = false;
            }
        } else {
            if (mPosX - 9 > frame.left) {
                mPosX -= 9;
            } else {
                mPosX = frame.left;
                isGoingDown = true;
            }

        }
        this.invalidate();
    }

    private Bitmap bitmapCrop = null;

    public void setBitmapCrop(Bitmap bitmap) {
        this.bitmapCrop = bitmap;
    }


}

