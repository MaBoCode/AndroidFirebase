package com.example.androidfirebase.injects.base;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.androidfirebase.core.utils.ErrorState;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;

public abstract class BaseViewModel extends ViewModel {

    public enum LoadingState {
        LOADING,
        NOT_LOADING
    }

    protected SavedStateHandle savedStateHandle;

    protected MutableLiveData<FirebaseUser> _currentUserLiveData = new MutableLiveData<>();
    public LiveData<FirebaseUser> currentUserLiveData = _currentUserLiveData;

    protected MutableLiveData<LoadingState> _loadingStateLiveData = new MutableLiveData<>();
    public LiveData<LoadingState> loadingStateLiveData = _loadingStateLiveData;

    protected MutableLiveData<ErrorState> _errorStateLiveData = new MutableLiveData<>();
    public LiveData<ErrorState> errorStateLiveData = _errorStateLiveData;

    public class LoadingStateConsumer implements Consumer<Disposable> {

        @Override
        public void accept(Disposable disposable) throws Throwable {
            _loadingStateLiveData.postValue(LoadingState.LOADING);
        }
    }

    public class LoadingStateAction implements Action {

        @Override
        public void run() throws Throwable {
            _loadingStateLiveData.postValue(LoadingState.NOT_LOADING);
        }
    }

    public class ErrorStateConsumer implements Consumer<Throwable> {

        @Override
        public void accept(Throwable throwable) throws Throwable {
            ErrorState errorState = new ErrorState(throwable.getMessage());
            _errorStateLiveData.postValue(errorState);
        }
    }
}
