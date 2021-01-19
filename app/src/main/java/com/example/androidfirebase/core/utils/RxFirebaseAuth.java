package com.example.androidfirebase.core.utils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeEmitter;
import io.reactivex.rxjava3.core.MaybeOnSubscribe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.functions.Cancellable;

public class RxFirebaseAuth {

    public static Maybe<AuthResult> createUserWithEmailAndPassword(FirebaseAuth firebaseAuth, String email, String password) {
        return Maybe.create(new MaybeOnSubscribe<AuthResult>() {
            @Override
            public void subscribe(@NonNull MaybeEmitter<AuthResult> emitter) throws Throwable {
                Task<AuthResult> task = firebaseAuth.createUserWithEmailAndPassword(email, password);
                RxHandler.assignOnTask(emitter, task);
            }
        });
    }

    public static Maybe<AuthResult> signInWithEmailAndPassword(FirebaseAuth firebaseAuth, String email, String password) {
        return Maybe.create(new MaybeOnSubscribe<AuthResult>() {
            @Override
            public void subscribe(@NonNull MaybeEmitter<AuthResult> emitter) throws Throwable {
                Task<AuthResult> task = firebaseAuth.signInWithEmailAndPassword(email, password);
                RxHandler.assignOnTask(emitter, task);
            }
        });
    }

    public static Observable<FirebaseAuth> observableAuthState(FirebaseAuth firebaseAuth) {
        return Observable.create(new ObservableOnSubscribe<FirebaseAuth>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<FirebaseAuth> emitter) throws Throwable {
                FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@androidx.annotation.NonNull FirebaseAuth firebaseAuth) {
                        emitter.onNext(firebaseAuth);
                    }
                };

                firebaseAuth.addAuthStateListener(authStateListener);
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Throwable {
                        firebaseAuth.removeAuthStateListener(authStateListener);
                    }
                });
            }
        });
    }

}
