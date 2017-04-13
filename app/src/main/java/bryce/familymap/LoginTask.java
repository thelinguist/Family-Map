package bryce.familymap;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Bryce on 11/21/16.
 */

public class LoginTask extends AsyncTask<Object, Void, Object>  {
    private Context context;
    private String username;
    private String password;
    private String host;
    private String port;

    public LoginTask(Context context) {
        this.context = context;
    }

    //see Dr. Rodham's examples 18: web access

    @Override
    protected Object doInBackground(Object... Params) {
        String ToastIsDone = null;
        username = Params[0].toString();
        password = Params[1].toString();
        host = Params[2].toString();
        port = Params[3].toString();
        //to implement this, call LoginTask x = new; x.execute(params); NEVER SAY DOINBACKGROUND


        String postData = "{ username: \"" + username + "\", password: \"" + password + "\" }";
        try {
            URL url = new URL("http://" + host + ":" + port + "/user/login");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Set HTTP request headers, if necessary
            // connection.addRequestProperty(”Accept”, ”text/html”);

            connection.connect();

            // Write post data to request body
            OutputStream requestBody = connection.getOutputStream();
            requestBody.write(postData.getBytes());
            requestBody.close();

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
                    JSONObject response = new JSONObject(responseBodyData);
                    //if (response.getJSONObject("message") != null) {
                      //  ToastIsDone = "User name or password is wrong";
                    //} else {
                        FamilyMapModel.getInstance().setToken(response.getString("Authorization"));
                        FamilyMapModel.getInstance().setPersonId(response.getString("personId"));
                        FamilyMapModel.getInstance().setLoginStatus(true);
                        ToastIsDone =  "Success";
                    //}
                } catch (JSONException e) {
                    ToastIsDone = "IO error. Unable to parse response";
                    e.printStackTrace();
                }
            } else {
                if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                    ToastIsDone =  "Could not find server";
                    FamilyMapModel.getInstance().setLoginStatus(false);

                } else {
                    ToastIsDone =  "Invalid Login";
                    FamilyMapModel.getInstance().setLoginStatus(false);

                }
            }
        } catch (IOException e) {
            //freak out!
            ToastIsDone = "IO error. Emancipating family from person";
            e.printStackTrace();
        }
        return ToastIsDone;
    }

    @Override
    protected void onPostExecute(Object result) {
        if (result != null) {
            Toast.makeText(context,result.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
