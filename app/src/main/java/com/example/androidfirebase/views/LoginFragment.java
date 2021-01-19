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
import com.example.androidfirebase.core.LoginViewModel;
import com.example.androidfirebase.core.utils.ErrorState;
import com.example.androidfirebase.databinding.FrgLoginBinding;
import com.example.androidfirebase.injects.base.BaseFragment;
import com.example.androidfirebase.injects.base.BaseViewModel;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends BaseFragment {

    protected FrgLoginBinding binding;

    protected LoginViewModel viewModel;

    protected View.OnClickListener onLogin = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            login();
        }
    };

    protected View.OnClickListener toSignup = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.login_to_signup);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FrgLoginBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        binding.setLifecycleOwner(getViewLifecycleOwner());

        binding.loginBtn.setOnClickListener(onLogin);
        binding.signupBtn.setOnClickListener(toSignup);

        String signupTxt = getString(R.string.sign_up_txt);
        binding.signupBtn.setText(Html.fromHtml(signupTxt));

        initObservers();

        return binding.getRoot();
    }

    @Override
    public void initObservers() {

        currentUserObserver = new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                Navigation.findNavController(binding.getRoot()).navigate(R.id.login_to_main);
            }
        };

        errorStateObserver = new Observer<ErrorState>() {
            @Override
            public void onChanged(ErrorState errorHandler) {
                Toast.makeText(requireContext(), errorHandler.message, Toast.LENGTH_SHORT).show();
            }
        };

        loadingStateObserver = new Observer<BaseViewModel.LoadingState>() {
            @Override
            public void onChanged(BaseViewModel.LoadingState loadingStatus) {
                int visibility = loadingStatus == BaseViewModel.LoadingState.LOADING ? View.VISIBLE : View.GONE;
                binding.loader.setVisibility(visibility);
            }
        };

        viewModel.currentUserLiveData.observe(getViewLifecycleOwner(), currentUserObserver);
        viewModel.errorStateLiveData.observe(getViewLifecycleOwner(), errorStateObserver);
        viewModel.loadingStateLiveData.observe(getViewLifecycleOwner(), loadingStateObserver);

    }

    public void login() {
        String email = binding.emailTxtField.getEditText().getText().toString();
        String password = binding.passwordTxtField.getEditText().getText().toString();
        viewModel.login(firebaseAuth, email, password);
    }

    @Override
    public void onDestroyView() {

        viewModel.currentUserLiveData.removeObserver(currentUserObserver);
        viewModel.errorStateLiveData.removeObserver(errorStateObserver);
        viewModel.loadingStateLiveData.removeObserver(loadingStateObserver);

        binding.loginBtn.setOnClickListener(null);
        binding.signupBtn.setOnClickListener(null);

        super.onDestroyView();

        binding = null;
    }
}
