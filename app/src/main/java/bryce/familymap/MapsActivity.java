package bryce.familymap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;



public class MapsActivity extends AppCompatActivity {

    private MyMapFragment myMapFragment;
    private Event currEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currEvent = FamilyMapModel.getInstance().getAllEvents().get(getIntent().getStringArrayExtra("EventId"));
        setupActionBar();
        setContentView(R.layout.map_activity_temp);
        FragmentManager fm = getSupportFragmentManager(); //this allows us to swap in fragments
        myMapFragment =(MyMapFragment) fm.findFragmentById(R.id.myMapThing);
        if (myMapFragment == null) {
            myMapFragment = new MyMapFragment();
            fm.beginTransaction().add(R.id.myMapThing, myMapFragment).commit();
        }
    }

    /**
     * this is for the menu
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setTitle("Family Map: " + currPerson.getFirstName() + " " + currPerson.getLastName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.up_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.upArrow:
                Intent intent = new Intent(this, MainActivity.class); //class name is where we are going ex. personActivity.class
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //FamilyMapModel.getInstance().setRefresh(true);
                startActivity(intent);
                return super.onOptionsItemSelected(item);
            default:
                finish();
                return super.onOptionsItemSelected(item);
        }
    }
}
