package com.sheng.sliderbanner.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.sheng.sliderbanner.view.AutoPlayer.Playable;
import com.sheng.sliderbanner.R;


public class SliderBanner extends RelativeLayout {
    private static final int DEFAULT_INTERVAL = 3000;
    protected int mIdForViewPager;
    protected int mIdForIndicator;
    protected int mTimeInterval;
    private ViewPager mViewPager;
    private BannerAdapter mBannerAdapter;
    private OnPageChangeListener mOnPageChangeListener;
    private PagerIndicator mPagerIndicator;
    private AutoPlayer mAutoPlayer;
    private OnTouchListener mViewPagerOnTouchListener;
    private Playable mGalleryPlayable;

    public SliderBanner(Context context) {
        this(context, null);
    }

    public SliderBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mTimeInterval = DEFAULT_INTERVAL;
        this.mGalleryPlayable = new Playable() {
            public void playTo(int to) {
                SliderBanner.this.mViewPager.setCurrentItem(to, true);
            }

            public void playNext() {
                SliderBanner.this.mViewPager.setCurrentItem(SliderBanner.this.mViewPager.getCurrentItem() + 1, true);
            }

            public void playPrevious() {
                SliderBanner.this.mViewPager.setCurrentItem(SliderBanner.this.mViewPager.getCurrentItem() - 1, true);
            }

            public int getTotal() {
                return SliderBanner.this.mBannerAdapter.getCount();
            }

            public int getCurrent() {
                return SliderBanner.this.mViewPager.getCurrentItem();
            }
        };
        this.mAutoPlayer = new AutoPlayer(this.mGalleryPlayable);
        this.mAutoPlayer.setPlayRecycleMode(AutoPlayer.PlayRecycleMode.repeat_from_start);
        this.mAutoPlayer.setTimeInterval(this.mTimeInterval);

        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.SliderBanner, 0, 0);
        if (arr != null) {
            this.mIdForViewPager = arr.getResourceId(R.styleable.SliderBanner_slider_banner_pager, 0);
            this.mIdForIndicator = arr.getResourceId(R.styleable.SliderBanner_slider_banner_indicator, 0);
            this.mTimeInterval = arr.getInt(R.styleable.SliderBanner_slider_banner_time_interval, this.mTimeInterval);
            arr.recycle();
        }

    }

    public void setAdapter(BannerAdapter adapter) {
        this.mBannerAdapter = adapter;
        this.mViewPager.setAdapter(adapter);
    }

    public void setDotNum(int num) {
        if (this.mPagerIndicator != null) {
            this.mPagerIndicator.setNum(num);
        }
    }

    //TODO
    public void playTo() {
        this.mAutoPlayer.play(mPagerIndicator.getTotal() * 6);
    }

    public void play() {
        this.mAutoPlayer.play();
    }

    public void resume() {
        this.mAutoPlayer.resume();
    }

    public void pause() {
        this.mAutoPlayer.pause();
    }

    public void stop() {
        this.mAutoPlayer.stop();
    }

    public void setTimeInterval(int interval) {
        this.mAutoPlayer.setTimeInterval(interval);
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mOnPageChangeListener = listener;
    }

    public void setViewPagerOnTouchListener(OnTouchListener onTouchListener) {
        this.mViewPagerOnTouchListener = onTouchListener;
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (this.mAutoPlayer != null) {
                    this.mAutoPlayer.pause();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (this.mAutoPlayer != null) {
                    this.mAutoPlayer.resume();
                }
            case MotionEvent.ACTION_MOVE:
        }

        if (this.mViewPagerOnTouchListener != null) {
            this.mViewPagerOnTouchListener.onTouch(this, ev);
        }

        return super.dispatchTouchEvent(ev);
    }

    protected void onFinishInflate() {
        this.mViewPager = (ViewPager) this.findViewById(this.mIdForViewPager);
        this.mPagerIndicator = (DotView) this.findViewById(this.mIdForIndicator);
        this.mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (SliderBanner.this.mOnPageChangeListener != null) {
                    SliderBanner.this.mOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            public void onPageSelected(int position) {
                if (SliderBanner.this.mPagerIndicator != null) {
                    SliderBanner.this.mPagerIndicator.setSelected(SliderBanner.this.mBannerAdapter.getPositionForIndicator(position));
                }

                if (SliderBanner.this.mOnPageChangeListener != null) {
                    SliderBanner.this.mOnPageChangeListener.onPageSelected(position);
                }
            }

            public void onPageScrollStateChanged(int state) {
                if (SliderBanner.this.mOnPageChangeListener != null) {
                    SliderBanner.this.mOnPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
    }
}
