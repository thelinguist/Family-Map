package bryce.familymap;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * Created by Bryce on 12/1/16.
 */

public class PersonActivity extends AppCompatActivity {
    private TextView firstName;
    private TextView lastName;
    private TextView gender;
    private ImageView lifeEventsIcon;
    private RelativeLayout lifeEventsButton;
    private ImageView familyIcon;
    private RelativeLayout familyButton;

    private RecyclerView lifeEventsList;
    private PersonAdapter personAdapter;
    private RecyclerView familyList;
    private EventAdapter eventAdapter;


    private String currPersonID;
    private Person currPerson;
    private HashMap<String, String> relations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person);
        currPersonID = getIntent().getStringExtra("id");
        currPerson = FamilyMapModel.getInstance().getPeople().get(currPersonID);
        setupActionBar();

        //assign these things so that you can mess with them.
        bindViewItems();


        //update the Views
        updateTheViews();
    }

    /**
     * this sets up the go to top button and back arrow
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Family Map: " + currPerson.getFirstName() + " " + currPerson.getLastName());

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.up_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This helper function updates all views based on parameters set by current Person
     */
    private void updateTheViews() {
        firstName.setText(currPerson.getFirstName());
        lastName.setText(currPerson.getLastName());
        String genderField = currPerson.getGender();
        if (genderField.equals("m")) {
            gender.setText("Male");
        }
        else {
            gender.setText("Female");
        }
        setExpanderButton(familyButton, familyIcon, familyList);
        setExpanderButton(lifeEventsButton, lifeEventsIcon, lifeEventsList);
        Drawable image = new IconDrawable(PersonActivity.this, Iconify.IconValue.fa_arrow_up);
        familyIcon.setImageDrawable(image);
        lifeEventsIcon.setImageDrawable(image);


        personAdapter = new PersonAdapter(getRelations());
        familyList.setAdapter(personAdapter);
        eventAdapter = new EventAdapter(getEvents());
        lifeEventsList.setAdapter(eventAdapter);
    }

    /**
     * This method links the variables to the Views in the XML
     */
    private void bindViewItems() {
        firstName = (TextView) findViewById(R.id.firstNamePlaceholder);
        lastName = (TextView) findViewById(R.id.lastNamePlaceholder);
        gender = (TextView) findViewById(R.id.genderPlaceholder);
        lifeEventsIcon = (ImageView) findViewById(R.id.lifeEventsIcon);
        familyIcon = (ImageView) findViewById(R.id.familyIcon);
        lifeEventsButton = (RelativeLayout) findViewById(R.id.lifeEventsButton);
        familyButton = (RelativeLayout) findViewById(R.id.familyButton);

        lifeEventsList = (RecyclerView) findViewById(R.id.life_events_list);
        lifeEventsList.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        familyList = (RecyclerView) findViewById(R.id.family_list);
        familyList.setLayoutManager(new LinearLayoutManager(getBaseContext()));
    }


    /**
     * This function generates an ordered list of familial relations to the currentPerson
     * @return a list, sorted according to the specs: Father, Mother, Spouse, Children
     */
    private ArrayList<Person> getRelations() {
        relations = new HashMap<>();
        ArrayList<Person> relatives = new ArrayList<>();
        if (currPerson.hasFather()) {
            String fatherID = currPerson.getFather();
            relatives.add(FamilyMapModel.getInstance().getPeople().get(fatherID));
            relations.put(fatherID, "Father");
        }
        if (currPerson.hasMother()) {
            String motherID = currPerson.getMother();
            relatives.add(FamilyMapModel.getInstance().getPeople().get(motherID));
            relations.put(motherID, "Mother");

        }
        if (currPerson.hasSpouse()) {
            String spouseID = currPerson.getSpouse();
            relatives.add(FamilyMapModel.getInstance().getPeople().get(spouseID));
            relations.put(spouseID, "Spouse");

        }
        //add children
        if (currPerson.getGender().equals("m")) {
            for (Person person : FamilyMapModel.getInstance().getPeople().values()) {
                if (person.hasFather() && person.getFather().equals(currPersonID)) {
                    relatives.add(person);
                    relations.put(person.getPersonID(), "Child");
                }
            }
        }
        else {
            for (Person person : FamilyMapModel.getInstance().getPeople().values()) {
                if (person.hasMother() && person.getMother().equals(currPersonID)) {
                    relatives.add(person);
                    relations.put(person.getPersonID(), "Child");
                }
            }
        }
        return relatives;
    }

    /**
     * This grabs all the events pertaining to the currentPerson and sorts them
     * @return a sorted list as an arrayList
     */
    private ArrayList<Event> getEvents() {
        TreeSet<Event> toSort = new TreeSet<>();
        for (Event event : FamilyMapModel.getInstance().getAllEvents().values()) {
            if (event.getPersonID().equals(currPersonID)) {
                toSort.add(event);  //sorts automatically
            }
        }
        return new ArrayList<>(toSort);
    }

    /**
     * A helper function that sets a button and hides things based on onClick
     * @param button the button to detect clicks from
     * @param icon the icon to change
     * @param thingToHide the thing to hide
     */
    private void setExpanderButton(final RelativeLayout button, final ImageView icon, final RecyclerView thingToHide) {


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thingToHide.setVisibility(thingToHide.isShown()? View.GONE:View.VISIBLE);
                if (thingToHide.isShown())
                {
                    Drawable image = new IconDrawable(PersonActivity.this, Iconify.IconValue.fa_arrow_up);
                    icon.setImageDrawable(image);
                }
                else if (!thingToHide.isShown())
                {
                    Drawable image = new IconDrawable(PersonActivity.this, Iconify.IconValue.fa_arrow_down);
                    icon.setImageDrawable(image);
                }
            }
        });
    }



    //BASED ON TIME, Migrate this all to a new .class file so you can share
    // TODO: 12/6/16 see above
    /**
     * the adapter for person, which is req'd for RecyclerView
     */
    public class PersonAdapter extends RecyclerView.Adapter<PersonHolder> {
        private ArrayList<Person> people;
        public PersonAdapter(ArrayList<Person> people) {
            this.people = people;
        }
        public PersonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(PersonActivity.this);
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
        private TextView relation;
        private Person person;

        public PersonHolder(View view) {
            //comes from personAdapter?
            super(view);
            image = (ImageView) view.findViewById(R.id.search_row_image);
            name =  (TextView) view.findViewById(R.id.search_row_field_top);
            relation = (TextView) view.findViewById(R.id.search_row_field_bottom);
            view.setOnClickListener(this);
        }

        /**
         * connect data members to view items
         * @param p a person to get details from
         */
        public void bindPerson(Person p) {
            person = p;
            name.setText(p.getFirstName() + " " + p.getLastName());
            relation.setText(relations.get(p.getPersonID()));


            if (p.getGender().equals("f")) {
                Drawable genderIcon = new IconDrawable(PersonActivity.this, Iconify.IconValue.fa_female).
                        colorRes(R.color.female).sizeDp(40);
                image.setImageDrawable(genderIcon);            }
            else {
                Drawable genderIcon = new IconDrawable(PersonActivity.this, Iconify.IconValue.fa_male).
                        colorRes(R.color.male).sizeDp(40);
                image.setImageDrawable(genderIcon);
            }
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(PersonActivity.this, PersonActivity.class);
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
            LayoutInflater inflater = LayoutInflater.from(PersonActivity.this);
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
     * the EventHolder that binds Persons to a view in RecycleView
     */
    public class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView image;
        private TextView title;
        private TextView personName;
        private Event event;

        public EventHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.search_row_image);
            title =  (TextView) view.findViewById(R.id.search_row_field_top);
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
            personName.setText(eventExperiencer.getFirstName() +  " " + eventExperiencer.getLastName());

            //icon
            Drawable genderIcon = new IconDrawable(PersonActivity.this, Iconify.IconValue.fa_map_marker).colorRes(R.color.grey).sizeDp(40);
            image.setImageDrawable(genderIcon);
        }

        /**
         * make a map activity with the event
         * @param v
         */
        public void onClick(View v) {
            Intent i = new Intent(PersonActivity.this, MapsActivity.class);
            i.putExtra("EventId", event.getEventID());
            FamilyMapModel.getInstance().setFocusEvent(event);
            FamilyMapModel.getInstance().setRefresh(true);
            startActivity(i);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.upArrow:
                Intent intent = new Intent(this, MainActivity.class); //class name is where we are going ex. personActivity.class
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                FamilyMapModel.getInstance().setRefresh(true);
                startActivity(intent);
                return super.onOptionsItemSelected(item);
            default:
                finish();
                return super.onOptionsItemSelected(item);
        }
    }
}
