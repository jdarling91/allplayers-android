package com.allplayers.android;

import com.allplayers.objects.DataObject;

import com.allplayers.objects.EventData;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import com.google.gson.Gson;

public class EventsMap {
    private ArrayList<EventData> events = new ArrayList<EventData>();

    public EventsMap(String jsonResult) {
        try {
            JSONArray jsonArray = new JSONArray(jsonResult);

            // Used to create EventData objects from json.
            Gson gson = new Gson();

            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonResult.length(); i++) {
                    EventData event = gson.fromJson(jsonArray.getString(i), EventData.class);
                    try {
                        event.setLatitude(jsonArray.getJSONObject(i).getJSONObject("resource").getJSONObject("location").getString("latitude"));
                        event.setLongitude(jsonArray.getJSONObject(i).getJSONObject("resource").getJSONObject("location").getString("longitude"));
                        event.setZip(jsonArray.getJSONObject(i).getJSONObject("resource").getJSONObject("location").getString("zip"));
                    } catch (JSONException ex) {
                        // If the latitude and longitude don't exist for the specified element then do not set them
                    }

                    if (event.isNew(events)) {
                        events.add(event);
                    }
                }
            }
        } catch (JSONException ex) {
            System.err.println("EventsMap/" + ex);
        }
    }

    public ArrayList<EventData> getEventData() {
        return events;
    }
}