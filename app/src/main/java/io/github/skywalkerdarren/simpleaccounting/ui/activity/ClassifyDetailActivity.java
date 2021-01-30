package io.github.skywalkerdarren.simpleaccounting.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.base.BaseFragmentActivity;
import io.github.skywalkerdarren.simpleaccounting.model.entity.TypeAndStats;
import io.github.skywalkerdarren.simpleaccounting.ui.fragment.ClassifyDetailFragment;
import io.github.skywalkerdarren.simpleaccounting.view_model.ClassifyViewModel;

public class ClassifyDetailActivity extends BaseFragmentActivity {
    private static ClassifyViewModel classifyViewModel;

    public static Intent newIntent(Context context, TypeAndStats typeAndStats, ClassifyViewModel model) {
        Log.d("ClassifyDetailActivity", "newIntent: trans obj="+typeAndStats);
        Intent intent = new Intent(context, ClassifyDetailActivity.class);
        intent.putExtra("TypeAndStats",typeAndStats);
        classifyViewModel = model;
        return intent;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @NotNull
    @Override
    public Fragment createFragment() {
        TypeAndStats t = (TypeAndStats) getIntent().getSerializableExtra("TypeAndStats");
        Log.d("ClassifyDetailActivity","the trans obj typeandstats = "+t);
        return ClassifyDetailFragment.newIntance(classifyViewModel,t);
    }
}
