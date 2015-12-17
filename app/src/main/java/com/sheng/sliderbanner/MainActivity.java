package com.sheng.sliderbanner;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sheng.sliderbanner.view.BannerAdapter;
import com.sheng.sliderbanner.view.SliderBanner;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    private List<String> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SliderBanner sliderBanner = (SliderBanner) findViewById(R.id.demo_slider_banner);

        datas = new ArrayList<String>();
        for (int i = 0; i < 3; i++) {
            datas.add("title" + (i + 1));
        }

        sliderBanner.setAdapter(new BannerAdapter<String>(datas) {
            @Override
            public View getView(LayoutInflater inflater, int position) {
                View convertView = inflater.inflate(R.layout.banner, null);
                ImageView imageView = (ImageView) convertView.findViewById(R.id.demo_banner_item_image);
                TextView titleTextView = (TextView) convertView.findViewById(R.id.demo_banner_item_title);

                String data = getData(position);
                imageView.setBackgroundResource(R.mipmap.ic_launcher);
                titleTextView.setText(data);
                return convertView;
            }
        });
        sliderBanner.setDotNum(datas.size());
        sliderBanner.playTo();

    }

}
