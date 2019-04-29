package com.goddb.internal;

//branch test1 test2
//test

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Wildcard {
    ArrayList<JSONObject> result = new ArrayList<>();


    public static String Sharp(String path) {
        //Todo : Search
        System.out.println("# " + path);
        return path;
    }

    public static String Star(String path) {
        //Todo : View all
        System.out.println("* " + path);
        return path;
    }

    public static String Split(String path) {
        //Todo : multi search
        String wild = "";
        boolean IsWild = false;
        int wildlocation = 0;
        if (path.indexOf("#") > -1) {
            wild = "#";
            wildlocation = path.indexOf("#");
            IsWild = true;
            Sharp(path);
        } else if (path.indexOf("*") > -1) {
            wild = "*";
            wildlocation = path.indexOf("*");
            IsWild = true;
            Star(path);
        }
        if (IsWild == false) {
            // 차일드 나오게 만들어야함
            System.out.println("& " + path);
        }
        return path;
    }

    public static ArrayList<String> extractWildcard(String path, List Minjae) {
        // 변수
        //ArrayList<JSONObject> result = new ArrayList<>();
        // 변수
        path = "Korea/Pusan/Haeundae/*&Korea/Seoul/kangnam&US/Sanhose&??&whycant..&#zzez";
        String wild = "";
        boolean IsWild = false;

        int pathlength = path.length();
        int wildlocation = 0;
        if (path.indexOf("&") > -1) {
            wild = "&";
            wildlocation = path.indexOf("&");
            IsWild = true;
            //And(path);
        } else {
            if (path.indexOf("#") > -1) {
                wild = "#";
                wildlocation = path.indexOf("#");
                IsWild = true;
                Sharp(path);
            } else if (path.indexOf("*") > -1) {
                wild = "*";
                wildlocation = path.indexOf("*");
                IsWild = true;
                Star(path);
            }

        }

        if (IsWild) {
            if (wild.equals("#")) {
                Sharp(path);
            } else if (wild.equals("*")) {
                Star(path);
            } else if (wild.equals("&")) {
                String str = path;
                String[] array = str.split("&");
                for (int i = 0; i < array.length; i++) {
                    Split(array[i]);
            }
            }
        }


        return null;
    }
}




/*
public class com.goddb.internal.Wildcard {


    public static String[] extractWildcard(List minjae, String path) {
        /*
         * TODO: path를 받았을 때(ex, "/Korea/Busan/University/*"), 연관된 path를 찾아내어 배열로 Return
         * TODO: 민재가 만드는 리스트(path와 index를 매칭시키는)를 가져와서 확인한다....
         *
         * return: 와일드카드와 연관된 path


        return null;
    }
}
*/