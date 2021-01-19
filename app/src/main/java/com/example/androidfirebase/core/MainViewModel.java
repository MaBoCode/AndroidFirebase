package com.example.androidfirebase.core;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.SavedStateHandle;

import com.example.androidfirebase.core.utils.RxFirebaseAuth;
import com.example.androidfirebase.injects.base.BaseViewModel;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.rxjava3.functions.Consumer;

public class MainViewModel extends BaseViewModel {

    @ViewModelInject
    public MainViewModel(@Assisted SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
    }

    public void observeAuthState(FirebaseAuth firebaseAuth) {
        RxFirebaseAuth.observableAuthState(firebaseAuth)
                .subscribe(new Consumer<FirebaseAuth>() {
                    @Override
                    public void accept(FirebaseAuth firebaseAuth) throws Throwable {
                        _currentUserLiveData.postValue(firebaseAuth.getCurrentUser());
                    }
                });
    }

}
