## 프로젝트설명
요약하자면, **GodDB**는 사용자 친화적인 인터페이스를 가지는 데이터베이스입니다.

**LevelDB**와 **Snappy 알고리즘**을 적용하여 만들어진 [SnappyDB](http://www.snappydb.com/)에서 사용한 JNI를 기반으로 하여 인터페이스를 재구현하는 프로젝트입니다.

**GodDB**의 인터페이스는 운영체제에서 쉽게 볼 수 있는 디렉토리 구조를 사용하여 좀더 체계적으로 데이터베이스를 관리할 수 있습니다.

복잡한 ***SQL***을 짜지말고, **와일드카드**를 통해 ***강력한 검색기능***을 가져보세요!

## 사용예시
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

## 와일드카드
GodDB는 **와일드카드**를 도입하여 기존 NoSQL 데이터베이스들과는 달리 강력한 검색기능을 구현하였습니다.

GodDB가 제공하는 **와일드카드**의 종류에는 총 2가지가 있습니다.

### *
To Add...
#### 사용예시
To Add...

### #
To Add...
#### 사용예시
To Add...


## 조건문
GodDB는 또한 조건문을 제공합니다.

조건의 종류에는 총 3가지가 있습니다.

To Add...

### ==
To Add...
#### 사용예시
To Add...

### >
To Add...
#### 사용예시
To Add...

### <
To Add...
#### 사용예시
To Add...

## 설치방법
To Add...
