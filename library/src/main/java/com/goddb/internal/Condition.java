package com.goddb.internal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Condition {

    public static int idx;
    public static String leftOperand = "";
    public static String rightOperand = "";
    public static String operator = "";

    public static void separation(String condition, int flag) { //연산자를 구분한다.

        switch (condition = condition.replaceAll(" ", "")) { //입력받은 문자열의 공백 제거
        }
        switch (condition = condition.replaceAll("'", "")) { //문자열을 입력받았을때 작은따옴표가 있다면 제거
        }

        if(flag == 0) {
            if(condition.indexOf("-") != -1) {
                operator = "-";
                idx = condition.indexOf("-");
                leftOperand = condition.substring(1);
                separation(leftOperand, 1);
            } else if (condition.indexOf("==") != -1) {
                operator = "==";
                idx = condition.indexOf("==");
                leftOperand = condition.substring(0, idx);
                rightOperand = condition.substring(idx+2);
            } else if(condition.indexOf(">=") != -1) {
                operator = ">=";
                idx = condition.indexOf("=");
                leftOperand = condition.substring(0, idx-1);
                rightOperand = condition.substring(idx+1);
            } else if(condition.indexOf("<=") != -1) {
                operator = "<=";
                idx = condition.indexOf("=");
                leftOperand = condition.substring(0, idx-1);
                rightOperand = condition.substring(idx+1);
            } else if(condition.indexOf(">") != -1) {
                operator = ">";
                idx = condition.indexOf(">");
                leftOperand = condition.substring(0, idx);
                rightOperand = condition.substring(idx+1);
            } else if (condition.indexOf("<") != -1) {
                operator = "<";
                idx = condition.indexOf("<");
                leftOperand = condition.substring(0, idx);
                rightOperand = condition.substring(idx+1);
            } else {
                System.out.println("Invalid operator");
            }
        }

        if(flag == 1) { //-연산자가 적용되었으므로, 기존의 연산자와 반대의 효과를 낸다.
            if(condition.indexOf("==") != -1) {
                operator = "!=";
                idx = condition.indexOf("==");
                leftOperand = condition.substring(0, idx);
                rightOperand = condition.substring(idx+2);
            } else if (condition.indexOf(">") != -1) {
                operator = "<=";
                idx = condition.indexOf(">");
                leftOperand = condition.substring(0, idx);
                rightOperand = condition.substring(idx+1);
            } else if (condition.indexOf("<") != -1) {
                operator = ">=";
                idx = condition.indexOf("<");
                leftOperand = condition.substring(0, idx);
                rightOperand = condition.substring(idx+1);
            } else {
                System.out.println("Invalid operator");
            }
        }
    }

    public static JSONArray extractCondition(JSONArray jsonArray, String condition) throws JSONException {

        if (condition.isEmpty()) return jsonArray;

        JSONArray newarray = new JSONArray();
        separation(condition, 0);

        //연관된 오브젝트를 배열에 넣는다.
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = new JSONObject(jsonArray.getString(i));
            try {
                switch(operator) {
                    case "==":
                        if ((obj.get(leftOperand).toString()).equals(rightOperand)) { //int일때와 문자열일때 둘 다 만족하기 위해 equals를 사용한다.
                            newarray.put(obj);
                        }
                        break;

                    case ">":
                        if (Integer.valueOf(obj.get(leftOperand).toString()) > Integer.parseInt(rightOperand)) {
                            newarray.put(obj);
                        }
                        break;

                    case "<":
                        if (Integer.valueOf(obj.get(leftOperand).toString()) < Integer.parseInt(rightOperand)) {
                            newarray.put(obj);
                        }
                        break;

                    case "!=":
                        if (Integer.valueOf(obj.get(leftOperand).toString()) != Integer.parseInt(rightOperand)) {
                            newarray.put(obj);
                        }
                        break;

                    case "<=":
                        if (Integer.valueOf(obj.get(leftOperand).toString()) <= Integer.parseInt(rightOperand)) {
                            newarray.put(obj);
                        }
                        break;

                    case ">=":
                        if (Integer.valueOf(obj.get(leftOperand).toString()) >= Integer.parseInt(rightOperand)) {
                            newarray.put(obj);
                        }
                        break;
                        
                    default:
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                throw e;
            }
        }
        return newarray;
    }
}
