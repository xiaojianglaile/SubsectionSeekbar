package com.jym.sandbox.seekbar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.jym.sandbox.R;
import com.jym.sandbox.seekbar.drawable.SubsectionDrawable;

/**
 * Created by Jimmy on 2016/5/19 0019.
 */
public class SubsectionSeekBar extends LinearLayout {

    private final int DEFAULT_MAX = 100;
    private final int DEFAULT_PROGRESS = 0;
    private final int DEFAULT_DOTS = 4;
    private final boolean DEFAULT_IS_DISPLAY = false;

    private Context context;
    private SeekBar sbSubsection;
    private OnSeekBarChangeListener onSeekBarChangeListener;
    private int max;
    private int progress;
    private int dots;
    private int selectIndex;
    private int unProgressLineColor;
    private int unProgressCircleColor;
    private Drawable thumbDrawable;
    private boolean isDisplay; // 是否为展示

    public SubsectionSeekBar(Context context) {
        this(context, null);
    }

    public SubsectionSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SubsectionSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SubsectionSeekBar);
        max = array.getInteger(R.styleable.SubsectionSeekBar_max, DEFAULT_MAX);
        progress = array.getInteger(R.styleable.SubsectionSeekBar_progress, DEFAULT_PROGRESS);
        dots = array.getInteger(R.styleable.SubsectionSeekBar_dots, DEFAULT_DOTS);
        unProgressLineColor = array.getColor(R.styleable.SubsectionSeekBar_unProgressLineColor, Color.parseColor("#97E4F6"));
        unProgressCircleColor = array.getColor(R.styleable.SubsectionSeekBar_unProgressCircleColor, Color.parseColor("#2B2B2B"));
        thumbDrawable = array.getDrawable(R.styleable.SubsectionSeekBar_thumb);
        isDisplay = array.getBoolean(R.styleable.SubsectionSeekBar_isDisplay, DEFAULT_IS_DISPLAY);
        if (thumbDrawable == null) {
            thumbDrawable = context.getResources().getDrawable(R.drawable.bg_subsection_thumb);
        }
        if (progress > max) {
            progress = max;
        }
        if (dots > max) {
            throw new IllegalArgumentException("SubsectionSeekBar dots must be less than max");
        }
        if (max % (dots - 1) != 0) {
            throw new IllegalArgumentException("SubsectionSeekBar max must be divisible by (dots-1)");
        }
        initView();
    }

    private void initView() {
        LinearLayout view = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.view_subsection_seekbar, this);
        sbSubsection = (SeekBar) view.findViewById(R.id.sbSeekBar);
        initSeekBar();
    }

    private void initSeekBar() {
        sbSubsection.setMax(max);
        sbSubsection.setProgress(progress);
        sbSubsection.setThumb(thumbDrawable);
        sbSubsection.setProgressDrawable(new SubsectionDrawable(sbSubsection, dots, unProgressLineColor, unProgressCircleColor));
        if (!isDisplay) {
            sbSubsection.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (onSeekBarChangeListener != null) {
                        onSeekBarChangeListener.onProgressChanged(seekBar, progress, fromUser);
                    }
                    SubsectionSeekBar.this.progress = progress;
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                    if (onSeekBarChangeListener != null) {
                        onSeekBarChangeListener.onStartTrackingTouch(seekBar);
                    }
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                    float subsectionValue = max / (dots - 1);
                    float halfOfSubsectionValue = subsectionValue / 2;
                    if (progress > max - halfOfSubsectionValue) {
                        seekBar.setProgress(max);
                        selectIndex = dots - 1;
                        if (onSeekBarChangeListener != null) {
                            onSeekBarChangeListener.onStopTrackingTouch(seekBar, selectIndex);
                        }
                        return;
                    }
                    if (progress < halfOfSubsectionValue) {
                        seekBar.setProgress(0);
                        selectIndex = 0;
                        if (onSeekBarChangeListener != null) {
                            onSeekBarChangeListener.onStopTrackingTouch(seekBar, selectIndex);
                        }
                        return;
                    }
                    float accumulatedValue = 0;
                    int recordingIndex = 0;
                    while (progress > accumulatedValue) {
                        accumulatedValue += subsectionValue;
                        recordingIndex++;
                    }
                    if (progress < accumulatedValue - halfOfSubsectionValue) {
                        seekBar.setProgress(Math.round(accumulatedValue - subsectionValue));
                        recordingIndex--;
                    } else {
                        seekBar.setProgress(Math.round(accumulatedValue));
                    }
                    selectIndex = recordingIndex;
                    if (onSeekBarChangeListener != null) {
                        onSeekBarChangeListener.onStopTrackingTouch(seekBar, selectIndex);
                    }
                }
            });
        } else {
            sbSubsection.setEnabled(false);
        }
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener onSeekBarChangeListener) {
        this.onSeekBarChangeListener = onSeekBarChangeListener;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
        sbSubsection.setMax(max);
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        sbSubsection.setProgress(progress);
    }

    public int getDots() {
        return dots;
    }

    public void setDots(int dots) {
        this.dots = dots;
    }

    public int getUnProgressLineColor() {
        return unProgressLineColor;
    }

    public void setUnProgressLineColor(int unProgressLineColor) {
        this.unProgressLineColor = unProgressLineColor;
    }

    public int getUnProgressCircleColor() {
        return unProgressCircleColor;
    }

    public void setUnProgressCircleColor(int unProgressCircleColor) {
        this.unProgressCircleColor = unProgressCircleColor;
    }

    public void setThumb(Drawable thumb) {
        this.thumbDrawable = thumb;
    }

    public Drawable getThumb() {
        return thumbDrawable;
    }

    public void setSelectedIndex(int index) {
        if (index >= dots) {
            throw new IllegalArgumentException("index must less than (dots-1)");
        }
        selectIndex = index;
        float subsectionValue = max / (dots - 1);
        float halfOfSubsectionValue = subsectionValue / 2;
        if (subsectionValue * index > max - halfOfSubsectionValue) {
            sbSubsection.setProgress(max);
        } else {
            sbSubsection.setProgress(Math.round(subsectionValue * index));
        }
    }

    public int getSelectedIndex() {
        return selectIndex;
    }

    public interface OnSeekBarChangeListener {
        void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser);

        void onStartTrackingTouch(SeekBar seekBar);

        void onStopTrackingTouch(SeekBar seekBar, int selectIndex);
    }
}
