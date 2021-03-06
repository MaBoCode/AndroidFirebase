package com.example.androidfirebase.core;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.SavedStateHandle;

import com.example.androidfirebase.core.utils.RxFirebaseAuth;
import com.example.androidfirebase.injects.base.BaseViewModel;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginViewModel extends BaseViewModel {

    @ViewModelInject
    public LoginViewModel(@Assisted SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
    }

    public void login(FirebaseAuth firebaseAuth, String email, String password) {

        RxFirebaseAuth.signInWithEmailAndPassword(firebaseAuth, email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnSubscribe(new LoadingStateConsumer())
                .doFinally(new LoadingStateAction())
                .subscribe(new Consumer<AuthResult>() {
                    @Override
                    public void accept(AuthResult authResult) throws Throwable {
                        _currentUserLiveData.postValue(authResult.getUser());
                    }
                }, new ErrorStateConsumer());
    }

}
