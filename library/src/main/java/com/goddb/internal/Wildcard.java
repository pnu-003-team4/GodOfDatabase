package com.goddb.internal;

//branch test1 test2
//test

import java.util.ArrayList;

public class Wildcard {
    public static String Sharp(String path) {
        //Todo : Search
        // 차일드 나오게 만들어야함
        System.out.println("# " + path);
        return path;
    }

    public static ArrayList<Integer> Star(int pathkey, MappingTable mappingTable) {
        ArrayList<Integer> child;
        child = mappingTable.getChildKeys(pathkey);
        for (int i = 0; i < child.size(); i++) {
            child.addAll(Star(child.get(i), mappingTable));
        }

        return child;
    }

    public static ArrayList<Integer> Split(String path, MappingTable mappingTable) {
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
            String Starpath = path.substring(0, wildlocation - 1);
            return Star(mappingTable.getKey(Starpath), mappingTable);
        }
        if (IsWild == false) {
            //ArrayList<Integer> child;
            ArrayList<Integer> temp = new ArrayList<>();
            temp.add(mappingTable.getKey(path));
            return temp;
        }
        return null;

    }

    public static ArrayList<Integer> extractWildcard(String path, MappingTable mp) {
        // 변수
        ArrayList<Integer> result = new ArrayList<>();

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

            }

        }

        if (IsWild) {
            if (("#").equals(wild)) {
                Sharp(path);
            } else if (("*").equals(wild)) {
                String Starpath = path.substring(0, wildlocation - 1);
                result.addAll(Star(mp.getKey(Starpath), mp));
            } else if (("&").equals(wild)) {
                String str = path;
                String[] array = str.split("&");
                for (int i = 0; i < array.length; i++) {
                    result.addAll(Split(array[i], mp));

                }
                // System.out.println(result);
                return result;
            }
        } else
            result.add(mp.getKey(path));

        return result;
    }
}