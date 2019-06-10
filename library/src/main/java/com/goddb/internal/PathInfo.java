package com.goddb.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

/**
 * PathInfo: mapping table element class
 *
 * key: 현재 directory(path) key ( -1(or <0) : invalid )
 * name: 현재 directory 이름
 * parent: 상위 directory key
 * childs: 하위 directory keys
 *
 * @author MinJae
 *
 */
class PathInfo implements Serializable {
    private static final long serialVersionUID = 2L;
    private final String name;
    private final int parent;
    private int key;
    private ArrayList<Integer> childs;

    public PathInfo(int key, String name, int parent) {
        this.key = key;
        this.name = name;
        this.parent = parent;
        this.childs = new ArrayList<>();
    }

    /*public PathInfo( PathInfo p ) {
        this.key = p.key;
        this.name = p.name;
        this.parent = p.parent;
        this.childs = new ArrayList<>(p.childs);
    }*/
    public boolean addChild(int child) { //
        int i;
        for (i = 0; i < childs.size() && childs.get(i) < child; ++i) ;
        if (i < childs.size() && childs.get(i) == child)
            return false; // 실패
        childs.add(i, child);
        return true;
    }

    public boolean deleteChild(int child) { //
        int i;
        for (i = 0; i < childs.size() && childs.get(i) != child; ++i) ;
        if (i < childs.size()) {
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

    public String getName() {
        return name;
    }

    public int getParent() {
        return parent;
    }

    public ArrayList<Integer> getChilds() {
        return childs;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "key: %d,\tname: %-20s, parent: %d,\tchilds: %s", key, name, parent, childs);
    }
}

