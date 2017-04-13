package bryce.familymap;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


/**
 * Created by Bryce on 11/29/16.
 */

public class GetTask extends AsyncTask<String, Void, String>  {
    private Context context;
    private JSONObject JSONstuff;

    public GetTask(Context context) {
        this.context = context;
    }



    /**
     * use the GetTask class to get and parse a JSON object from the server into a list of Events
     * @return
     */
    public HashMap<String, Event> getEvents() {
        HashMap<String, Event> events = new HashMap<>();
        try {
            for (int i = 0; i < JSONstuff.getJSONArray("data").length(); i++) {
                JSONObject event = JSONstuff.getJSONArray("data").getJSONObject(i);
                String eventID = event.getString("eventID");

                events.put(eventID, new Event());
                events.get(eventID).setEventID(eventID);
                events.get(eventID).setpersonID(event.getString("personID"));
                events.get(eventID).setLatitude(event.getInt("latitude"));
                events.get(eventID).setLongitude(event.getInt("longitude"));
                events.get(eventID).setCountry(event.getString("country"));
                events.get(eventID).setCity(event.getString("city"));
                String description = event.getString("description");
                FamilyMapModel.getInstance().addEventType(description);
                events.get(eventID).setDescription(description);

                //rumor has it, some servers do not provide year in some cases. So account for this
                if (event.has("year")) {
                    events.get(eventID).setYear(event.getString("year"));
                }
                events.get(eventID).setDescendant(event.getString("descendant"));
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return events;
    }

    /**
     * save the JSON people! (to the familyMapModel class)
     */
    public void getPeople() {
        try {
            for (int i = 0; i < JSONstuff.getJSONArray("data").length(); i++) {
                JSONObject personData = JSONstuff.getJSONArray("data").getJSONObject(i);
                Person person = new Person();
                person.setDescendant(personData.getString("descendant"));
                person.setPersonID(personData.getString("personID"));
                person.setFirstName(personData.getString("firstName"));
                person.setLastName(personData.getString("lastName"));
                person.setGender(personData.getString("gender"));
                if (personData.has("father")) {
                    person.setFather(personData.getString("father"));
                }
                if (personData.has("mother")) {
                    person.setMother(personData.getString("mother"));
                }
                if (personData.has("spouse")) {
                    person.setSpouse(personData.getString("spouse"));
                }
                FamilyMapModel.getInstance().addPerson(person.getPersonID(), person);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected String doInBackground(String[] Params) {

        try {
            URL url = new URL("http://" + FamilyMapModel.getInstance().getHost() + ":"
                    + FamilyMapModel.getInstance().getPort() + Params[0]); //api handle

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.setRequestMethod("GET");
            //connection.setDoOutput(true);

            connection.addRequestProperty("Authorization",FamilyMapModel.getInstance().getToken());
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get HTTP response headers, if necessary
                // Map<String, List<String>> headers = connection.getHeaderFields();

                // Get response body input stream
                InputStream responseBody = connection.getInputStream();

                // Read response body bytes
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = responseBody.read(buffer)) != -1) {
                    baos.write(buffer, 0, length);
                }

                // Convert response body bytes to a string
                String responseBodyData = baos.toString();
                try {
                    JSONstuff = new JSONObject(responseBodyData);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Params[0];
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.equals("/events/")) {
            FamilyMapModel.getInstance().setEvents(getEvents());
        }
        else if (result.equals("/person/")) {
            getPeople();
        }
        else {
            Toast.makeText(context, "Incorrect API handle", Toast.LENGTH_SHORT).show();
        }
    }
}
