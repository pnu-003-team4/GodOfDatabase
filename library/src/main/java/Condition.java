import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Condition {
    public static JSONObject[] extractCondition(JSONObject[] objects, String condition) {
        /*
         * TODO: condition 문장을 받았을 때(ex, "age > 30"), 연관된 오브젝트들을 배열로 Return
         *
         * return: condition 연관된 오브젝트 배열
         */
        ArrayList<JSONObject> newarray = new ArrayList<JSONObject>();

        for(JSONObject obj : objects) {
            try {
                if (Integer.valueOf(obj.get("age").toString()) > 20) {
                    newarray.add(obj);
                }
            } catch (JSONException e) {

            }
        }

        return (JSONObject[])newarray.toArray();
    }
}
//json object에 age가 없을때