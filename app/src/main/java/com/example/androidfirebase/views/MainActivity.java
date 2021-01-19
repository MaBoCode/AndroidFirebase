package com.example.androidfirebase.views;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.example.androidfirebase.R;
import com.example.androidfirebase.databinding.ActMainBinding;
import com.example.androidfirebase.injects.base.BaseActivity;

import org.androidannotations.annotations.EActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
@EActivity
public class MainActivity extends BaseActivity {

    protected ActMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.act_main);
    }

    @Override
    public void onBackPressed() {
        finishAfterTransition();
    }
}