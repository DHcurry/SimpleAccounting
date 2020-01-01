package io.github.skywalkerdarren.simpleaccounting.ui.fragment;


import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.joda.time.DateTime;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.adapter.ClassifyAdapter;
import io.github.skywalkerdarren.simpleaccounting.base.BaseFragment;
import io.github.skywalkerdarren.simpleaccounting.databinding.EmptyStatsBinding;
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentClassifyBinding;
import io.github.skywalkerdarren.simpleaccounting.util.ViewModelFactory;
import io.github.skywalkerdarren.simpleaccounting.view_model.ClassifyViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassifyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassifyFragment extends BaseFragment {

    private static final int REQUEST_PERIOD = 0;
    private ClassifyViewModel mViewModel;
    private ViewPager mViewPager;
    private ClassifyAdapter mClassifyAdapter;
    private ImageView mToIncomeImageView;
    private ImageView mToExpenseImageView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ClassifyFragment.
     */
    public static ClassifyFragment newInstance() {
        ClassifyFragment fragment = new ClassifyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentClassifyBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_classify, container, false);
        ViewModelFactory factory = ViewModelFactory.getInstance(requireActivity().getApplication());
        mViewModel = ViewModelProviders.of(this, factory).get(ClassifyViewModel.class);
        mViewModel.setExpense(true);
        binding.setLifecycleOwner(this);

        mViewPager = binding.classifyViewPager;
        mToIncomeImageView = binding.toIncomeImageView;
        mToExpenseImageView = binding.toExpenseImageView;
        mToIncomeImageView.setVisibility(View.VISIBLE);
        mToExpenseImageView.setVisibility(View.VISIBLE);


        mToIncomeImageView.setOnClickListener(view -> mViewPager.setCurrentItem(0));

        mToExpenseImageView.setOnClickListener(view -> mViewPager.setCurrentItem(1));

        binding.backImageView.setOnClickListener(view -> {
            mViewModel.back();
            setStatsData(mViewPager.getCurrentItem() == 1, mClassifyAdapter);
            updateUI();
            mViewModel.start();
        });
        binding.moreImageView.setOnClickListener(view -> {
            mViewModel.more();
            setStatsData(mViewPager.getCurrentItem() == 1, mClassifyAdapter);
            updateUI();
            mViewModel.start();
        });
        binding.customImageView.setOnClickListener(view -> customDialog());
        binding.dateTextView.setOnClickListener(view -> customDialog());

        binding.classifyRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        mClassifyAdapter = new ClassifyAdapter();
        mViewModel.getStatsList().observe(this, typeStats -> mClassifyAdapter.setNewData(typeStats));
        mClassifyAdapter.setDuration(100);
        mClassifyAdapter.setEmptyView(emptyView());
        binding.classifyRecyclerView.setAdapter(mClassifyAdapter);
        binding.setClassify(mViewModel);
        mViewModel.start();
        return binding.getRoot();
    }

    private void customDialog() {
        PeriodDialogFragment periodDialog = PeriodDialogFragment
                .newInstance(mViewModel.getStart(), mViewModel.getEnd());
        periodDialog.setTargetFragment(this, REQUEST_PERIOD);
        periodDialog.show(requireFragmentManager(), "periodDialog");
    }

    @Override
    protected void updateUI() {
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return PieChartFragment.newInstance(
                                mViewModel.getStart(), mViewModel.getEnd(), false);
                    case 1:
                        return PieChartFragment.newInstance(
                                mViewModel.getStart(), mViewModel.getEnd(), true);
                    default:
                        throw new IllegalArgumentException("no this item");
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
        mViewPager.clearOnPageChangeListeners();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        setStatsData(false, mClassifyAdapter);
                        break;
                    case 1:
                        setStatsData(true, mClassifyAdapter);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(mViewModel.isExpense() ? 1 : 0);
        mClassifyAdapter.notifyDataSetChanged();
    }

    /**
     * @param toIncome true则转到收入页，当前为支出页
     */
    private void changeImageView(boolean toIncome) {
        Animator disappear = AnimatorInflater.loadAnimator(requireContext(), toIncome ? R.animator.to_left_disappear : R.animator.to_right_disappear);
        Animator appear = AnimatorInflater.loadAnimator(requireContext(), toIncome ? R.animator.to_left_appear : R.animator.to_right_appear);
        disappear.setTarget(toIncome ? mToExpenseImageView : mToIncomeImageView);
        appear.setTarget(toIncome ? mToIncomeImageView : mToExpenseImageView);

        AnimatorSet set = new AnimatorSet();
        set.playSequentially(disappear, appear);
        set.start();
    }

    private void setStatsData(boolean t, ClassifyAdapter adapter) {
        changeImageView(t);
        mViewModel.setExpense(t);
        mViewModel.getStatsList().observe(this, adapter::setNewData);
        adapter.openLoadAnimation(t ?
                BaseQuickAdapter.SLIDEIN_RIGHT : BaseQuickAdapter.SLIDEIN_LEFT);
    }

    private View emptyView() {
        return EmptyStatsBinding.inflate(LayoutInflater.from(requireContext())).getRoot();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_PERIOD:
                DateTime start = (DateTime) data.getSerializableExtra(PeriodDialogFragment.EXTRA_START_DATE);
                DateTime end = (DateTime) data.getSerializableExtra(PeriodDialogFragment.EXTRA_END_DATE);
                mViewModel.setDate(start, end);
                updateUI();
                break;
            default:
                break;
        }
    }
}
