package io.github.skywalkerdarren.simpleaccounting.ui.activity;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import io.github.skywalkerdarren.simpleaccounting.base.BaseFragmentActivity;
import io.github.skywalkerdarren.simpleaccounting.ui.fragment.LoginFragment;

public class LoginActivity extends BaseFragmentActivity {

    @NotNull
    @Override
    public Fragment createFragment() {
        return LoginFragment.newInstance();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }
}
