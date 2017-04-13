package bryce.familymap;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;

import java.util.ArrayList;


/**
 * Created by Bryce on 11/30/16.
 */

public class MyMapFragment extends Fragment {
    private GoogleMap appMap;
    private SupportMapFragment mapFragment;
    private ImageView icon;
    private TextView eventInfo;
    private TextView personInfo;
    private RelativeLayout footer;
    private String currPersonID;
    private Event currentEvent;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.map_fragment, container, false);

        //assign these things so that you can mess with them.
        icon = (ImageView) v.findViewById(R.id.iconInfo);
        eventInfo = (TextView) v.findViewById(R.id.eventInfo);
        personInfo = (TextView) v.findViewById(R.id.nameInfo);
        footer = (RelativeLayout) v.findViewById(R.id.footer);


        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback()
        {

            @Override
            public void onMapReady(GoogleMap googleMap) {
                appMap = googleMap;
                appMap.setMapType(FamilyMapModel.getInstance().getMapType());
                if (FamilyMapModel.getInstance().getFocusEvent() != null) {
                    currentEvent = FamilyMapModel.getInstance().getFocusEvent();
                    //8 is a good zoom level
                    appMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentEvent.getLatitude(),currentEvent.getLongitude()), 8));
                }


                for (Event event : FamilyMapModel.getInstance().getSelEvents().values()) {
                    MarkerOptions options = new MarkerOptions()
                            .position(new LatLng(event.getLatitude(), event.getLongitude()))
                            .title(event.getEventID())
                            .icon(BitmapDescriptorFactory.defaultMarker(FamilyMapModel.getInstance().getColor(event.getDescription())));

                    //why is this googleMap and not appMap??
                    Marker marker = googleMap.addMarker(options);
                    FamilyMapModel.getInstance().addMarker(event.getEventID(), marker);
                }

                appMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
                {

                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Event selEvent = FamilyMapModel.getInstance().getSelEvents().get(marker.getTitle());
//                        String eventText = selEvent.getDescription() + ": " + selEvent.getCity() + ", " + selEvent.getCountry() + " (" + selEvent.getYear() + ")";
//                        eventInfo.setText(eventText);
//
//                        currPersonID = selEvent.getPersonID();
//                        Person selPerson = FamilyMapModel.getInstance().getPeople().get(currPersonID);
//                        String personText = selPerson.getFirstName() + " " + selPerson.getLastName();
//                        personInfo.setText(personText);
//                        if (selPerson.getGender().equals("f")) {
//                            Drawable genderIcon = new IconDrawable(getActivity(), Iconify.IconValue.fa_female).
//                                    colorRes(R.color.female).sizeDp(40);
//                            icon.setImageDrawable(genderIcon);            }
//                        else {
//                            Drawable genderIcon = new IconDrawable(getActivity(), Iconify.IconValue.fa_male).
//                                    colorRes(R.color.male);
//                            icon.setImageDrawable(genderIcon);
//                        }
                        populateFooter(selEvent);

                        return true;
                    }
                });

                if (currentEvent != null) {
                    populateFooter(currentEvent);
                    FamilyMapModel.getInstance().setFocusEvent(null);
                }
                    footer.setOnClickListener(new View.OnClickListener()
                {

                    @Override
                    public void onClick(View v) {
                        ((MainActivity)getActivity()).changeToPerson(currPersonID);
                    }
                });

            }
        });

        return v;
    }

    /**
     * generate icon, fill the two text fields, and (as Patrick said) zoom in on the event that is selected
     * @param selEvent the event conaining all the info for the footer
     */
    private void populateFooter(Event selEvent) {
        //MOVE CAMERA
        appMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(selEvent.getLatitude(),selEvent.getLongitude()), 6));
        //DRAW LINES
        if (FamilyMapModel.getInstance().getLifeStoryLinesToggle()) {
            drawLines(FamilyMapModel.getInstance().getEventsOfPerson(selEvent.getPersonID()), FamilyMapModel.getInstance().getLifeEventsLineColor());
        }
        if (FamilyMapModel.getInstance().getFamilyTreeLinesToggle()) {
            //for pair, draw line
            //drawLines(FamilyMapModel.getInstance().getFamilyEvents(selEvent.getPersonID()), FamilyMapModel.getInstance().getFamilyLineColor());
        }
        if (FamilyMapModel.getInstance().getSpouseLinesToggle()) {
            Person person = FamilyMapModel.getInstance().getPeople().get(selEvent.getPersonID());
            if (person.hasSpouse()) {
                ArrayList<Event>toDraw = new ArrayList<>();
                toDraw.add(selEvent);
                toDraw.add(FamilyMapModel.getInstance().getSpouseBirth(person));
                drawLines(toDraw, FamilyMapModel.getInstance().getSpouseLinesColor());
            }
        }

        //WRITE INFO
        String eventText = selEvent.getDescription() + ": " + selEvent.getCity() + ", " + selEvent.getCountry() + " (" + selEvent.getYear() + ")";
        eventInfo.setText(eventText);

        currPersonID = selEvent.getPersonID();
        Person selPerson = FamilyMapModel.getInstance().getPeople().get(currPersonID);
        String personText = selPerson.getFirstName() + " " + selPerson.getLastName();
        personInfo.setText(personText);

        //DRAW ICON
        if (selPerson.getGender().equals("f")) {
            Drawable genderIcon = new IconDrawable(getActivity(), Iconify.IconValue.fa_female).
                    colorRes(R.color.female).sizeDp(40);
            icon.setImageDrawable(genderIcon);            }
        else {
            Drawable genderIcon = new IconDrawable(getActivity(), Iconify.IconValue.fa_male).
                    colorRes(R.color.male);
            icon.setImageDrawable(genderIcon);
        }
    }
    /**
     * draw lines on the map according to input
     * @param eventsToDraw a list of events to draw (they better have data!)
     * @param color the right color from the model class
     */
    private void drawLines(ArrayList<Event> eventsToDraw, Integer color) {
        if (eventsToDraw.size() > 1) {
            for (int i = 0; i < eventsToDraw.size() - 1; i++) {
                //draw lines
                Polyline line = appMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(eventsToDraw.get(i).getLatitude(), eventsToDraw.get(i).getLongitude()), new LatLng(eventsToDraw.get(i+1).getLatitude(), eventsToDraw.get(i+1).getLongitude()))
                        .width(12)
                        .color(color));
            }
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        if(getActivity().getClass() == MainActivity.class) {
            menu.findItem(R.id.searchMenuItem).setVisible(true);
            menu.findItem(R.id.filterMenuItem).setVisible(true);
            menu.findItem(R.id.settingsMenuItem).setVisible(true);
        }
        else {
            menu.findItem(R.id.searchMenuItem).setVisible(false);
            menu.findItem(R.id.filterMenuItem).setVisible(false);
            menu.findItem(R.id.settingsMenuItem).setVisible(false);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.searchMenuItem:
                changeToSearch();
                return true;
            case R.id.filterMenuItem:
                changeToFilter();
                return true;
            case R.id.settingsMenuItem:
                changeToSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //these three functions are the functionality of the menu items
    public void changeToSettings() {
        Intent intent = new Intent(getActivity(), SettingsActivity.class); //class name is where we are going ex. personActivity.class
        startActivity(intent);
    }

    public void changeToFilter() {
        Intent intent = new Intent(getActivity(), FilterActivity.class); //class name is where we are going ex. personActivity.class
        startActivity(intent);
    }

    public void changeToSearch() {
        Intent intent = new Intent(getActivity(), SearchActivity.class); //class name is where we are going ex. personActivity.class
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadChanges();
    }

    /**
     * If the program is in a logout state, use the MainActivity's onLogout
     * If the program is in a refresh state, then recreate the map
     */
    private void loadChanges() {
        if (FamilyMapModel.getInstance().getLogout()) {
            FamilyMapModel.getInstance().setLogout(false);
            ((MainActivity)getActivity()).onLogout();
        }
        else if (FamilyMapModel.getInstance().getRefresh()) {
            if (getActivity().getClass() == MainActivity.class) {
                FamilyMapModel.getInstance().setRefresh(false);
                ((MainActivity) getActivity()).onLogin();
            }
        }
    }
}
