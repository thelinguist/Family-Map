package bryce.familymap;


import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.concurrent.ExecutionException;

public class SettingsActivity extends AppCompatActivity {
    Spinner lifeStorySpinner;
    ToggleButton lifeStoryToggle;
    Spinner familyTreeLine;
    ToggleButton familyTreeToggle;
    Spinner spouseLines;
    ToggleButton spouseLinesToggle;
    Spinner mapTypes;
    GridLayout resyncData;
    GridLayout logout;

    /**
     * for the top menu
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        setContentView(R.layout.settings);
        bindViews();
        updateViews();
        FamilyMapModel.getInstance().setLineColorIndices(getResources().getStringArray(R.array.colors_list));
    }

    /**
     * Set functionality for each button, toggle, and spinner
     */
    private void updateViews() {
        //*****TOGGLES*******
        lifeStoryToggle.setChecked(FamilyMapModel.getInstance().getLifeStoryLinesToggle());
        lifeStoryToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    FamilyMapModel.getInstance().setShowLifeEventLines(true);
                } else {
                    FamilyMapModel.getInstance().setShowLifeEventLines(false);
                }
            }
        });

        familyTreeToggle.setChecked(FamilyMapModel.getInstance().getLifeStoryLinesToggle());
        familyTreeToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    FamilyMapModel.getInstance().setShowFamilyLines(true);
                } else {
                    FamilyMapModel.getInstance().setShowFamilyLines(false);
                }
            }
        });

        spouseLinesToggle.setChecked(FamilyMapModel.getInstance().getSpouseLinesToggle());
        spouseLinesToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    FamilyMapModel.getInstance().setShowSpouseLines(true);
                } else {
                    FamilyMapModel.getInstance().setShowSpouseLines(false);
                }
            }
        });


        //*****SPINNERS*******
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.colors_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lifeStorySpinner.setAdapter(adapter);
        //set default
        lifeStorySpinner.setSelection(FamilyMapModel.getInstance().getLifeEventsSpinnerPosition());
        lifeStorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FamilyMapModel.getInstance().setLifeEventsLineColor(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        familyTreeLine.setAdapter(adapter);
        familyTreeLine.setSelection(FamilyMapModel.getInstance().getFamilyEventsSpinnerPosition());
        familyTreeLine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FamilyMapModel.getInstance().setFamilyLineColor(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spouseLines.setAdapter(adapter);
        spouseLines.setSelection(FamilyMapModel.getInstance().getSpouseEventsColorPosition());
        spouseLines.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FamilyMapModel.getInstance().setSpouseLinesColor(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //MAP SPINNER
        ArrayAdapter<CharSequence> mapAdapter = ArrayAdapter.createFromResource(this, R.array.map_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mapTypes.setAdapter(mapAdapter);
        mapTypes.setSelection(FamilyMapModel.getInstance().getMapTypePosition());
        final String[] mapOptions = getResources().getStringArray(R.array.map_list);
        mapTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FamilyMapModel.getInstance().setMapSel(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        //*****BUTTONS*******
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FamilyMapModel.getInstance().clearData();
                FamilyMapModel.getInstance().setLogout(true);
                finish();
            }
        });

        resyncData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success = true;
                FamilyMapModel.getInstance().clearCache();
                GetTask getTask = new GetTask(getBaseContext());
                getTask.execute("/events/");
                try {
                    getTask.get();
                } catch (InterruptedException e) {
                    success = false;
                    Toast.makeText(getBaseContext(), "Error loading data, emancipating family from person", Toast.LENGTH_SHORT).show();
                } catch (ExecutionException e) {
                    success = false;
                    Toast.makeText(getBaseContext(), "Error loading data, emancipating family from person", Toast.LENGTH_SHORT).show();
                }
                GetTask getPersonTask = new GetTask(getBaseContext());
                getPersonTask.execute("/person/");
                try {
                    getPersonTask.get();
                } catch (InterruptedException e) {
                    success = false;
                    Toast.makeText(getBaseContext(), "Error loading data, emancipating family from person", Toast.LENGTH_SHORT).show();
                } catch (ExecutionException e) {
                    success = false;
                    Toast.makeText(getBaseContext(), "Error loading data, emancipating family from person", Toast.LENGTH_SHORT).show();
                }
                if (success) {
                    Toast.makeText(getBaseContext(), "The data was refreshed", Toast.LENGTH_LONG).show();
                    FamilyMapModel.getInstance().setRefresh(true);
                    Intent intent = new Intent(getBaseContext(), MainActivity.class); //class name is where we are going ex. personActivity.class
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            }
        });
    }

    /**
     * bind the views to the data members
     */
    private void bindViews() {
        lifeStorySpinner = (Spinner) findViewById(R.id.life_story_lines_color);
        lifeStoryToggle = (ToggleButton)  findViewById(R.id.lifeStoryLinesToggle);
        familyTreeLine = (Spinner) findViewById(R.id.familyTreeLinesColor);
        familyTreeToggle = (ToggleButton) findViewById(R.id.familyTreeLinesToggle);
        spouseLines = (Spinner) findViewById(R.id.spouseLinesColor);
        spouseLinesToggle = (ToggleButton) findViewById(R.id.spouseLinesToggle);
        mapTypes = (Spinner) findViewById(R.id.mapTypeSelector);
        resyncData = (GridLayout) findViewById(R.id.resyncSetting);
        logout = (GridLayout) findViewById(R.id.logoutSetting);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class); //class name is where we are going ex. personActivity.class
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        FamilyMapModel.getInstance().setRefresh(true);
        startActivity(intent);
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class); //class name is where we are going ex. personActivity.class
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        FamilyMapModel.getInstance().setRefresh(true);
        finish();
        startActivity(intent);
    }
}
