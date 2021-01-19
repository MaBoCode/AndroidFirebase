package com.example.androidfirebase.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.androidfirebase.R;
import com.example.androidfirebase.databinding.FrgSplashscreenBinding;
import com.example.androidfirebase.injects.base.BaseFragment;
import com.google.firebase.auth.FirebaseUser;

import org.androidannotations.annotations.EFragment;

import java.util.Timer;
import java.util.TimerTask;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
@EFragment
public class SplashScreenFragment extends BaseFragment {

    protected FrgSplashscreenBinding binding;

    static class GoToFirstFragmentTask extends TimerTask {

        protected NavController navController;
        protected int action;

        public GoToFirstFragmentTask(@NonNull NavController navController, int action) {
            this.navController = navController;
            this.action = action;
        }

        @Override
        public void run() {
            navController.navigate(action);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FrgSplashscreenBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        Timer timer = new Timer();

        NavController navController = Navigation.findNavController(requireActivity(), R.id.frg_nav_host);
        GoToFirstFragmentTask toFirstFragmentTask;

        if (currentUser == null) {
            toFirstFragmentTask = new GoToFirstFragmentTask(navController, R.id.splashscreen_to_signup);
        } else {
            toFirstFragmentTask = new GoToFirstFragmentTask(navController, R.id.splashscreen_to_main);
        }

        timer.schedule(toFirstFragmentTask, 400);

        return binding.getRoot();
    }

    @Override
    public void initObservers() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
