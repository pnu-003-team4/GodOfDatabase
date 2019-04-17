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
    // key: 현재 directory(path) key
    // name: 현재 directory 이름
    // parent: 상위 directory key
    // childs: 하위 directory keys
    class PathInfo implements Serializable {
        private final int key;
        private final String name;
        private final int parent;
        private ArrayList<Integer> childs;

        public PathInfo(int key, String name, int parent ) {
            this.key = key;
            this.name = name;
            this.parent = parent;
            this.childs = new ArrayList<>();
        }
        public PathInfo(final PathInfo p ) {
            this.key = p.key;
            this.name = p.name;
            this.parent = p.parent;
            this.childs = new ArrayList<>(p.childs);
        }
        public boolean addChild(int child) {
            childs.add(child);
            return true;
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
    public MappingTable(final MappingTable mt) {
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
     * @return key or -1
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
                if (table.get(childkey).name.equals(name[i]) )
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
                if (table.get(childkey).name.equals(name[i]) )
                    break;
            }
            if( j == table.get(key).childs.size() ) {	// 존재하지 않으면 path를 추가함
                int index = table.size();
                table.add(new PathInfo(index, name[i], key));
                table.get(key).addChild(index);
                key = index;
            }
            else
                key = childkey;
        }
        return key;
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

/*    public static void main(String[] args) {
        //Scanner scanner = new Scanner(System.in);
        //String path = scanner.next();
        //logger.info("");

        MappingTable mt = new MappingTable();
        mt.addPathAndGetKey("/Busan");
        mt.addPathAndGetKey("/Busan/university");
        mt.addPathAndGetKey("/Busan/university/PNU");
        mt.addPathAndGetKey("/Seoul");
        mt.addPathAndGetKey("/Busan/A/B");
        mt.print();
        System.out.println(mt);
        System.out.println(mt.getKey("/"));
        System.out.println(mt.getKey("/Busan"));
        System.out.println(mt.getKey("/Busan/university"));
        System.out.println(mt.getKey("/Busan/university/PNU"));
        System.out.println(mt.getKey("/Seoul"));
        System.out.println(mt.getKey("/Busan/A"));
        System.out.println(mt.getKey("/Busan/A/B"));

        MappingTable mt2 = new MappingTable(mt);
        mt2.addPathAndGetKey("/Seo");
        System.out.println(mt2);
        System.out.println(mt);
    }*/

}