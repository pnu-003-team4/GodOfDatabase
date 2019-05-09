package com.goddb.internal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

/**
 * [기능]
 * mapping Table 생성 및 관리
 *
 * mapping Path and Key
 *
 * mapping Table은 path들을 directory단위로 저장하는데, 이때 각 directory들은 유일한 key값을 가지고 있음.
 * 이 key값을 사용하여 GOD DB에 데이터를 저장함.
 *
 * 만약 table에 path를 추가하였으면, (int addPathAndGetKey(String))
 * path로 부터 key정보를 얻을 수 있음 (int getKey(String))
 *
 * 어떤 path가 table에 저장 되어있는지 여부는 PathExists() 통해 확인 가능. (boolean PathExists(String))
 *
 * 원하는 path의 부모 directory(path) key를 해당 path의 key 정보를 이용하여 얻을 수 있음 (int getParentKey(int))
 * 원하는 path의 자식 directory(path)들의 key를  해당 path의 key 정보를 이용하여 얻을 수 있음 (ArrayList<Integer> getChildKeys(int)
 *
 * path 정보를 table에서 지울 수 있음 (boolean deletePath(String))
 *
 * @author MinJae
 *
 */
public class MappingTable implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger("logger");

    // PathInfo: mapping table element class
    // key: 현재 directory(path) key ( -1(or <0) : invalid )
    // name: 현재 directory 이름
    // parent: 상위 directory key
    // childs: 하위 directory keys
    private class PathInfo implements Serializable {
        private static final long serialVersionUID = 2L;
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
        /*public PathInfo( PathInfo p ) { // not need
            this.key = p.key;
            this.name = p.name;
            this.parent = p.parent;
            this.childs = new ArrayList<>(p.childs);
        }*/
        public boolean addChild(int child) { //
            int i;
            for(i=0; i<childs.size() && childs.get(i)<child; ++i );
            if(i<childs.size() && childs.get(i)==child)
                return false; // 실패
            childs.add(i,child);
            return true;
        }
        public boolean deleteChild(int child) { //
            int i;
            for(i=0; i<childs.size() && !(childs.get(i)==child); ++i ) ;
            if(i<childs.size()) {
                childs.remove(i);
                return true;
            }
            return false; // 실패
        }
        public void keyToInvalid() { // key가 delete될 때
            key = -1;
        }
        public boolean isInvalid() { // delete된 index 알아낼 때 필요 (findInvalidIndex)
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
        table.add(new PathInfo(0,"<root>",-1)); // root
    }
    /*public MappingTable( final MappingTable mt) {	// not need
    	table = new ArrayList<>(mt.table);
    }*/
    /**
     * constructor with read file
     *
     * @param fileName This is the file with mapping table.
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ClassNotFoundException
     */
    public MappingTable(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
        table = new ArrayList<>();
        readFile(fileName);
    }
    /**
     * read file
     *
     * @param fileName This is the file with mapping table.
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public void readFile(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
        table = (ArrayList<PathInfo>) ois.readObject();
        ois.close();
    }
    /**
     * save mapping table to file
     *
     * @param fileName This is the file to save mapping table.
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void saveToFile(String fileName) throws FileNotFoundException, IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
        oos.writeObject(table);
        oos.close();
    }

    /**
     * path -> key
     * if path doesn't exist : return -1
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
                if (table.get(childkey).name.equals(name[i]))
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
     * It is recommended to use it for PUT function.
     * if path doesn't exist : add the path to mapping table and return its key
     * if path exists : same as getKey function ( just return key )
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
                if (table.get(childkey).name.equals(name[i]))
                    break;
            }
            if( j == table.get(key).childs.size() ) {	// 존재하지 않으면 path를 추가함
                int index = findInvalidIndex();
                if( index < 0 ) {
                    index = table.size();
                    table.add(new PathInfo(index, name[i], key));
                }
                else { //delete된  key가 존재
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
            if(table.get(i).isInvalid() ) { // delete된 key가 존재
                index = i;
                break;
            }
        }
        return index;
    }


    /**
     * get child keys by using the current key
     *
     * @param key This is current key.
     * @return child keys
     */
    public ArrayList<Integer> getChildKeys(int key) {
        if(key < 0) // exception
            return new ArrayList<>();
        return new ArrayList<>(table.get(key).childs);
    }
    /**
     * get parent key by using the current key
     *
     * @param key This is current key.
     * @return parent key
     */
    public int getParentKey(int key) {
        if(key < 0) // exception
            return key;
        return table.get(key).parent;
    }
    /**
     * Delete the path that exists in the mapping table.
     * Also delete all subdirectories (paths).
     * @param path which You want to delete.
     * @return delete is success. (path: exist)
     */
    public boolean deletePath(String path) {
        int key = getKey(path);
        if( key < 0 )
            return false;
        int parentKey = getParentKey(key);
        if( parentKey < 0 ) // root를 지울 순 없음.
            return false;
        return table.get(parentKey).deleteChild(key) && __deleteKey(key);
    }
    private boolean __deleteKey(int key) {
        if( key < 0 ) // exception
            return false;
        ArrayList<Integer> childkeys = getChildKeys(key);
        for( int i =0; i<childkeys.size(); ++i) {
            __deleteKey(childkeys.get(i));
        }
        table.get(key).keyToInvalid();
        return true;
    }
    /**
     * Check that path is in mapping table
     *
     * @param path like /korea/busan/pnu
     * @return path exist
     */
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