# 프로젝트설명
요약하자면, **GodDB**는 사용자 친화적인 인터페이스를 가지는 데이터베이스입니다. **LevelDB**와 **Snappy 알고리즘**을 적용하여 만들어진 [SnappyDB](http://www.snappydb.com/)에서 사용한 JNI를 기반으로 하여 인터페이스를 재구현하는 프로젝트입니다. **GodDB**의 인터페이스는 운영체제에서 쉽게 볼 수 있는 디렉토리 구조를 사용하여 좀더 체계적으로 데이터베이스를 관리할 수 있습니다.

# 사용방법
```java
DB godDB = DBManager.open("DB Name");

JSONObject student = new JSONObject();
student.put("name", "GOD");
student.put("age", 20);
student.put("major", "CSE");

godDB.put("/Korea/Busan/PNU", student);

JSONArray retStudents;

retStudent = godDB.get("/Korea/Busan/*", "age==20");
```

# 설치방법
To Add....
