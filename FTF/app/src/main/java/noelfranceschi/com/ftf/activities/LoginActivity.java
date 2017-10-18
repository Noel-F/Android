package noelfranceschi.com.ftf.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import noelfranceschi.com.ftf.R;
import noelfranceschi.com.ftf.constants.Constants;
import noelfranceschi.com.ftf.data.AuthService;

/**
 * Created by noelfranceschi on 10/13/17.
 */

public class LoginActivity extends AppCompatActivity {

    //: UI References.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private String authToken;
    private boolean isLoggedIn = false;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //: Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    private void attemptLogin() {


        //: Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        //: Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //: Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        //: Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            //: There was an error; don't attempt login and focus the first
            //: form field with an error.
            focusView.requestFocus();
        } else {
            //: Show a progress spinner, and kick off a background task to
            //: perform the user login attempt.
            showProgress(true);

            // Use out auth service api calls.


            final LoginInterface loginInterface = new LoginInterface() {
                @Override
                public void success(Boolean success) {
                    if (success) {
                        authToken = AuthService.getInstance().getAuthToken();
                        isLoggedIn = true;
                        prefs.edit().putString(Constants.AUTH_TOKEN, authToken).apply();
                        prefs.edit().putBoolean(Constants.IS_LOGGED_IN, true).apply();
                        finish();
                    }
                }
            };

            RegisterInterface registerInterface = new RegisterInterface() {
                @Override
                public void success(Boolean success) {
                    AuthService.getInstance().loginUser(email, password, getBaseContext(), loginInterface);
                }
            };

            AuthService.getInstance().registerUser(email, password, getBaseContext(), registerInterface);

        }
    }

    public interface RegisterInterface {
        void success(Boolean success);
    }

    public interface LoginInterface {
        void success(Boolean success);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        //: On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        //: for very easy animations. If available, use these APIs to fade-in
        //: the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {

            //: The ViewPropertyAnimator APIs are not available, so simply show
            //: and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

