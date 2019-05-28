package com.goddb.internal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Option {
    static public JSONArray objectSorting(JSONArray jarray, String options) {
        final String[] section = options.split("=");

        JSONArray sortArr = new JSONArray();
        JSONArray retArr = jarray;
        // section[0] = age, section[1] = <<
        try {

            List<JSONObject> jsonValues = new ArrayList<JSONObject>();
            for (int i = 0; i < retArr.length(); i++) {
                jsonValues.add(retArr.getJSONObject(i));
            }
            if (section[1].equals("<<")) {
                Collections.sort(jsonValues, new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject o1, JSONObject o2) {
                        int valA = 0;
                        int valB = 0;

                        try {
                            valA = o1.getInt(section[0]);
                            valB = o2.getInt(section[0]);
                        } catch (JSONException e) {

                        }

                        return valA - valB;
                    }
                });
            } else if (section[1].equals(">>")) {
                Collections.sort(jsonValues, new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject o1, JSONObject o2) {
                        int valA = 0;
                        int valB = 0;

                        try {
                            valA = o1.getInt(section[0]);
                            valB = o2.getInt(section[0]);
                        } catch (JSONException e) {

                        }

                        return valB - valA;
                    }
                });
            }

            for (int i = 0; i < retArr.length(); i++) {
                sortArr.put(jsonValues.get(i));
            }
        } catch (JSONException e) {

        }
        return sortArr;
    }
}
