package com.sheng.sliderbanner.view;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class BannerAdapter<T> extends PagerAdapter {
    protected List<T> datas;

    public BannerAdapter(List<T> datas) {
        this.datas = datas;
    }

    public abstract View getView(LayoutInflater inflater, int position);

    public T getData(int position) {
        if (null == datas || datas.size() == 0)
            return null;
        return datas.get(getPositionForIndicator(position));
    }

    public int getPositionForIndicator(int position) {
        if (null == datas || datas.size() == 0)
            return 0;
        else return position % datas.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = this.getView(LayoutInflater.from(container.getContext()), position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        if (null == datas || datas.size() == 0)
            return 0;
        else return Integer.MAX_VALUE;
    }
}
