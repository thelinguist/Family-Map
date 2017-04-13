package bryce.familymap;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView personRecyclerView;
    private PersonAdapter personAdapter;
    private RecyclerView eventRecyclerView;
    private EventAdapter eventAdapter;
    private EditText searchField;
    private Button searchButton;    //probably not needed

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupActionBar();
        personRecyclerView = (RecyclerView) findViewById(R.id.personRecycler);
        personRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));    //Spencer said getActivity(), but Adriana says getBaseContext()
        eventRecyclerView = (RecyclerView) findViewById(R.id.eventRecycler);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));    //Spencer said getActivity(), but Adriana says getBaseContext()
        searchField = (EditText) findViewById(R.id.searchText);
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                search(s.toString().toLowerCase());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {
                search(s.toString().toLowerCase());
            }
        });

    }

    /**
     * main function, when given a string, find people with that name, events with that description, or events with that location
     * @param s
     */
    public void search(String s) {
        //do the events too
        //loop through people and events and do if .contains
        ArrayList<Person> peopleHits = new ArrayList<>();
        ArrayList<Event> eventHits = new ArrayList<>();

        //name
        for (Person person : FamilyMapModel.getInstance().getPeople().values()) {
            String name = person.getFirstName() + " " + person.getLastName();
            if (name.toLowerCase().contains(s)) {
                peopleHits.add(person);
            }
        }

        //location or description
        for (Event event : FamilyMapModel.getInstance().getAllEvents().values()) {
            //check specs to see if this is all of it
            if (event.getDescription().toLowerCase().contains(s) || event.getCity().toLowerCase().contains(s)
                    || event.getCountry().toLowerCase().contains(s) || event.getYear().toLowerCase().contains(s)) {
                eventHits.add(event);
            }
        }
        personAdapter = new PersonAdapter(peopleHits);
        personRecyclerView.setAdapter(personAdapter);
        eventAdapter = new EventAdapter(eventHits);
        eventRecyclerView.setAdapter(eventAdapter);
    }


    /**
     * the adapter for person, which is req'd for RecyclerView
     */
    public class PersonAdapter extends RecyclerView.Adapter<PersonHolder> {
        private ArrayList<Person> people;

        public PersonAdapter(ArrayList<Person> people) {
            this.people = people;
        }

        public PersonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(SearchActivity.this);
            View view = inflater.inflate(R.layout.search_row, parent, false);
            return new PersonHolder(view);
        }

        public int getItemCount() {
            return people.size();
        }

        public void onBindViewHolder(PersonHolder ph, int pos) {
            Person p = people.get(pos);
            ph.bindPerson(p);
        }
    }


    /**
     * the PersonHolder that binds Persons to a view in RecycleView
     */
    public class PersonHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView image;
        private TextView name;
        private TextView empty;
        private Person person;

        public PersonHolder(View view) {
            //comes from personAdapter?
            super(view);
            image = (ImageView) view.findViewById(R.id.search_row_image);
            name = (TextView) view.findViewById(R.id.search_row_field_top);
            empty = (TextView) view.findViewById(R.id.search_row_field_bottom);
            view.setOnClickListener(this);
        }

        public void bindPerson(Person p) {
            person = p;
            name.setText(p.getFirstName() + " " + p.getLastName());
            empty.setText("");


            if (p.getGender().equals("f")) {
                Drawable genderIcon = new IconDrawable(SearchActivity.this, Iconify.IconValue.fa_female).
                        colorRes(R.color.female).sizeDp(40);
                image.setImageDrawable(genderIcon);
            } else {
                Drawable genderIcon = new IconDrawable(SearchActivity.this, Iconify.IconValue.fa_male).
                        colorRes(R.color.male).sizeDp(40);
                image.setImageDrawable(genderIcon);
            }
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(SearchActivity.this, PersonActivity.class);
            i.putExtra("id", person.getPersonID());
            startActivity(i);
        }
    }

    /**
     * the EventAdapter that is req'd for RecyclerView
     */
    public class EventAdapter extends RecyclerView.Adapter<EventHolder> {
        private ArrayList<Event> events;

        public EventAdapter(ArrayList<Event> events) {
            this.events = events;
        }

        public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(SearchActivity.this);
            View view = inflater.inflate(R.layout.search_row, parent, false);
            return new EventHolder(view);
        }

        public int getItemCount() {
            return events.size();
        }

        public void onBindViewHolder(EventHolder eh, int pos) {
            Event e = events.get(pos);
            eh.bindEvent(e);
        }
    }

    /**
     * the PersonHolder that binds Persons to a view in RecycleView
     */
    public class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView image;
        private TextView title;
        private TextView personName;
        private Event event;

        public EventHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.search_row_image);
            title = (TextView) view.findViewById(R.id.search_row_field_top);
            personName = (TextView) view.findViewById(R.id.search_row_field_bottom);
            view.setOnClickListener(this);
        }

        public void bindEvent(Event eventToBind) {
            event = eventToBind;

            //top field
            String topFieldText = eventToBind.getDescription() + ": " + eventToBind.getCity() + ", " + eventToBind.getCountry();
            if (eventToBind.getYear() != null) {
                topFieldText += "(" + eventToBind.getYear() + ")";
            }
            title.setText(topFieldText);

            //bottom field
            Person eventExperiencer = FamilyMapModel.getInstance().getPeople().get(eventToBind.getPersonID());
            personName.setText(eventExperiencer.getFirstName() + " " + eventExperiencer.getLastName());

            //icon
            Drawable genderIcon = new IconDrawable(SearchActivity.this, Iconify.IconValue.fa_map_marker).colorRes(R.color.grey).sizeDp(40);
            image.setImageDrawable(genderIcon);
        }

        public void onClick(View v) {
            Intent i = new Intent(SearchActivity.this, MapsActivity.class);
            i.putExtra("EventId", event.getEventID());
            FamilyMapModel.getInstance().setFocusEvent(event);
            FamilyMapModel.getInstance().setRefresh(true);
            startActivity(i);
        }
    }


    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.search_prompt);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class); //class name is where we are going ex. personActivity.class
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //FamilyMapModel.getInstance().setRefresh(true);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class); //class name is where we are going ex. personActivity.class
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //FamilyMapModel.getInstance().setRefresh(true);
        startActivity(intent);
    }
}
