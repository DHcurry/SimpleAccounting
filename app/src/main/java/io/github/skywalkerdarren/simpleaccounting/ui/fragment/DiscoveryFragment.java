package io.github.skywalkerdarren.simpleaccounting.ui.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.base.BaseFragment;
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentDiscoveryBinding;
import io.github.skywalkerdarren.simpleaccounting.ui.activity.MyAccountActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscoveryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoveryFragment extends BaseFragment {
    private LinearLayout mDotLayout;
    private ArrayList<ImageView> mImageViews;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DiscoveryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscoveryFragment newInstance() {
        Bundle args = new Bundle();
        DiscoveryFragment fragment = new DiscoveryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentDiscoveryBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_discovery, container, false);

        binding.myAccount.setOnClickListener(view -> {
            Intent intent = MyAccountActivity.newIntent(getContext());
            startActivity(intent);
        });

        ViewPager viewPager = binding.showPager;
        mDotLayout = binding.dotLayout;
        setImage();
        mDotLayout.getChildAt(0).setEnabled(true);
        viewPager.setAdapter(new DiscoverAdapter());
        viewPager.addOnPageChangeListener(new DiscoverListener());
        viewPager.setOffscreenPageLimit(2);
        return binding.getRoot();
    }

    private void setImage() {
        final int[] productPhotos = {R.drawable.bg1,
                R.drawable.bg3, R.drawable.bg2};
        mImageViews = new ArrayList<>(productPhotos.length);
        for (int i : productPhotos) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(i);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageViews.add(imageView);

            View view = new View(getContext());
            view.setBackgroundResource(R.drawable.indicator);
            view.setEnabled(false);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30, 30);
            if (i != productPhotos[0]) {
                params.leftMargin = 10;
            }
            mDotLayout.addView(view, params);
        }
    }

    @Override
    protected void updateUI() {

    }

    private class DiscoverListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < mImageViews.size(); i++) {
                mDotLayout.getChildAt(i).setEnabled(false);
                if (position == i) {
                    mDotLayout.getChildAt(i).setEnabled(true);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private class DiscoverAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mImageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mImageViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageViews.get(position));
            return mImageViews.get(position);
        }
    }
}
