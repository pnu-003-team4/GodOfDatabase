package com.goddb.internal;

//branch test1 test2
//test

import java.util.ArrayList;

public class Wildcard {
    static MappingTable mappingTable = new MappingTable();




    public static String Sharp(String path) {
        //Todo : Search
        // 차일드 나오게 만들어야함
        System.out.println("# " + path);
        return path;
    }

    public static String Star(String path) {
        //Todo : View all
        System.out.println("* " + path);
        return path;
    }

    public static int Split(String path) {
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
            //ArrayList<Integer> child;
            return mappingTable.getKey(path);
        }
        return 0;

    }

    public static ArrayList<Integer> extractWildcard(String path, MappingTable mp) {
        // 변수
        ArrayList<Integer> result = new ArrayList<>();
        //
        mappingTable = mp;

        //path = "/Korea/Pusan/Haeundae/*&/Korea/Seoul/kangnam&US/Sanhose&??&whycant..&#zzez";
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
                    result.add(Split(array[i]));


                }
                // System.out.println(result);
                return result;
            }
        }


        return null;
    }
}