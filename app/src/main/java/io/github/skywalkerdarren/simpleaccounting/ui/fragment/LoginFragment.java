package io.github.skywalkerdarren.simpleaccounting.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.databinding.LoginBinding;
import io.github.skywalkerdarren.simpleaccounting.ui.activity.LoginActivity;
import io.github.skywalkerdarren.simpleaccounting.util.ViewModelFactory;
import io.github.skywalkerdarren.simpleaccounting.view_model.AccountViewModel;
import io.github.skywalkerdarren.simpleaccounting.view_model.LoginViewModel;

public class LoginFragment extends Fragment {

    private LoginBinding mBinding;
    private LoginViewModel mViewModel;

    private Button button;
    private EditText username;
    private EditText password;

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
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
                .obtainViewModel(this, LoginViewModel.class);
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.login, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        button = getActivity().findViewById(R.id.submit);
        username = mBinding.username;
        password = mBinding.password;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String un = String.valueOf(username.getText());
                String pw = String.valueOf(password.getText());
                mViewModel.login(un,pw);
            }
        });
    }


}
