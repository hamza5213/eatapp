package Adapter;

import com.ubereat.world.R;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class MainSliderAdapter extends SliderAdapter {

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {
        switch (position) {
            case 0:
                viewHolder.bindImageSlide(R.drawable.food4);
                break;
            case 1:
                viewHolder.bindImageSlide(R.drawable.food5);
                break;
            case 2:
                viewHolder.bindImageSlide(R.drawable.food6);
                break;
        }
    }
}