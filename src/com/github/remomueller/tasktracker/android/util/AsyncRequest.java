// // This file should act as an intermediary between the app, and the database/web JSON requests.

// // Ex:
// // 1. App makes requests for all projects
// // 2. ProjectRecord checks if a refresh has been requested, or if it's been X time since last refresh
// // 3. If refresh is requested, then make a web request for projects as JSON and load them into the internal database
// // 4. Complete request by returning all projects as Project objects (Array) from database.

// // NOTE: If the pull from the external JSON server fails, it should still pull from the internal database.

package com.github.remomueller.tasktracker.android.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import java.util.ArrayList;

// From libs directory
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import com.github.remomueller.tasktracker.android.Project;
import com.github.remomueller.tasktracker.android.Sticky;
import com.github.remomueller.tasktracker.android.User;

// import com.github.remomueller.tasktracker.android.util.DatabaseHandler;
import com.github.remomueller.tasktracker.android.util.WebRequest;

// GET /stickies.json                                           // Get JSON Array of Stickies
// GET /stickies/1.json                                         // Get JSON Sticky Object
// POST /stickies.json?description=New+Sticky&project_id=1      // Create Sticky (returns JSON Sticky object)
// PUT /stickies/1.json?description=Update+Sticky&project_id=1  // Update Sticky (returns JSON Sticky object) May be updated to PATCH in the future
// DELETE /stickies/1.json                                      // Delete Sticky (returns empty JSON)


public class AsyncRequest extends AsyncTask<Void, Void, String> {
    private static final String TAG = "TaskTrackerAndroid";

    private WebRequest webRequest;
    private Context context;
    private String method;
    private String path;
    private String params;
    private boolean doOutput;
    private boolean doInput;
    private final AsyncRequestFinishedListener finishedListener;

    // String params = URLEncoder.encode("project[name]", "UTF-8") + "=" + URLEncoder.encode(project.name, "UTF-8");
    // params += "&" + URLEncoder.encode("project[description]", "UTF-8") + "=" + URLEncoder.encode(project.description, "UTF-8");
    // params += "&" + URLEncoder.encode("project[status]", "UTF-8") + "=" + URLEncoder.encode(project.status, "UTF-8");
    // params += "&" + URLEncoder.encode("project[start_date]", "UTF-8") + "=" + URLEncoder.encode(project.start_date, "UTF-8");
    // params += "&" + URLEncoder.encode("project[end_date]", "UTF-8") + "=" + URLEncoder.encode(project.end_date, "UTF-8");

    public interface AsyncRequestFinishedListener {
        void onTaskFinished(String json); // If you want to pass something back to the listener add a param to this method
    }

    public AsyncRequest(Context context, String method, String path, String params, AsyncRequestFinishedListener finishedListener) {
        this.webRequest = new WebRequest(context, method, path, params);

        this.finishedListener = finishedListener;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            // Check if web request is required.
            // Web Request, and JSON Deserialization need to be done here.
            return webRequest.webRequest();
        } catch (IOException e) {
            return "Unable to Connect: Make sure you have an active network connection." + e;
        }
    }

    @Override
    protected void onPostExecute(String json) {
        super.onPostExecute(json);
        // Sticky sticky = new Sticky();
        // sticky.id = 5;
        // ArrayList<Project> projects = new ArrayList<Project>();
        finishedListener.onTaskFinished(json);
   }

}
