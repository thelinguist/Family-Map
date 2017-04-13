package bryce.familymap;

import android.graphics.Color;
import android.test.AndroidTestCase;

import com.google.android.gms.maps.GoogleMap;

import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by Bryce on 12/2/16.
 */
public class FamilyMapModelTest extends AndroidTestCase {
    GetTask getTask;
    LoginTask loginTask;
    GetTask getPersonTask;

        @Override
        public void setUp() throws Exception {

            FamilyMapModel.getInstance().setUsername("brigham1");
            FamilyMapModel.getInstance().setPassword("password");
            FamilyMapModel.getInstance().setHost("192.168.1.7");
            FamilyMapModel.getInstance().setPort("8080");
            getTask = new GetTask(getContext());
            loginTask =  new LoginTask(getContext());
            getPersonTask = new GetTask(getContext());
            loginTask.execute("brigham1", "password",
                    "192.168.1.7", "8080");
            try {
                loginTask.get();
                getTask.execute("/events/"); // .get() forces async to be waited on
                getTask.get();
                getPersonTask.execute("/person/");
                getPersonTask.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }


    /**
     * can I get data from the server, store a token and use the token?
     */
    public void testLogin() {

        assertNotNull(FamilyMapModel.getInstance().getToken());
        assertNotNull(FamilyMapModel.getInstance().getPersonId());
        assertNotNull(FamilyMapModel.getInstance().getAllEvents());
        assertNotNull(FamilyMapModel.getInstance().getPeople());
        assertNotNull(FamilyMapModel.getInstance().getPeople().get(0).getFirstName());
        assertNotNull(FamilyMapModel.getInstance().getPeople().get(1).getLastName());
        assertNotNull(FamilyMapModel.getInstance().getPeople().get(2).getGender());
    }


    /**
     * test adding add removing filter parameters, and if they refelct in the get selected Events function
     */
    public void testFilter() {

        //test gender param
        FamilyMapModel.getInstance().setFemaleGenderParam(true);
        HashMap<String, Event> mine = FamilyMapModel.getInstance().getSelEvents();
        for (Event e: mine.values()) {
            assertEquals(FamilyMapModel.getInstance().getPeople().get(e.getPersonID()).getGender(), "f");
        }

        FamilyMapModel.getInstance().setFemaleGenderParam(false);
        mine = FamilyMapModel.getInstance().getSelEvents();
        for (Event e: mine.values()) {
            assertEquals(FamilyMapModel.getInstance().getPeople().get(e.getPersonID()).getGender(), "m");
        }

        //clear params
        for (String eventDescription : FamilyMapModel.getInstance().getEventTypes().keySet()) {
            FamilyMapModel.getInstance().delEventParam(eventDescription);
        }
        for (boolean value : FamilyMapModel.getInstance().getFilterParams().values()) {
            assertEquals(value, false);
        }

        //test for baptism only
        FamilyMapModel.getInstance().addEventParam("Baptism");
        mine = FamilyMapModel.getInstance().getSelEvents();
        for (Event e: mine.values()) {
            assertEquals(e.getDescription(), "Baptism");
        }

        //test adding a second event param
        FamilyMapModel.getInstance().addEventParam("Marriage");
        mine = FamilyMapModel.getInstance().getSelEvents();
        for (Event e : mine.values()) {
            if (e.getDescription().equals("Marriage")) {
                assertEquals(e.getDescription(), "Marriage");
            }
            else {
                assertEquals(e.getDescription(), "Baptism");
            }
        }
    }

    /**
     * can I mess with the settings, and it gets reflected in my FamilyMapModel class?
     * @throws Exception
     */
    public void testSettings() throws Exception {
        //maps settings
        assertEquals((int) FamilyMapModel.getInstance().getMapType(), GoogleMap.MAP_TYPE_NORMAL);
        FamilyMapModel.getInstance().setMapSel(2);  //this is the index from map_menu.xml
        assertEquals((int) FamilyMapModel.getInstance().getMapType(), GoogleMap.MAP_TYPE_SATELLITE);

        //Life Story line's default should be true
        assertEquals(FamilyMapModel.getInstance().getLifeStoryLinesToggle(), true);
        //change it and test it
        FamilyMapModel.getInstance().setShowLifeEventLines(false);
        assertEquals(FamilyMapModel.getInstance().getLifeStoryLinesToggle(), false);

        //set the color (4 corresponds to black in the color_list.xml
        FamilyMapModel.getInstance().setLifeEventsLineColor(4);
        assertEquals((int) FamilyMapModel.getInstance().getLifeEventsLineColor(), Color.BLACK);

        //logout
        FamilyMapModel.getInstance().clearData();
        assertEquals(FamilyMapModel.getInstance().getToken(), "");
        assertEquals(FamilyMapModel.getInstance().getPassword(), "");
        getTask.execute("/events/"); // .get() forces async to be waited on
        getTask.get();
        getPersonTask.execute("/person/");
        getPersonTask.get();
        assertEquals((int) FamilyMapModel.getInstance().getColors().get(0), 2); //2 was an arbitrary value that is created on "new colors"

        //resync
        FamilyMapModel.getInstance().clearCache();
        assertEquals(FamilyMapModel.getInstance().getColors().size(), 0);
        assertEquals(FamilyMapModel.getInstance().getEventTypes().size(), 0);
        assertEquals(FamilyMapModel.getInstance().getSelEvents().size(), 0);
        assertEquals(FamilyMapModel.getInstance().getMarkers().size(), 0);
        assertEquals(FamilyMapModel.getInstance().getPeople().size(), 0);
    }
}