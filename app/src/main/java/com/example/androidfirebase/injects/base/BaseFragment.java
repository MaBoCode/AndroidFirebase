package com.example.androidfirebase.injects.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.androidfirebase.core.utils.ErrorState;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public abstract class BaseFragment extends Fragment {

    protected FirebaseAuth firebaseAuth;

    protected Observer<FirebaseUser> currentUserObserver;
    protected Observer<ErrorState> errorStateObserver;
    protected Observer<BaseViewModel.LoadingState> loadingStateObserver;

    public abstract void initObservers();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
    }
}
