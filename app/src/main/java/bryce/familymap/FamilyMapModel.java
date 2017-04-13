package bryce.familymap;

import android.graphics.Color;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by Bryce on 11/21/16.
 */

public class FamilyMapModel {

    private static FamilyMapModel instance;

    //login items
    private String username;
    private String password;
    private String host;
    private String port;
    private String token;
    private String personId;
    private boolean loginIsGood;
    private boolean loggingOut;

    //model lists
    private HashMap<String, Event> events;
    private HashMap<String, Person> people;
    private HashMap<String, Marker> markers;
    private HashMap<String, Integer> eventTypes;
    private ArrayList<Integer> colors;
    ArrayList<Person> relatives;

    //settings params
    private Integer mapType;
    private Integer familyLineColor;
    private boolean showFamilyLines;
    private Integer lifeEventsLineColor;
    private boolean showLifeEventsLines;
    private Integer spouseLinesColor;
    private boolean showSpouseLines;


    //filter params
    private HashMap<String, Event> selEvents;
    private HashMap<String, Boolean> filterParams;
    private boolean maleGenderParam;
    private boolean femaleGenderParam;
    private boolean fatherToggleParam;
    private boolean motherToggleParam;
    private boolean refresh;
    private String[] lineColorIndices;

    //map variables

    //defaults
    private static final Integer DEFAULT_LIFE_EVENTS_LINE_COLOR = Color.GREEN;
    private static final Integer DEFAULT_FAMILY_TREE_LINE_COLOR = Color.RED;
    private static final Integer DEFAULT_SPOUSE_LINE_COLOR = Color.BLUE;
    private static final Integer DEFAULT_MAP_TYPE = GoogleMap.MAP_TYPE_NORMAL;
    private static final Boolean DEFAULT_PARAM_TOGGLE = true;

    private Event focusEvent;

    //Singleton(SEEN-GUHL-TUHN): a town in England where all the sad single people go
    public static FamilyMapModel getInstance() {
        if (instance == null) {
            instance = new FamilyMapModel();
        }
        return instance;
    }

    private FamilyMapModel() {
        loginIsGood = false;
        loggingOut = false;
        refresh = false;
        maleGenderParam = DEFAULT_PARAM_TOGGLE;
        femaleGenderParam = DEFAULT_PARAM_TOGGLE;
        fatherToggleParam = DEFAULT_PARAM_TOGGLE;
        motherToggleParam = DEFAULT_PARAM_TOGGLE;

        lifeEventsLineColor = DEFAULT_LIFE_EVENTS_LINE_COLOR;
        familyLineColor = DEFAULT_FAMILY_TREE_LINE_COLOR;
        spouseLinesColor = DEFAULT_SPOUSE_LINE_COLOR;
        mapType = DEFAULT_MAP_TYPE;

        showSpouseLines = DEFAULT_PARAM_TOGGLE;
        showFamilyLines = DEFAULT_PARAM_TOGGLE;
        showLifeEventsLines = DEFAULT_PARAM_TOGGLE;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
        //relatives = getRelatives();
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setEvents(HashMap<String, Event> events) {
        this.events = events;
        //on initial load, set selected events too. always call events
        selEvents = this.events;
    }

    public Integer getMapType() {
        return mapType;
    }

    public HashMap<String, Event> getAllEvents() {
        return events;
    }

    public HashMap<String, Person> getPeople() {
        return people;
    }

    public HashMap<String, Marker> getMarkers() {
        return markers;
    }

    public int getColor(String description) {
        return eventTypes.get(description.toLowerCase());
    }

    public HashMap<String, Integer> getEventTypes() {
        return eventTypes;
    }

    public List<Integer> getColors() {
        return colors;
    }

    public void setLoginStatus(boolean status) {
        loginIsGood = status;
    }

    public boolean getLoginStatus() {
        return loginIsGood;
    }

    public void setShowLifeEventLines(boolean showLifeStory) {
        this.showLifeEventsLines = showLifeStory;
    }

    public void setShowFamilyLines(boolean showFamilyLines) {
        this.showFamilyLines = showFamilyLines;
    }

    public void setShowSpouseLines(boolean showSpouseLines) {
        this.showSpouseLines = showSpouseLines;
    }

    public void setLogout(boolean logout) {
        loggingOut = logout;
    }

    public boolean getLogout() {
        return loggingOut;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    public boolean getRefresh() {
        return refresh;
    }

    public boolean isMotherToggleParam() {
        return motherToggleParam;
    }

    public void setMotherToggleParam(boolean motherToggleParam) {
        this.motherToggleParam = motherToggleParam;
    }

    public boolean isFatherToggleParam() {
        return fatherToggleParam;
    }

    public void setFatherToggleParam(boolean fatherToggleParam) {
        this.fatherToggleParam = fatherToggleParam;
    }

    public boolean isMaleGenderParam() {
        return maleGenderParam;
    }

    public void setMaleGenderParam(boolean maleGenderParam) {
        this.maleGenderParam = maleGenderParam;
    }

    public boolean isFemaleGenderParam() {
        return femaleGenderParam;
    }

    public void setFemaleGenderParam(boolean femaleGenderParam) {
        this.femaleGenderParam = femaleGenderParam;
    }

    public void setLineColorIndices(String[] lineColorIndices) {
        this.lineColorIndices = lineColorIndices;
    }

    public void addMarker(String eventID, Marker marker) {
        if (markers == null) {
            markers = new HashMap<>();
        }
        markers.put(eventID, marker);
    }

    /**
     * for each event, if it's a new event, add it to the list of eventTypes,
     * Then, generate a color and save it
     *
     * @param description the event type
     */
    public void addEventType(String description) {
        description = description.toLowerCase();
        if (eventTypes == null) {
            eventTypes = new HashMap<>();
            filterParams = new HashMap<>();
        }
        if (colors == null) {
            colors = new ArrayList<>();
            colors.add(2);  //2 isn't used as an actual color
        }
        if (!eventTypes.containsKey(description)) {
            int color = 2;
            while (colors.contains(color)) {
                color = (int) (Math.random() * 18); //allow 18 colors
            }
            colors.add(color);
            eventTypes.put(description, (color * 20));
            filterParams.put(description, DEFAULT_PARAM_TOGGLE);
        }
    }

    /**
     * add a person to the people HashMap (also initialize if necessary)
     * @param personID
     * @param person
     */
    public void addPerson(String personID, Person person) {
        if (people == null) {
            people = new HashMap<>();
        }
        people.put(personID, person);
    }

    /**
     * get the filtered list of events based on params already set
     *
     * @return
     */
    public HashMap<String, Event> getSelEvents() {
        HashMap<String, Event> eventsOut = new HashMap<>();

        for (Event event : events.values()) {
            if (paramsChecked(event)) {
                eventsOut.put(event.getEventID(), event);
            }
        }
        if (fatherToggleParam) {
            //remove dad side people
        }
        if (motherToggleParam) {
            //remove mom side people
        }
        selEvents = eventsOut;
        return selEvents;
    }

    /**
     * scan the parameters to see if they are set
     *
     * @param event
     * @return the parameter value
     */
    private boolean paramsChecked(Event event) {
        for (String eventType : eventTypes.keySet()) {
            //if the filter param hasn't been turned on and the event IS that param = false, return false
            //if the filter param HAS been turned on and event ISN'T param = false;
            // else if filter param HAS been turned on and the event IS true = true, skip the return
            if (!filterParams.get(eventType) && event.getDescription().toLowerCase().equals(eventType)) {
                return false;
            }
        }

        //gender

        if (!maleGenderParam && people.get(event.getPersonID()).getGender().equals("m")) {
            return false;
        }
        if (!femaleGenderParam && people.get(event.getPersonID()).getGender().equals("f")) {
            return false;
        }
        return true;
    }

    public void addEventParam(String param) {
        filterParams.put(param, true);
    }

    public void delEventParam(String param) {
        filterParams.put(param, false);
    }

    /**
     * this clears all the data that was set during the program.
     * It's used only for logout
     */
    public void clearData() {
        loginIsGood = false;
        clearCache();

        username = "";
        password = "";
        host = "";
        port = "";
        personId = "";

        mapType = GoogleMap.MAP_TYPE_NORMAL;
        showFamilyLines = DEFAULT_PARAM_TOGGLE;
        showSpouseLines = DEFAULT_PARAM_TOGGLE;
        showLifeEventsLines = DEFAULT_PARAM_TOGGLE;
        lifeEventsLineColor = DEFAULT_LIFE_EVENTS_LINE_COLOR;
        familyLineColor = DEFAULT_FAMILY_TREE_LINE_COLOR;
        spouseLinesColor = DEFAULT_SPOUSE_LINE_COLOR;
        mapType = DEFAULT_MAP_TYPE;

        maleGenderParam = DEFAULT_PARAM_TOGGLE;
        femaleGenderParam = DEFAULT_PARAM_TOGGLE;
        fatherToggleParam = DEFAULT_PARAM_TOGGLE;
        motherToggleParam = DEFAULT_PARAM_TOGGLE;


        token = "";
    }

    /**
     * just clear the database.
     * Used on Resync, or in conjunction with clearData
     */
    public void clearCache() {
        colors.clear();
        eventTypes.clear();
        filterParams.clear();
        selEvents.clear();
        people.clear();
        events.clear();
        markers.clear();
    }

    public Integer getFamilyLineColor() {
        return familyLineColor;
    }

    /**
     * this is based on the menu xml,
     * @param menuPosition returns a position in the array relating to the menu xml
     */
    public void setFamilyLineColor(Integer menuPosition) {
        if (lineColorIndices[menuPosition].equals("Red")) {
            familyLineColor = Color.RED;
        } else if (lineColorIndices[menuPosition].equals("Yellow")) {
            familyLineColor = Color.YELLOW;
        } else if (lineColorIndices[menuPosition].equals("Green")) {
            familyLineColor = Color.GREEN;
        } else if (lineColorIndices[menuPosition].equals("Blue")) {
            familyLineColor = Color.BLUE;
        } else if (lineColorIndices[menuPosition].equals("Black")) {
            familyLineColor = Color.BLACK;
        }
        if (lineColorIndices[menuPosition].equals("Pink")) {
            familyLineColor = Color.MAGENTA;
        }
    }

    public Integer getLifeEventsLineColor() {
        return lifeEventsLineColor;
    }

    /**
     * set the color of Life Event Lines based on the menu position from the color_list.xml
     * @param menuPosition
     */
    public void setLifeEventsLineColor(Integer menuPosition) {
        if (lineColorIndices[menuPosition].equals("Red")) {
            lifeEventsLineColor = Color.RED;
        } else if (lineColorIndices[menuPosition].equals("Yellow")) {
            lifeEventsLineColor = Color.YELLOW;
        } else if (lineColorIndices[menuPosition].equals("Green")) {
            lifeEventsLineColor = Color.GREEN;
        } else if (lineColorIndices[menuPosition].equals("Blue")) {
            lifeEventsLineColor = Color.BLUE;
        } else if (lineColorIndices[menuPosition].equals("Black")) {
            lifeEventsLineColor = Color.BLACK;
        } else if (lineColorIndices[menuPosition].equals("Pink")) {
            lifeEventsLineColor = Color.MAGENTA;
        }
    }

    public Integer getSpouseLinesColor() {
        return spouseLinesColor;
    }

    /**
     * set the color of Spouse Lines based on the menu position from the color_list.xml
     * @param menuPosition
     */
    public void setSpouseLinesColor(Integer menuPosition) {
        if (lineColorIndices[menuPosition].equals("Red")) {
            spouseLinesColor = Color.RED;
        } else if (lineColorIndices[menuPosition].equals("Yellow")) {
            spouseLinesColor = Color.YELLOW;
        } else if (lineColorIndices[menuPosition].equals("Green")) {
            spouseLinesColor = Color.GREEN;
        } else if (lineColorIndices[menuPosition].equals("Blue")) {
            spouseLinesColor = Color.BLUE;
        } else if (lineColorIndices[menuPosition].equals("Black")) {
            spouseLinesColor = Color.BLACK;
        } else if (lineColorIndices[menuPosition].equals("Pink")) {
            spouseLinesColor = Color.MAGENTA;
        }
    }


    /**
     * this returns the position based on types found in colors_list.xml
     *
     * @return position of array
     */
    public int getLifeEventsSpinnerPosition() {
        if (lifeEventsLineColor == Color.RED) {
            return 0;
        } else if (lifeEventsLineColor == Color.YELLOW) {
            return 1;
        } else if (lifeEventsLineColor == Color.GREEN) {
            return 2;
        } else if (lifeEventsLineColor == Color.BLUE) {
            return 3;
        } else if (lifeEventsLineColor == Color.BLACK) {
            return 4;
        } else if (lifeEventsLineColor == Color.MAGENTA) {
            return 5;
        } else return -1; //dang it, I broke it
    }

    /**
     * this returns the position based on types found in colors_list.xml
     *
     * @return position of array
     */
    public int getSpouseEventsColorPosition() {
        if (spouseLinesColor == Color.RED) {
            return 0;
        } else if (spouseLinesColor == Color.YELLOW) {
            return 1;
        } else if (spouseLinesColor == Color.GREEN) {
            return 2;
        } else if (spouseLinesColor == Color.BLUE) {
            return 3;
        } else if (spouseLinesColor == Color.BLACK) {
            return 4;
        } else if (spouseLinesColor == Color.MAGENTA) {
            return 5;
        } else return -1; //dang it, I broke it
    }

    /**
     * this returns the position based on types found in colors_list.xml
     *
     * @return position of array
     */
    public int getFamilyEventsSpinnerPosition() {
        if (familyLineColor == Color.RED) {
            return 0;
        } else if (familyLineColor == Color.YELLOW) {
            return 1;
        } else if (familyLineColor == Color.GREEN) {
            return 2;
        } else if (familyLineColor == Color.BLUE) {
            return 3;
        } else if (familyLineColor == Color.BLACK) {
            return 4;
        } else if (familyLineColor == Color.MAGENTA) {
            return 5;
        } else return -1; //dang it, I broke it
    }

    /**
     * this returns the position based on types found in map_list.xml
     *
     * @return position of array
     */
    public int getMapTypePosition() {
        if (mapType.equals(GoogleMap.MAP_TYPE_NORMAL)) {
            return 0;
        } else if (mapType == GoogleMap.MAP_TYPE_HYBRID) {
            return 1;
        } else if (mapType == GoogleMap.MAP_TYPE_SATELLITE) {
            return 2;
        } else if (mapType == GoogleMap.MAP_TYPE_TERRAIN) {
            return 3;
        } else return -1; //you broke it
    }

    public boolean getSpouseLinesToggle() {
        return showSpouseLines;
    }

    public boolean getFamilyTreeLinesToggle() {
        return showFamilyLines;
    }

    public boolean getLifeStoryLinesToggle() {
        return showLifeEventsLines;
    }

    public HashMap<String, Boolean> getFilterParams() {
        return filterParams;
    }

    /**
     * set the map based on the map selection xml
     * @param mapSel index of map selection menu
     */
    public void setMapSel(int mapSel) {
        switch (mapSel) {
            case 0:
                mapType = GoogleMap.MAP_TYPE_NORMAL;
                break;
            case 1:
                mapType = GoogleMap.MAP_TYPE_HYBRID;
                break;
            case 2:
                mapType = GoogleMap.MAP_TYPE_SATELLITE;
                break;
            case 3:
                mapType = GoogleMap.MAP_TYPE_TERRAIN;
                break;
            default:
                try {
                    throw new Exception();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    public void setFocusEvent(Event focusEvent) {
        this.focusEvent = focusEvent;
    }

    public Event getFocusEvent() {
        return focusEvent;
    }
    /**
     * This grabs all the events pertaining to the currentPerson and sorts them
     * @return a sorted list as an arrayList
     */
    public ArrayList<Event> getEventsOfPerson(String personID) {
            TreeSet<Event> toSort = new TreeSet<>();
        //find all the events (minus the ones filtered out)
            for (Event event : getAllEvents().values()) {
                if (event.getPersonID().equals(personID)) {
                    toSort.add(event);  //sorts automatically
                }
            }
            return new ArrayList<>(toSort);
    }

    /**
     * a quick way to get the brith of the spouse, based on the person
     * @param person
     * @return first spouse event
     */
    public Event getSpouseBirth(Person person) {
        Person spouse = people.get(person.getSpouse());
        ArrayList<Event> spouseEvents = getEventsOfPerson(spouse.getPersonID());
        return spouseEvents.get(0);
    }


    //public ArrayList<Person> getRelatives() {
        //if (relatives == null) {
            //String ID = personId;
            //while (people.get(ID).hasFather())
        //}
        //else return relatives;
    //}
}
