package io.github.skywalkerdarren.simpleaccounting.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.databinding.FregmentAccountAddBinding;
import io.github.skywalkerdarren.simpleaccounting.util.ViewModelFactory;
import io.github.skywalkerdarren.simpleaccounting.view_model.AccountViewModel;

public class AccountAddFragment extends Fragment {
    private static final int REQUEST_DATE = 0;
    private static final String ARG_CX = "cx";
    private static final String ARG_CY = "cy";
    private AccountViewModel mViewModel;
    private FregmentAccountAddBinding mBinding;

    private EditText name;
    private EditText hint;
    private EditText balance;
    private Button button;
    private TextView textView;


    public static AccountAddFragment newInstance(int centerX, int centerY) {
        AccountAddFragment fragment = new AccountAddFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CX, centerX);
        args.putInt(ARG_CY, centerY);
        fragment.setArguments(args);
        return fragment;
    }

            @Override
            public void onCreate(@Nullable Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
            }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelFactory.getInstance(requireActivity().getApplication())
                .obtainViewModel(this, AccountViewModel.class);
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fregment_account_add, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        button = getActivity().findViewById(R.id.submit);
        textView = getActivity().findViewById(R.id.suc);
        name = mBinding.name;
        hint = mBinding.hint;
        balance = mBinding.balance;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = String.valueOf(name.getText());
                Log.d("AccountAddFragment","name="+n);
                String h = String.valueOf(hint.getText());
                Log.d("AccountAddFragment","hint="+h);
                String b = String.valueOf(balance.getText());
                Log.d("AccountAddFragment","balance="+b);
                mViewModel.addAccount(n,h,b);
                getActivity().finish();
            }
        });
    }
}
