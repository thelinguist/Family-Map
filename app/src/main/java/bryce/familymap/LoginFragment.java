package bryce.familymap;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.concurrent.ExecutionException;
import java.util.zip.Inflater;

/**
 * Created by Bryce on 11/21/16.
 */

public class LoginFragment extends Fragment {
    private EditText setUsername;
    private EditText setHost;
    private EditText setPassword;
    private EditText setPort;
    private Button loginButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        setUsername = (EditText) view.findViewById(R.id.username); //do this for all edit texts
        setPassword = (EditText) view.findViewById(R.id.password);
        setPort = (EditText) view.findViewById(R.id.port);
        setHost = (EditText) view.findViewById(R.id.host);


        loginButton = (Button) view.findViewById(R.id.loginButton); //find it and link
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin(); //this will be a function in the LoginFragment class
            }
        });
        return view;
    }

    /**
     * save the login values
     * send the login request to the server
     */
    public void onLogin() {
        FamilyMapModel.getInstance().setUsername(setUsername.getText().toString()); //FMM is the data model for everything
        FamilyMapModel.getInstance().setPassword(setPassword.getText().toString()); //FMM is the data model for everything
        FamilyMapModel.getInstance().setHost(setHost.getText().toString()); //FMM is the data model for everything
        FamilyMapModel.getInstance().setPort(setPort.getText().toString()); //FMM is the data model for everything

        LoginTask loginTask = new LoginTask(this.getContext());
        loginTask.execute(FamilyMapModel.getInstance().getUsername(), FamilyMapModel.getInstance().getPassword(),
                FamilyMapModel.getInstance().getHost(), FamilyMapModel.getInstance().getPort());
            loadData();
    }

    /**
     * after login, get the events and people details
     */
    private void loadData() {
        GetTask getTask = new GetTask(this.getContext());
        getTask.execute("/events/");
        try {
            getTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        GetTask getPersonTask = new GetTask(this.getContext());
        getPersonTask.execute("/person/");
        try {
            getPersonTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ((MainActivity)getActivity()).onLogin();
    }
}
