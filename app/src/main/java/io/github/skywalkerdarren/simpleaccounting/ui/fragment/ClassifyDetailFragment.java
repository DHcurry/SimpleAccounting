package io.github.skywalkerdarren.simpleaccounting.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.adapter.AccountBillsAdapter;
import io.github.skywalkerdarren.simpleaccounting.databinding.ClassifyDetailBinding;
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillInfo;
import io.github.skywalkerdarren.simpleaccounting.model.entity.TypeAndStats;
import io.github.skywalkerdarren.simpleaccounting.ui.activity.ClassifyDetailActivity;
import io.github.skywalkerdarren.simpleaccounting.util.ViewModelFactory;
import io.github.skywalkerdarren.simpleaccounting.view_model.ClassifyViewModel;

public class ClassifyDetailFragment extends Fragment {
    private static TypeAndStats typeAndStats;

    private ClassifyDetailBinding mBinding;
    private static ClassifyViewModel mViewModel;
    private RecyclerView recyclerView;
    private AccountBillsAdapter adapter;

    public static ClassifyDetailFragment newIntance(ClassifyViewModel model, TypeAndStats typeAndStats){
        ClassifyDetailFragment fragment = new ClassifyDetailFragment();
//        Bundle args = new Bundle();
//        args.putSerializable("TypeAndStats",typeAndStats);
//        fragment.setArguments(args);
        ClassifyDetailFragment.typeAndStats = typeAndStats;
        mViewModel = model;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(getArguments() != null){
//            typeAndStats = (TypeAndStats) getArguments().getSerializable("TypeAndStats");
//        }
        setHasOptionsMenu(true);
        Log.d("ClassifyDetailFragment","the trans obj = "+typeAndStats);
    }

    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.classify_detail,container,false);
        mBinding.setLifecycleOwner(this);
        recyclerView = mBinding.classifyAccountBillList;
        mViewModel.setTypeAndStats(typeAndStats);
        LinearLayoutManager layout = new LinearLayoutManager(requireActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(layout);
        adapter = new AccountBillsAdapter();
        recyclerView.setAdapter(adapter);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel.getTypeBill(typeAndStats);
        mViewModel.getBillList().observe(getViewLifecycleOwner(),this::updateAdapter);
    }

    private void updateAdapter(List<BillInfo> billInfoList){
        adapter.setNewList(billInfoList);
    }
}
