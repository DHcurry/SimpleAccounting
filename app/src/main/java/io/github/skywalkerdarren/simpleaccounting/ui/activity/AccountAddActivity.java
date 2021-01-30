package io.github.skywalkerdarren.simpleaccounting.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import io.github.skywalkerdarren.simpleaccounting.base.BaseFragmentActivity;
import io.github.skywalkerdarren.simpleaccounting.ui.fragment.AccountAddFragment;

public class AccountAddActivity extends BaseFragmentActivity {
    public static final String EXTRA_CENTER_X = "io.github.skywalkerdarren.simpleaccounting.centerX";
    public static final String EXTRA_CENTER_Y = "io.github.skywalkerdarren.simpleaccounting.centerY";
//    private static final String EXTRA_ACCOUNT = "accountAdd";

    public static Intent newIntent(Context context, int x, int y) {
        Intent intent = new Intent(context, AccountAddActivity.class);
        intent.putExtra(EXTRA_CENTER_X, x);
        intent.putExtra(EXTRA_CENTER_Y, y);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @NotNull
    @Override
    public Fragment createFragment() {
        int cx = getIntent().getIntExtra(EXTRA_CENTER_X, 0);
        int cy = getIntent().getIntExtra(EXTRA_CENTER_Y, 0);
        return AccountAddFragment.newInstance(cx, cy);
//        return AccountAddFragment.newInstance();
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }





}
