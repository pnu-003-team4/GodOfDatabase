import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Condition {

    public static int idx;
    public static String leftOperand = "";
    public static String rightOperand = "";
    public static String operator = "";

    public static void separation(String condition, int flag) { //연산자를 구분한다.

        if(flag == 0) {
            if(condition.indexOf("-") != -1) {
                operator = "-";
                idx = condition.indexOf("-");
                leftOperand = condition.substring(1);
                separation(leftOperand, 1);
            }
            else if(condition.indexOf("==") != -1) {
                operator = "==";
                idx = condition.indexOf("==");
                leftOperand = condition.substring(0, idx);
                rightOperand = condition.substring(idx+2);
            }
            else if(condition.indexOf(">") != -1) {
                operator = ">";
                idx = condition.indexOf(">");
                leftOperand = condition.substring(0, idx);
                rightOperand = condition.substring(idx+1);
            }
            else if(condition.indexOf("<") != -1) {
                operator = "<";
                idx = condition.indexOf("<");
                leftOperand = condition.substring(0, idx);
                rightOperand = condition.substring(idx+1);
            }
        }

        if(flag == 1) { //-연산자가 적용되었으므로, 기존의 연산자와 반대의 효과를 낸다.
            if(condition.indexOf("==") != -1) {
                operator = "!=";
                idx = condition.indexOf("==");
                leftOperand = condition.substring(0, idx);
                rightOperand = condition.substring(idx+2);
            }
            else if(condition.indexOf(">") != -1) {
                operator = "<=";
                idx = condition.indexOf(">");
                leftOperand = condition.substring(0, idx);
                rightOperand = condition.substring(idx+1);
            }
            else if(condition.indexOf("<") != -1) {
                operator = ">=";
                idx = condition.indexOf("<");
                leftOperand = condition.substring(0, idx);
                rightOperand = condition.substring(idx+1);
            }
        }
    }

    public static JSONObject[] extractCondition(JSONObject[] objects, String condition) throws JSONException {
        ArrayList<JSONObject> newarray = new ArrayList<>();

        separation(condition, 0);

        //연관된 오브젝트를 배열에 넣는다.
        for(JSONObject obj : objects) {
            try {
                switch(operator) {
                    case "==":
                        if (Integer.valueOf(obj.get(leftOperand).toString()) == Integer.parseInt(rightOperand)) { //Int
                            newarray.add(obj);
                        }
                        break;

                    case ">":
                        if (Integer.valueOf(obj.get(leftOperand).toString()) > Integer.parseInt(rightOperand)) {
                            newarray.add(obj);
                        }
                        break;

                    case "<":
                        if (Integer.valueOf(obj.get(leftOperand).toString()) < Integer.parseInt(rightOperand)) {
                            newarray.add(obj);
                        }
                        break;

                    case "!=":
                        if (Integer.valueOf(obj.get(leftOperand).toString()) != Integer.parseInt(rightOperand)) {
                            newarray.add(obj);
                        }
                        break;

                    case "<=":
                        if (Integer.valueOf(obj.get(leftOperand).toString()) <= Integer.parseInt(rightOperand)) {
                            newarray.add(obj);
                        }
                        break;

                    case ">=":
                        if (Integer.valueOf(obj.get(leftOperand).toString()) >= Integer.parseInt(rightOperand)) {
                            newarray.add(obj);
                        }
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                throw e;
            }
        }
        return (JSONObject[])newarray.toArray();
    }
}