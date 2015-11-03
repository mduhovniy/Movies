package info.duhovniy.maxim.movies.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import info.duhovniy.maxim.movies.R;

/**
 * Created by maxduhovniy on 02/11/2015.
 */
public class LoginFragment extends Fragment {

    private EditText mLogin, mPassword;
    private Button mSubmitButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_frag, container, false);
        mLogin = (EditText) rootView.findViewById(R.id.login_text);
        mPassword = (EditText) rootView.findViewById(R.id.password_text);
        mSubmitButton = (Button) rootView.findViewById(R.id.submit_button);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkUser(mLogin.getText().toString(), mPassword.getText().toString())) {

                }
            }
        });

        return rootView;
    }

    private boolean checkUser(String login, String pass) {
        boolean checkResult = false;


        return checkResult;
    }


}