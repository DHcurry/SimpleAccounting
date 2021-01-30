package io.github.skywalkerdarren.simpleaccounting.ui.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.adapter.AccountBillsAdapter;
import io.github.skywalkerdarren.simpleaccounting.adapter.BillAdapter;
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentAccountEditBinding;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillInfo;
import io.github.skywalkerdarren.simpleaccounting.util.ViewModelFactory;
import io.github.skywalkerdarren.simpleaccounting.view_model.AccountEditViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountEditFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ACCOUNT = "account";
    public static final String EXTRA_CENTER_X = "io.github.skywalkerdarren.simpleaccounting.centerX";
    public static final String EXTRA_CENTER_Y = "io.github.skywalkerdarren.simpleaccounting.centerY";

    private FragmentAccountEditBinding mBinding;
    private Account mAccount;
    private AccountEditViewModel mViewModel;
    private AccountBillsAdapter billAdapter;

//    private ImageView photo;
    private EditText text;
    private RecyclerView accountBillRecycleView;


    public AccountEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountEditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountEditFragment newInstance(Account account, int param1, int param2) {
        AccountEditFragment fragment = new AccountEditFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ACCOUNT,account);
        args.putString(EXTRA_CENTER_X, String.valueOf(param1));
        args.putString(EXTRA_CENTER_Y, String.valueOf(param2));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAccount = (Account) getArguments().getSerializable(ARG_ACCOUNT);
        }
        setHasOptionsMenu(true);

    }

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_account_edit,container,false);

        // 获取viewModel
        mViewModel = ViewModelFactory.getInstance(requireActivity().getApplication())
                .obtainViewModel(this,AccountEditViewModel.class);
        // 将account传入
        mViewModel.setAccount(mAccount);
//        mViewModel.getBills(mAccount.getUuid());
        text = mBinding.balanceEdit;

        // 取出这个recycleView
        accountBillRecycleView = mBinding.accountBillList;
        LinearLayoutManager layout = new LinearLayoutManager(requireActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        accountBillRecycleView.setLayoutManager(layout);
        billAdapter = new AccountBillsAdapter();
        accountBillRecycleView.setAdapter(billAdapter);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mViewModel.getBills(mAccount.getUuid());
//        while(mViewModel.getBillInfos().getValue() == null){
//            mViewModel.getBills(mAccount.getUuid());
//        }
//        updateAdapter(mViewModel.getBillInfos().getValue());
        mBinding.setAccountEdit(mViewModel);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        mBinding.submitEdit.setOnClickListener(listener -> mViewModel.submitChange(mAccount.getUuid(), text.getText().toString()));
        mBinding.del.setOnClickListener(lis->mViewModel.delAccount(mAccount.getUuid()));
        mViewModel.getBills(mAccount.getUuid());
        mViewModel.getBillLiveData().observe(getViewLifecycleOwner(),this::updateAdapter);
    }

//    private void updateAdapter(List<BillInfo> billInfos){
//        Log.d("AccountEditFragment","updateAdapter get billInfos = "+ billInfos);
//        if(billAdapter == null){
//            billAdapter = new AccountBillsAdapter(billInfos);
//            accountBillRecycleView.setAdapter(billAdapter);
//        }else{
//            billAdapter.setNewList(billInfos);
//        }
//    }

    private void updateAdapter(List<BillInfo> billInfoList){
        if(billAdapter == null){
            billAdapter = new AccountBillsAdapter();
            accountBillRecycleView.setAdapter(billAdapter);
        }else{
            billAdapter.setNewList(billInfoList);
        }

    }
}
