package com.pqq.lib;

import android.graphics.Rect;
import android.view.View;

/**
 * 镂空View
 */
public class HellowData {

    public View hellowInfo;

    public Rect targetRect;

    public int padding;

    public View getHellowInfo() {
        return hellowInfo;
    }

    public void setHellowInfo(View hellowInfo) {
        this.hellowInfo = hellowInfo;
    }

    public Rect getTargetRect() {
        return targetRect;
    }

    public void setTargetRect(Rect targetRect) {
        this.targetRect = targetRect;
    }
}
