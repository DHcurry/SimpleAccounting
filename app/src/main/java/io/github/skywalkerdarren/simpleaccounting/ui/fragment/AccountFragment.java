package io.github.skywalkerdarren.simpleaccounting.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.adapter.AccountAdapter;
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentAccountBinding;
import io.github.skywalkerdarren.simpleaccounting.databinding.FregmentAccountAddBinding;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.ui.activity.AccountAddActivity;
import io.github.skywalkerdarren.simpleaccounting.ui.activity.AccountEditActivity;
import io.github.skywalkerdarren.simpleaccounting.util.ViewModelFactory;
import io.github.skywalkerdarren.simpleaccounting.view_model.AccountViewModel;
import kotlin.Unit;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author darren
 */
public class AccountFragment extends Fragment {
    private RecyclerView mAccountRecyclerView;
    private AccountAdapter mAdapter;
    private FragmentAccountBinding mBinding;
    private AccountViewModel mViewModel;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AccountFragment.
     */
    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_account, container, false);
        mAccountRecyclerView = mBinding.accountRecyclerView;
        mAccountRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mViewModel == null) {
            mViewModel = ViewModelFactory.getInstance(requireActivity().getApplication())
                    .obtainViewModel(this, AccountViewModel.class);
        }
        mBinding.setAccount(mViewModel);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
//        if (mAdapter == null) {
//            mAdapter = new AccountAdapter(mViewModel);
//        }
        mViewModel.getAccounts().observe(getViewLifecycleOwner(),this::updateAdapter);
        updateAdapter(mViewModel.getAccounts().getValue());
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemDragAndSwipeCallback(mAdapter));
        itemTouchHelper.attachToRecyclerView(mAccountRecyclerView);
        mAdapter.enableDragItem(itemTouchHelper);
        // 设置adapter
        mAccountRecyclerView.setAdapter(mAdapter);
        // 这里是设置account点击的关键
        mViewModel.getAccounts().observe(getViewLifecycleOwner(),
                accounts -> mAdapter.setNewList(accounts));
        // 创建之初加载按钮可点击的事件
        AppCompatImageView addButton =  getActivity().findViewById(R.id.add_account);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = (int)v.getX() + v.getWidth()/2;
                int y = (int)v.getY() + v.getHeight()/2;
                Intent intent = AccountAddActivity.newIntent(requireContext(),x,y);
                startActivity(intent);
            }
        });


    }

    private void updateAdapter(List<Account> accountList){
        if(mAdapter == null){
            mAdapter = new AccountAdapter(mViewModel);
            mAdapter.setListener((item,imageView)->mViewModel.getAccount(item.getUuid(),account->{
                Intent intent = AccountEditActivity.newIntent(requireContext(),account,0,0);
                startActivity(intent);
                return Unit.INSTANCE;
            }));
            mAccountRecyclerView.setAdapter(mAdapter);
            mAccountRecyclerView.addItemDecoration(new PinnedHeaderItemDecoration.Builder(2).create());
        }else {
            mAdapter.setNewList(accountList);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mViewModel.start();
    }


}
