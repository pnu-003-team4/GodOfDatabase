package com.goddb.internal;

//branch test1 test2
//test

import java.nio.file.Path;
import java.util.ArrayList;

public class Wildcard {
        public static ArrayList<Integer> Sharp(int PathKey,String RightNode, MappingTable mappingTable) {
            ArrayList<Integer> child;
            ArrayList<Integer> Find =new ArrayList<>();
            child = mappingTable.getChildKeys(PathKey);
            if (mappingTable.getKeyFromKey(PathKey, RightNode) != -1) {
                Find.add(mappingTable.getKeyFromKey(PathKey, RightNode));
            }
            for (int i = 0; i < child.size(); i++) {
                    Find.addAll(Sharp(child.get(i), RightNode, mappingTable));
            }
            return Find;
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
            // wild = "#";
            //wildlocation = path.indexOf("#");
            IsWild = true;

            int SharpLocation = path.indexOf("#");
            String leftnode  = path.substring(0,SharpLocation-1);
            int LeftKey = mappingTable.getKey(leftnode);
            int last = path.length();
            String rightnode = path.substring(SharpLocation+1,last);
            return Sharp(LeftKey,rightnode,mappingTable);

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

        //path = "/Korea/Pusan/Haeundae/*&/Korea/Seoul/#/kangnam&US/Sanhose&??&whycant..&megabox/#/zzez";
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
                // Sharp(path);
            } else if (path.indexOf("*") > -1) {
                wild = "*";
                wildlocation = path.indexOf("*");
                IsWild = true;

            }

        }

        if (IsWild) {
            if (("#").equals(wild)) {

                int SharpLocation = path.indexOf("#");
                String leftnode  = path.substring(0,SharpLocation-1);
                int last = path.length();
                String rightnode = path.substring(SharpLocation+1,last);
                result.addAll(Sharp(mp.getKey(leftnode),rightnode,mp));
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