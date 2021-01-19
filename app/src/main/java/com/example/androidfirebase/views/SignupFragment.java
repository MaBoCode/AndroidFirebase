package com.example.androidfirebase.views;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.androidfirebase.R;
import com.example.androidfirebase.core.SignupViewModel;
import com.example.androidfirebase.core.utils.ErrorState;
import com.example.androidfirebase.databinding.FrgSignupBinding;
import com.example.androidfirebase.injects.base.BaseFragment;
import com.example.androidfirebase.injects.base.BaseViewModel.LoadingState;
import com.google.firebase.auth.FirebaseUser;

import org.androidannotations.annotations.EFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
@EFragment
public class SignupFragment extends BaseFragment {

    protected FrgSignupBinding binding;

    protected SignupViewModel viewModel;

    protected View.OnClickListener onSignup = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            signup();
        }
    };

    protected View.OnClickListener toLogin = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.signup_to_login);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FrgSignupBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(SignupViewModel.class);

        binding.setLifecycleOwner(getViewLifecycleOwner());

        binding.signupBtn.setOnClickListener(onSignup);
        binding.loginBtn.setOnClickListener(toLogin);

        String loginTxt = getString(R.string.log_in_txt);
        binding.loginBtn.setText(Html.fromHtml(loginTxt));

        initObservers();

        return binding.getRoot();
    }

    @Override
    public void initObservers() {

        currentUserObserver = new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                Navigation.findNavController(binding.getRoot()).navigate(R.id.signup_to_main);
            }
        };

        errorStateObserver = new Observer<ErrorState>() {
            @Override
            public void onChanged(ErrorState errorState) {
                Toast.makeText(requireContext(), errorState.message, Toast.LENGTH_SHORT).show();
            }
        };

        loadingStateObserver = new Observer<LoadingState>() {
            @Override
            public void onChanged(LoadingState loadingState) {
                int visibility = loadingState == LoadingState.LOADING ? View.VISIBLE : View.GONE;
                binding.loader.setVisibility(visibility);
            }
        };

        viewModel.currentUserLiveData.observe(getViewLifecycleOwner(), currentUserObserver);
        viewModel.errorStateLiveData.observe(getViewLifecycleOwner(), errorStateObserver);
        viewModel.loadingStateLiveData.observe(getViewLifecycleOwner(), loadingStateObserver);
    }

    public void signup() {
        String email = binding.emailTxtField.getEditText().getText().toString();
        String password = binding.passwordTxtField.getEditText().getText().toString();
        viewModel.signup(firebaseAuth, email, password);
    }

    @Override
    public void onDestroyView() {

        viewModel.currentUserLiveData.removeObserver(currentUserObserver);
        viewModel.errorStateLiveData.removeObserver(errorStateObserver);
        viewModel.loadingStateLiveData.removeObserver(loadingStateObserver);

        binding.signupBtn.setOnClickListener(null);
        binding.loginBtn.setOnClickListener(null);

        super.onDestroyView();

        binding = null;
    }
}
