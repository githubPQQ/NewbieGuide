package com.pqq.lib;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.pqq.lib.widget.GuideView;

public class GuideDialog extends DialogFragment {

    /**
     * 镂空图层
     */
    private GuideView guideView;

    /**
     * 操作图层
     */
    private View operateView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.Dialog_FullScreen);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view instanceof ViewGroup) {

            if (guideView != null) {
                ((ViewGroup) view).addView(guideView);
            }

            if (guideView != null) {
                ((ViewGroup) view).addView(operateView);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide_layout, container, false);
        return view;
    }

    public void setGuideView(GuideView guideView) {
        this.guideView = guideView;
    }

    public void setOperateView(View operateView) {
        this.operateView = operateView;
    }

    public static class Builder {
        /**
         * 镂空图层
         */
        private GuideView guideView;

        /**
         * 操作图层
         */
        private View operateView;

        public Builder buildGuideView(GuideView guideView) {
            this.guideView = guideView;
            return this;
        }

        public Builder buildOperateView(View operateView) {
            this.operateView = operateView;
            return this;
        }

        public GuideDialog build() {
            GuideDialog guideDialog = new GuideDialog();
            guideDialog.setGuideView(guideView);
            guideDialog.setOperateView(operateView);
            return guideDialog;
        }

    }

}
