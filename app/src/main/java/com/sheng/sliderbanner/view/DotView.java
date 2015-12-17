package com.sheng.sliderbanner.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.sheng.sliderbanner.R;

public class DotView extends LinearLayout implements PagerIndicator {
    private int mLittleDotSize = -2;
    private int mDotSpan = 36;
    private int mCurrent = 0;
    private int mTotal = 0;
    private int mSelectedColor = -13141010;
    private int mUnSelectedColor = -3813669;
    private float mDotRadius = 6.0F;
    private DotView.OnDotClickHandler mOnDotClickHandler;
    private OnClickListener mDotClickHandler = new OnClickListener() {
        public void onClick(View view) {
            if (view instanceof DotView.LittleDot && null != DotView.this.mOnDotClickHandler) {
                DotView.this.mOnDotClickHandler.onDotClick(((DotView.LittleDot) view).getIndex());
            }
        }
    };

    public DotView(Context context) {
        this(context, null);
    }

    public DotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setGravity(Gravity.CENTER_HORIZONTAL);
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.DotView, 0, 0);
        if (arr != null) {
            this.mDotRadius = arr.getDimension(R.styleable.DotView_dot_radius, this.mDotRadius);
            this.mDotSpan = (int) arr.getDimension(R.styleable.DotView_dot_span, (float) this.mDotSpan);
            this.mSelectedColor = arr.getColor(R.styleable.DotView_dot_selected_color, this.mSelectedColor);
            this.mUnSelectedColor = arr.getColor(R.styleable.DotView_dot_unselected_color, this.mUnSelectedColor);
            arr.recycle();
        }

        this.mLittleDotSize = (int) ((float) (this.mDotSpan / 2) + this.mDotRadius * 2.0F);
    }

    public final void setNum(int num) {
        if (num >= 0) {
            this.mTotal = num;
            this.removeAllViews();
            this.setOrientation(LinearLayout.HORIZONTAL);

            for (int i = 0; i < num; ++i) {
                DotView.LittleDot dot = new DotView.LittleDot(this.getContext(), i);
                if (i == 0) {
                    dot.setColor(this.mSelectedColor);
                } else {
                    dot.setColor(this.mUnSelectedColor);
                }

                dot.setLayoutParams(new LayoutParams(this.mLittleDotSize, (int) this.mDotRadius * 2, 1.0F));
                dot.setClickable(true);
                dot.setOnClickListener(this.mDotClickHandler);
                this.addView(dot);
            }

        }
    }

    public int getTotal() {
        return this.mTotal;
    }

    public int getCurrentIndex() {
        return this.mCurrent;
    }

    public void setOnDotClickHandler(DotView.OnDotClickHandler handler) {
        this.mOnDotClickHandler = handler;
    }

    public final void setSelected(int index) {
        if (index < this.getChildCount() && index >= 0 && this.mCurrent != index) {
            if (this.mCurrent < this.getChildCount() && this.mCurrent >= 0) {
                ((DotView.LittleDot) this.getChildAt(this.mCurrent)).setColor(this.mUnSelectedColor);
            }

            ((DotView.LittleDot) this.getChildAt(index)).setColor(this.mSelectedColor);
            this.mCurrent = index;
        }
    }

    public void setSelectedColor(int color) {
        if (this.mSelectedColor != color) {
            this.mSelectedColor = color;
            this.invalidate();
        }

    }

    public void setColor(int selectedColor, int unSelectedColor) {
        if (this.mSelectedColor != selectedColor || this.mUnSelectedColor != unSelectedColor) {
            this.mSelectedColor = selectedColor;
            this.mUnSelectedColor = unSelectedColor;
            this.invalidate();
        }

    }

    public void setUnSelectedColor(int color) {
        if (this.mUnSelectedColor != color) {
            this.mSelectedColor = color;
            this.invalidate();
        }

    }

    private class LittleDot extends View {
        private int mColor;
        private int mIndex;
        private Paint mPaint = new Paint();

        public LittleDot(Context context, int index) {
            super(context);
            this.mPaint.setAntiAlias(true);
            this.mIndex = index;
        }

        public int getIndex() {
            return this.mIndex;
        }

        public void setColor(int color) {
            if (color != this.mColor) {
                this.mColor = color;
                this.invalidate();
            }
        }

        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            this.mPaint.setColor(this.mColor);
            canvas.drawCircle((float) (DotView.this.mLittleDotSize / 2), DotView.this.mDotRadius, DotView.this.mDotRadius, this.mPaint);
        }
    }

    public interface OnDotClickHandler {
        void onDotClick(int var1);
    }
}
