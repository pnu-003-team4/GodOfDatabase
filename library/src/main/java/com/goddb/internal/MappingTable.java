package com.goddb.internal;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class MappingTable implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger("logger");

    // PathInfo: mapping table element class
    // key: 현재 directory(path) key ( -1(or <0) : invalid )
    // name: 현재 directory 이름
    // parent: 상위 directory key
    // childs: 하위 directory keys
    private class PathInfo implements Serializable {
        private int key;
        private final String name;
        private final int parent;
        private ArrayList<Integer> childs;

        public PathInfo(int key, String name, int parent ) {
            this.key = key;
            this.name = name;
            this.parent = parent;
            this.childs = new ArrayList<>();
        }
        public PathInfo( PathInfo p ) {
            this.key = p.key;
            this.name = p.name;
            this.parent = p.parent;
            this.childs = new ArrayList<>(p.childs);
        }
        public void addChild(int child) {
            childs.add(child);
        }
        public void keyToInvalid() {
            key = -1;
        }
        public boolean isInvalid() {
            return key < 0;
        }
        @Override
        public String toString() {
            return String.format(Locale.US, "key: %d,\tname: %-20s, parent: %d,\tchilds: %s", key, name, parent, childs);
        }
    }
    private ArrayList<PathInfo> table;

    public MappingTable() {
        table = new ArrayList<>();
        table.add(new PathInfo(0,"/",-1)); // root
    }
    public MappingTable( final MappingTable mt) {
        table = new ArrayList<>(mt.table);
    }
    /**
     * read file
     *
     * @param fileName This is the file which has mapping table.
     */
    public MappingTable(String fileName) {
    	/*
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
            table = (ArrayList<PathInfo>) ois.readObject();
            ois.close();
        } catch (IOException ex) {
            //Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            //Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
    /**
     * save mapping table to file
     *
     * @param fileName This is the file which will save mapping table.
     */
    public void saveToFile(String fileName) {
    	/*
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
            oos.writeObject(table);
            oos.close();
        } catch (IOException ex) {
            //Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        } */
    }

    /**
     * path -> key
     * 존재하지 않는 path는 추가하지 않고 -1를 리턴함
     *
     * @param path like /korea/busan/pnu
     * @return key or -1(path doesn't exist)
     */
    public int getKey(String path) {
        // path: /korea/busan/pnu -> name: [ root, korea, busan, pnu ]
        String[] name;
        Pattern pattern = Pattern.compile("/");
        name = pattern.split(path);
        int key = 0;
        // 해당 key에서의 path와 일치하는 child를 찾음
        for(int i=1; i<name.length; ++i) {	//
            int j;
            int childkey = 0;
            for(j=0; j<table.get(key).childs.size(); ++j) {
                childkey = table.get(key).childs.get(j);
                if (table.get(childkey).name.equals(name[i]) && !table.get(childkey).isInvalid() )
                    break;
            }
            if( j == table.get(key).childs.size() )
                return -1;	// table에  해당 path가 존재하지 않음
            else
                key = childkey;
        }

        return key;
    }

    /**
     * put 할 때 쓰길 권장
     * 존재하지 않는 path는 mapping table에 추가함
     *
     * @param path like /korea/busan/pnu
     * @return key
     */
    public int addPathAndGetKey(String path) {
        String[] name;
        Pattern pattern = Pattern.compile("/");
        name = pattern.split(path);
        int key = 0;
        for(int i=1; i<name.length; ++i) {
            int j;
            int childkey = 0;
            for(j=0; j<table.get(key).childs.size(); ++j) {
                childkey = table.get(key).childs.get(j);
                if (table.get(childkey).name.equals(name[i]) && !table.get(childkey).isInvalid() )
                    break;
            }
            if( j == table.get(key).childs.size() ) {	// 존재하지 않으면 path를 추가함
                int index = findInvalidIndex();
                if( index < 0 ) {
                    index = table.size();
                    table.add(new PathInfo(index, name[i], key));
                }
                else {
                    table.remove(index);
                    table.add(index, new PathInfo(index, name[i], key));
                }
                table.get(key).addChild(index);
                key = index;
            }
            else
                key = childkey;
        }
        return key;
    }
    /**
     * used for addPathAndGetKey to find index which is invalid
     * @return index or -1
     */
    private int findInvalidIndex() {
        int index = -1;
        for( int i=0; i<table.size(); ++i) {
            if(table.get(i).isInvalid() ) {
                index = i;
                break;
            }
        }
        return index;
    }


    /**
     * 현 path의 key로 child keys 얻기
     *
     * @param key This is current key.
     * @return child keys
     */
    public ArrayList<Integer> getChildKeys(int key) {
        return new ArrayList<>(table.get(key).childs);
    }
    /**
     * 현 path의 key로 parent key 얻기
     *
     * @param key This is current key.
     * @return parent key
     */
    public int getParentKey(int key) {
        return table.get(key).parent;
    }
    /**
     * mapping table에 존재하는 path를 삭제합니다.
     * 하위 directory(path)도 같이 삭제합니다.
     * @param path which You want to delete.
     * @return delete is success. (path: exist)
     */
    public boolean deletePath(String path) {
        int key = getKey(path);
        return deleteKey(key);
    }
    private boolean deleteKey(int key) {
        if( key < 0 )
            return false;
        ArrayList<Integer> childkeys = getChildKeys(key);
        for( int i =0; i<childkeys.size(); ++i) {
            deleteKey(childkeys.get(i));
        }
        table.get(key).keyToInvalid();
        return true;
    }

    public boolean PathExists(String path) {
        return getKey(path) >= 0;
    }
    @Override
    public String toString() {
        String str = "";
        for( PathInfo p : table) {
            str += p + "\n";
        }
        return str;
    }
    public void print() {
        for(PathInfo p : table ) {
            logger.info(p.toString());
            //System.out.println(p);
        }
    }


}