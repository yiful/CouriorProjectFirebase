package com.yiful.couriorprojectfirebase.model;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.yiful.couriorprojectfirebase.presenter.LoginRegisterPresenter;
import com.yiful.couriorprojectfirebase.view.MainActivityView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Yifu on 12/16/2017.
 */

public class LoginRegisterImplementation implements LoginRegisterPresenter {
    private static final int RC_SIGN_IN = 123;
    private MainActivityView mainActivityView;
    private Activity activity;

    public LoginRegisterImplementation(MainActivityView mainActivityView, Activity activity) {
        this.mainActivityView = mainActivityView;
        this.activity = activity;
    }

    @Override
    public void loginRegister() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build());
        activity.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(), RC_SIGN_IN);

    }

    @Override
    public void logout() {
        AuthUI.getInstance()
                .signOut((FragmentActivity) activity)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                 //       Toast.makeText(activity, "You have logged out!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
