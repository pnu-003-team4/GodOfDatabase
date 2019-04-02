import java.util.ArrayList;
import java.util.regex.Pattern;


public class MappingTable {
    class PathInfo {
        private int key;
        private String name;
        private int parent;
        private ArrayList<Integer> childs;

        public PathInfo(int key, String name, int parent ) {
            this.key = key;
            this.name = name;
            this.parent = parent;
            this.childs = new ArrayList<Integer>();
        }
        public boolean addChild(int child) {
            childs.add(child);
            return true;
        }
    }
    private ArrayList<PathInfo> table;

    public MappingTable() {
        table = new ArrayList<PathInfo>();
    }
    public MappingTable(MappingTable mt) {
        table = mt.table;	//copy...?
    }
    public MappingTable(String filename) {

    }

    /**
     * path -> key
     * 존재하지 않는 path는 추가하지 않고 -1를 리턴함
     *
     * @param path not null.
     * @return key or -1
     */
    public int getKey(String path) {
        // path: /korea/busan/pnu -> name: [ root, korea, busan, pnu ]
        String[] name;
        Pattern pattern = Pattern.compile("/");
        name = pattern.split(path);

        int N = name.length;
        int key = 0;
        // 해당 key에서의 path와 일치하는 child를 찾음
        for(int i=0; i<N; ++i) {
            int j;
            for(j=0; j<table.get(key).childs.size() && !( table.get(key).name.equals(name[i]) ); ++j) {
                key = table.get(key).childs.get(j);
                if( !table.get(key).name.equals(name[i]) )
                    key = table.get(key).parent;
            }
            if( j == table.get(key).childs.size() )
                return -1;	// table에  해당 path가 존재하지 않음
        }

        return key;
    }

    /**
     * put 할 때 쓰길 권장
     * 존재하지 않는 path는 mapping table에 추가함
     *
     * @param path not null.
     * @return key
     */
    public int addPathAndGetKey(String path) {
        String[] name;
        Pattern pattern = Pattern.compile("/");
        name = pattern.split(path);

        int N = name.length;
        int key = 0;
        for(int i=0; i<N; ++i) {
            int j;
            for(j=0; j<table.get(key).childs.size() && !( table.get(key).name.equals(name[i]) ); ++j) {
                key = table.get(key).childs.get(j);
                if( !table.get(key).name.equals(name[i]) )
                    key = table.get(key).parent;
            }
            if( j == table.get(key).childs.size() ) {	// 존재하지 않으면 path를 추가함
                int index = table.size();
                table.add(new PathInfo(index, name[i], key));
                table.get(key).addChild(index);
                key = index;
            }
        }
        return key;
    }

    public int[] getChildsKey(String path) {
        int[] a = {1,2};
        return a;
    }

    public int[] getChildsKey(int key) {
        int[] a = {1,2};
        return a;
    }

    public int getParentKey(String path) {
        int a = 1;
        return a;
    }

    public int getParentKey(int key) {
        int a = 1;
        return a;
    }

    public boolean isPath(String path) {
        return getKey(path) >= 0;
    }
/*
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String path = scanner.next();
        String[] name;
        Pattern pattern = Pattern.compile("/");
        name = pattern.split(path);
        int N = name.length;
        System.out.println(name[1]);
        System.out.print(N);
    }
 */
}