import java.util.List;
//branch test1 test2

public class Wildcard {
    public static String extractWildcard(List minjae, String path) {
        // 변수
        path = "Korea/Pusan/Haeundae/*";
        String wild = "";
        String WildComponent = "";
        boolean IsWild = false;

        int pathlength = path.length();
        int wildlocation = 0;
        if (path.indexOf("#") > -1) {
            wild = "#";
            wildlocation = path.indexOf("#");
            IsWild = true;
        } else if (path.indexOf("*") > -1) {
            wild = "*";
            wildlocation = path.indexOf("*");
            IsWild = true;
        } else if (path.indexOf("&") > -1) {
            wild = "&";
            wildlocation = path.indexOf("&");
            IsWild = true;
        }

        if (IsWild) {
            System.out.print(wild);
            if (wild == "#") {
                WildComponent = path.substring(wildlocation, pathlength);
                // TODO : 리스트를 전달 받고 WildComponent 에 맞는 키, 밸류 리턴
            }
            if (wild == "*") {
                // TODO : 리스트를 전달 받고 그 하위에있는 키, 밸류를 모두 표출.
            }
            if (wild == "&") {
                int i = 0;
                int zzez = 0;
                while (path.charAt(wildlocation - i) == '/') {
                    i--;
                    zzez = wildlocation - i;
                }
                WildComponent = path.substring(zzez, wildlocation);
                //TODO : 리스트를 전달 받고 다음 & 값을 계속 받아오게 함
            }
        }

        return WildComponent;
    }
}




/*
public class Wildcard {


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