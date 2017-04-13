package bryce.familymap;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Fragment login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fm = getSupportFragmentManager(); //this allows us to swap in fragments
        login = fm.findFragmentById(R.id._frag);
        if (login == null) {
            login = new LoginFragment();
            fm.beginTransaction().add(R.id._frag, login).commit();
        }
    }

    /**
     * after logging in, swap view from login to map
     */
    public void onLogin() {
        //this puts the map fragment where you want it
        if (FamilyMapModel.getInstance().getLoginStatus()) {
            Fragment fragment = new MyMapFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id._frag, fragment).commit();
        }
    }

    /**
     * when you log out, swap back to log in
     */
    public void onLogout() {
        Fragment login = new LoginFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id._frag, login).commit();
    }

    /**
     * when a person is clicked on, go to person activity
     * @param currPersonID
     */
    public void changeToPerson(String currPersonID) {
        if (currPersonID != null) {
            Intent intent = new Intent(this, PersonActivity.class); //class name is where we are going ex. personActivity.class
            intent.putExtra("id", currPersonID);                             // this puts stuff to bundle, just strings
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        if (login.isVisible()) {
            Toast.makeText(getBaseContext(),"NO", Toast.LENGTH_SHORT).show();
        }
        else {
            super.onBackPressed();
        }
    }



}
