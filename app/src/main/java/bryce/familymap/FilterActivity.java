package bryce.familymap;

/**
 * Created by Bryce on 12/3/16.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity {
    private ToggleButton fatherToggle;
    private ToggleButton motherToggle;
    private ToggleButton maleToggle;
    private ToggleButton femaleToggle;
    private RecyclerView eventList;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);
        setupActionBar();
        bindViews();
        updateViews();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * change the views based on data in the model class
     */
    private void updateViews() {

        //event toggles
        ArrayList<String> eventsArray = new ArrayList<>();
        eventsArray.addAll(FamilyMapModel.getInstance().getEventTypes().keySet());
        eventAdapter = new EventAdapter(eventsArray);
        eventList.setAdapter(eventAdapter);

        //parent toggles
        fatherToggle.setChecked(FamilyMapModel.getInstance().isFatherToggleParam());
        fatherToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    FamilyMapModel.getInstance().setFatherToggleParam(true);
                } else {
                    FamilyMapModel.getInstance().setFatherToggleParam(false);
                }
            }
        });

        motherToggle.setChecked(FamilyMapModel.getInstance().isMotherToggleParam());
        motherToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    FamilyMapModel.getInstance().setMotherToggleParam(true);
                } else {
                    FamilyMapModel.getInstance().setMotherToggleParam(false);
                }
            }
        });

        //gender toggles
        maleToggle.setChecked(FamilyMapModel.getInstance().isMaleGenderParam());
        maleToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    FamilyMapModel.getInstance().setMaleGenderParam(true);
                } else {
                    FamilyMapModel.getInstance().setMaleGenderParam(false);
                }
            }
        });

        femaleToggle.setChecked(FamilyMapModel.getInstance().isFemaleGenderParam());
        femaleToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    FamilyMapModel.getInstance().setFemaleGenderParam(true);
                } else {
                    FamilyMapModel.getInstance().setFemaleGenderParam(false);
                }
            }
        });
    }

    /**
     * connect the data members to real things in the View
     */
    private void bindViews() {
        fatherToggle = (ToggleButton) findViewById(R.id.father_side_toggle);
        fatherToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    FamilyMapModel.getInstance().setFatherToggleParam(true);
                } else {
                    FamilyMapModel.getInstance().setFatherToggleParam(false);
                }
            }
        });
        motherToggle = (ToggleButton) findViewById(R.id.mother_side_toggle);
        motherToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    FamilyMapModel.getInstance().setMotherToggleParam(true);
                } else {
                    FamilyMapModel.getInstance().setMotherToggleParam(false);
                }
            }
        });
        maleToggle = (ToggleButton) findViewById(R.id.male_events_toggle);
        maleToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    FamilyMapModel.getInstance().setMaleGenderParam(true);
                } else {
                    FamilyMapModel.getInstance().setMaleGenderParam(false);
                }
            }
        });
        femaleToggle = (ToggleButton) findViewById(R.id.femaleEventsToggle);
        femaleToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    FamilyMapModel.getInstance().setFemaleGenderParam(true);
                } else {
                    FamilyMapModel.getInstance().setFemaleGenderParam(false);
                }
            }
        });
        eventList = (RecyclerView) findViewById(R.id.events_filter_list);
        eventList.setLayoutManager(new LinearLayoutManager(getBaseContext()));
    }

    /* programatically add stuff
    private View createRowText(String eventName) {
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setMinimumWidth(toDP(200));
        ll.setMinimumHeight(LayoutParams.MATCH_PARENT);

        TextView title = new TextView(this);
        title.setMinimumHeight(LayoutParams.WRAP_CONTENT);
        title.setMinimumWidth(LayoutParams.WRAP_CONTENT);
        //you could set an ID here, but we don't need it
        title.setTextSize(toDP(18));
        title.setText(eventName + " Events");
        title.setTypeface(Typeface.DEFAULT_BOLD);
        ll.addView(title);

        TextView prompt = new TextView(this);
        prompt.setWidth(LayoutParams.WRAP_CONTENT);     //what's the diff between setWidth and setMinWidth?
        //you can set an ID here
        prompt.setText("Filter based on " + eventName + " Events");
        ll.addView(prompt);
        return ll;
    }
    */


    /**
     * the EventAdapter that is req'd for RecyclerView
     */
    public class EventAdapter extends RecyclerView.Adapter<EventHolder> {
        private ArrayList<String> events;
        public EventAdapter(ArrayList<String> events) {
            this.events = events;
        }
        public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(FilterActivity.this);
            View view = inflater.inflate(R.layout.filter_row, parent, false);
            return new EventHolder(view);
        }

        public int getItemCount() {
            return events.size();
        }

        public void onBindViewHolder(EventHolder eh, int pos) {
            String e = events.get(pos);
            eh.bindEvent(e);
        }
    }

    /**
     * the EventHolder that binds FilterOptions to a view in RecycleView
     */
    public class EventHolder extends RecyclerView.ViewHolder {
        private TextView filterItem;
        private TextView filterDescription;
        private ToggleButton filterToggleButton;
        private String eventNameToBind;

        public EventHolder(View view) {
            super(view);
            filterItem =  (TextView) view.findViewById(R.id.filter_item);
            filterDescription = (TextView) view.findViewById(R.id.filter_description);
            filterToggleButton = (ToggleButton) view.findViewById(R.id.filter_row_toggle);
        }

        public void bindEvent(final String eventNameToBind) {
            this.eventNameToBind = eventNameToBind;

            //top field
            String topFieldText = eventNameToBind.substring(0,1).toUpperCase() + eventNameToBind.substring(1) + " Events";
            filterItem.setText(topFieldText);

            //bottom field
            String description = "Filter by " + eventNameToBind.substring(0,1).toUpperCase() + eventNameToBind.substring(1) + " Events";
            filterDescription.setText(description);

            //toggle
            filterToggleButton.setChecked(FamilyMapModel.getInstance().getFilterParams().get(eventNameToBind));
            filterToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        FamilyMapModel.getInstance().addEventParam(eventNameToBind);
                    } else {
                        FamilyMapModel.getInstance().delEventParam(eventNameToBind);
                    }
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class); //class name is where we are going ex. personActivity.class
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        FamilyMapModel.getInstance().setRefresh(true);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class); //class name is where we are going ex. personActivity.class
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        FamilyMapModel.getInstance().setRefresh(true);
        startActivity(intent);
    }
}