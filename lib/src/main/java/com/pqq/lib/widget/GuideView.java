package com.pqq.lib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.pqq.lib.HellowData;
import com.pqq.lib.R;
import com.pqq.lib.util.ScreenUtil;

import java.lang.reflect.Field;
import java.util.HashMap;

public class GuideView extends View {

    private HellowData[] hellowDatas;
    private int maskLayerColor;
    private HashMap<HellowData, HellowData> mPositionCache;

    public GuideView(Context context) {
        super(context);
        initView(context, null);
    }

    public GuideView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public GuideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GuideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private Paint mPaint;

    private void initView(Context context, @Nullable AttributeSet attrs) {
        mPositionCache = new HashMap<HellowData, HellowData>(8);

        mPaint = new Paint();
        maskLayerColor = getResources().getColor(R.color.colorMaskLayer);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(ScreenUtil.getScreenWidth(getContext()), ScreenUtil.getScreenHeight(getContext()));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.saveLayer(0, 0, getWidth(), getHeight(), null);
        } else {
            canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        }
        drawBackgroud(canvas);
        drawHellow(canvas);
        canvas.restore();
    }

    /**
     * 绘制半透明蒙层
     *
     * @param canvas
     */
    private void drawBackgroud(Canvas canvas) {
        mPaint.setXfermode(null);
        mPaint.setColor(maskLayerColor);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
    }

    /**
     * 绘制镂空
     *
     * @param canvas
     */
    private void drawHellow(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        mPaint.setXfermode((new PorterDuffXfermode(PorterDuff.Mode.DST_OUT)));
        for (HellowData hellowData : hellowDatas) {
            if (!tryDrawBackground(hellowData, canvas)) {
                drawSingleHellow(hellowData, canvas);
            }
        }
    }

    private void drawSingleHellow(HellowData hellowData, Canvas canvas) {
        HellowData cacheData = mPositionCache.get(hellowData);
        if (cacheData != null) {
            onDrawSingleHellow(cacheData, canvas);
        }

        if (hellowData == null) {
            View view = hellowData.hellowView;
            hellowData.targetRect = new Rect();
            int[] locations = new int[2];
            view.getLocationOnScreen(locations);
            view.getDrawingRect(hellowData.targetRect);
            hellowData.targetRect.left = locations[0];
            hellowData.targetRect.top = locations[1];
            hellowData.targetRect.right += hellowData.targetRect.left;
            hellowData.targetRect.bottom += hellowData.targetRect.top;

            hellowData.targetRect.top -= ScreenUtil.getScreenHeight(getContext());
            hellowData.targetRect.bottom -= ScreenUtil.getScreenHeight(getContext());

            if (hellowData.padding > 0) {
                hellowData.targetRect.left += hellowData.padding;
                hellowData.targetRect.right -= hellowData.padding;
                hellowData.targetRect.top += hellowData.padding;
                hellowData.targetRect.bottom -= hellowData.padding;
            }

            mPositionCache.put(hellowData, hellowData);
            onDrawSingleHellow(hellowData, canvas);
        }
    }

    /**
     * 根据View背景镂空
     */
    private boolean tryDrawBackground(HellowData hellowData, Canvas canvas) {
        Drawable drawable = hellowData.hellowView.getBackground();

        if (drawable instanceof GradientDrawable) {
            return tryDrawGradientBackground(hellowData, (GradientDrawable) drawable, canvas);
        }

        if (drawable instanceof StateListDrawable) {
            return tryDrawStatelistBackground(hellowData, (StateListDrawable) drawable, canvas);
        }

        return false;
    }

    /**
     *
     */
    private boolean tryDrawGradientBackground(HellowData hellowData, GradientDrawable drawable, Canvas canvas) {
        Field fieldGradientStateFiled;
        int mShape = GradientDrawable.RECTANGLE;
        Object mGradientState = null;
        try {
            fieldGradientStateFiled =
                    Class.forName("android.graphics.drawable.GradientDrawable")
                            .getDeclaredField("mGradientState");
            fieldGradientStateFiled.setAccessible(true);
            mGradientState = fieldGradientStateFiled.get(drawable);

            Field fieldShape = mGradientState.getClass().getDeclaredField("mShape");
            fieldShape.setAccessible(true);
            mShape = (int) fieldShape.get(mGradientState);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        float mRadius = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mRadius = drawable.getCornerRadius();
        } else {
            Field fieldRadius = null;
            try {
                fieldRadius = mGradientState.getClass().getDeclaredField("mRadius");
                fieldRadius.setAccessible(true);
                mRadius = (float) fieldRadius.get(mGradientState);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (mShape == GradientDrawable.OVAL) {
            canvas.drawOval(new RectF(hellowData.targetRect.left, hellowData.targetRect.top,
                    hellowData.targetRect.right, hellowData.targetRect.bottom), mPaint);
        }else {
            float radius = Math.min(mRadius,Math.min(hellowData.targetRect.width(),hellowData.targetRect.height())*0.5f);
            canvas.drawRoundRect(new RectF(hellowData.targetRect.left, hellowData.targetRect.top,
                    hellowData.targetRect.right, hellowData.targetRect.bottom), radius, radius, mPaint);

        }

        return false;
    }

    private boolean tryDrawStatelistBackground(HellowData hellowData, StateListDrawable drawable, Canvas canvas) {
        return false;

    }


    private void onDrawSingleHellow(HellowData hellowData, Canvas canvas) {
        canvas.drawRect(hellowData.targetRect, mPaint);
    }

    public void setHellowDatas(HellowData[] hellowDatas) {
        this.hellowDatas = hellowDatas;
    }

    public Object getFiledData(Object data, String filedName) {

        try {
            Field field = data.getClass().getDeclaredField(filedName);

            field.setAccessible(true);

            Object value = field.get(data);

            return value;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
