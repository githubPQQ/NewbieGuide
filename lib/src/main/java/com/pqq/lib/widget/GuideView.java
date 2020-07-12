package com.pqq.lib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.pqq.lib.HellowData;
import com.pqq.lib.R;
import com.pqq.lib.util.ScreenUtil;

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
            drawSingleHellow(hellowData, canvas);
        }
    }

    private void drawSingleHellow(HellowData hellowData, Canvas canvas) {
        HellowData cacheData = mPositionCache.get(hellowData);
        if (cacheData != null) {

        }

        if (hellowData == null) {
            View view = hellowData.getHellowInfo();
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

    private void onDrawSingleHellow(HellowData hellowData, Canvas canvas) {
        canvas.drawRect(hellowData.targetRect, mPaint);
    }

    public void setHellowDatas(HellowData[] hellowDatas) {
        this.hellowDatas = hellowDatas;
    }
}
