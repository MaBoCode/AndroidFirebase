package com.example.androidfirebase.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.androidfirebase.core.MainViewModel;
import com.example.androidfirebase.databinding.FrgMainBinding;
import com.example.androidfirebase.injects.base.BaseFragment;
import com.google.firebase.auth.FirebaseUser;

public class MainFragment extends BaseFragment {

    protected FrgMainBinding binding;

    protected MainViewModel viewModel;

    protected View.OnClickListener onSignout = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            firebaseAuth.signOut();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FrgMainBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        binding.setLifecycleOwner(getViewLifecycleOwner());

        binding.logoutBtn.setOnClickListener(onSignout);

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        binding.setUser(currentUser);

        viewModel.observeAuthState(firebaseAuth);

        initObservers();

        return binding.getRoot();
    }

    @Override
    public void initObservers() {

        currentUserObserver = new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser == null) {
                    Navigation.findNavController(binding.getRoot()).navigateUp();
                }
            }
        };

        viewModel.currentUserLiveData.observe(getViewLifecycleOwner(), currentUserObserver);

    }

    @Override
    public void onDestroyView() {

        viewModel.currentUserLiveData.removeObserver(currentUserObserver);
        viewModel.onCleared();

        binding.logoutBtn.setOnClickListener(null);

        super.onDestroyView();

        binding = null;
    }
}
