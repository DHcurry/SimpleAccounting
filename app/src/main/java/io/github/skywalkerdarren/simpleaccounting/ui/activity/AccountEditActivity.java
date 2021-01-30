package io.github.skywalkerdarren.simpleaccounting.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import io.github.skywalkerdarren.simpleaccounting.base.BaseFragmentActivity;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;
import io.github.skywalkerdarren.simpleaccounting.ui.fragment.AccountEditFragment;
import io.github.skywalkerdarren.simpleaccounting.ui.fragment.BillEditFragment;

public class AccountEditActivity extends BaseFragmentActivity {
    public static final String EXTRA_CENTER_X = "io.github.skywalkerdarren.simpleaccounting.centerX";
    public static final String EXTRA_CENTER_Y = "io.github.skywalkerdarren.simpleaccounting.centerY";
    private static final String EXTRA_ACCOUNT = "account";

    public static Intent newIntent(Context context, @Nullable Account account, int x, int y) {
        Intent intent = new Intent(context, AccountEditActivity.class);
        intent.putExtra(EXTRA_ACCOUNT, account);
        intent.putExtra(EXTRA_CENTER_X, x);
        intent.putExtra(EXTRA_CENTER_Y, y);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public Fragment createFragment() {
        Account account = (Account) getIntent().getSerializableExtra(EXTRA_ACCOUNT);
        int cx = getIntent().getIntExtra(EXTRA_CENTER_X, 0);
        int cy = getIntent().getIntExtra(EXTRA_CENTER_Y, 0);
        return AccountEditFragment.newInstance(account, cx, cy);
    }
}
