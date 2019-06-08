package com.goddb.internal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Option {
    public static boolean isNumeric(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    static public JSONArray objectSorting(JSONArray jarray, String options) {
        final String[] section = options.split("=");

        JSONArray sortArr = new JSONArray();
        JSONArray retArr = jarray;
        // section[0] = age, section[1] = <<
        try {

            final List<JSONObject> jsonValues = new ArrayList<JSONObject>();
            for (int i = 0; i < retArr.length(); i++) {
                jsonValues.add(retArr.getJSONObject(i));
            }
            if (section[1].equals("<<")) {

                Collections.sort(jsonValues, new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject o1, JSONObject o2) {
                        try {
                            if (isNumeric(o1.getString(section[0]))) {
                                int valA = 0;
                                int valB = 0;

                                valA = o1.getInt(section[0]);
                                valB = o2.getInt(section[0]);

                                return valA - valB;

                            } else {
                                String valC;
                                String valD;

                                valC = o1.getString(section[0]);
                                valD = o2.getString(section[0]);

                                return valC.compareTo(valD);
                            }
                        } catch (JSONException e) {
                        }
                        return 0;
                    }
                });
            } else {
                Collections.sort(jsonValues, new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject o1, JSONObject o2) {
                        try {
                            if (isNumeric(o1.getString(section[0]))) {
                                int valA = 0;
                                int valB = 0;

                                valA = o1.getInt(section[0]);
                                valB = o2.getInt(section[0]);

                                return valB - valA;

                            } else {
                                String valC;
                                String valD;

                                valC = o1.getString(section[0]);
                                valD = o2.getString(section[0]);

                                return valD.compareTo(valC);
                            }
                        } catch (JSONException e) {
                        }
                        return 0;
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
