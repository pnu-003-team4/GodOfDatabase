## 프로젝트설명

요약하자면, **GodDB**는 사용자 친화적인 인터페이스를 가지는 데이터베이스입니다.

**LevelDB**와 **Snappy 알고리즘**을 적용하여 만들어진 [SnappyDB](http://www.snappydb.com/)에서 사용한 JNI를 기반으로 하여 인터페이스를 재구현하는 프로젝트입니다.

**GodDB**의 인터페이스는 운영체제에서 쉽게 볼 수 있는 디렉토리 구조를 사용하여 더 체계적으로 데이터베이스를 관리할 수 있습니다.

복잡한 **SQL**을 짜지말고, 와일드카드를 통해 **강력한 검색기능**을 가져보세요!

## 사용예시

```java
DB godDB = DBManager.open("DB Name");

JSONObject student = new JSONObject();
student.put("name", "GOD");
student.put("age", 20);
student.put("major", "CSE");

godDB.put("/Korea/Busan/PNU", student);

JSONArray retStudents;

retStudent = godDB.get("/Korea/Busan/University/PNU/CSE/*", "age==23");
```

## 와일드카드

GodDB는 **와일드카드**를 도입하여 기존 NoSQL 데이터베이스들과는 달리 강력한 검색기능을 구현하였습니다.

GodDB가 제공하는 **와일드카드**의 종류에는 총 3가지가 있습니다.

### " * " : 하위 모든 디렉토리 선택

##### 사용예시

 └─\/ 
    ├─Korea
    │  ├─Busan
    │  │  ├─University
    │  │  │  ├─PNU
    │  │  │  │  ├─Faculty
    │  │  │  │  │      faculty1 object
    │  │  │  │  └─ CSE
    │  │  │  │     ├─Freshman
    │  │  │  │     │      student1 object
    │  │  │  │     │      student2 object
    │  │  │  │     ├─Sophomore
    │  │  │  │     │      student1 object
    │  │  │  │     │      student2 object
    │  │  │  │     ├─Junior
    │  │  │  │     │      student1 object
    │  │  │  │     │      student2 object
    │  │  │  │     └─Senior
    │  │  │  │            student1 object
    │  │  │  │            student2 object
    │  │  │  ├─Dong-a
    │  │  │  │  └─ ...
    │  │  │  └─PKNU
    │  │  │     └─ ...
    │  │  └─Company
    │  │     └─ ...
    │  └─Seoul
    └─Japan
    ...
 
 
```java
JSONArray retStudents;

retStudent = godDB.get("/Korea/Busan/University/PNU/*");
```
// retStudent에는 **부산대학교**의 모든오브젝트들이 담겨있다.

### " \# " : 하위 모든 디렉토리의 특정 경로의 디렉토리 선택

##### 사용예시

```java
JSONArray retStudents;

retStudent = godDB.get("/Korea/Busan/University/#/CSE/Freshman");
```
// retStudent에는 **부산의 모든대학교(PNU, Dong-a, PKNU)의 정보컴퓨터공학부의 1학년**인 오브젝트가 담겨있다.


### " & " : 다중 디렉토리 선택

##### 사용예시

```java
JSONArray retStudents;
String myPath = "/Korea/Busan/University/PNU/CSE";

//자바는 연산자 오버로딩을 지원하지 않는다.
retStudent = godDB.get(myPath + "/Freshman  & " + myPath + "/Junior");
```
// retStudent에는 부산대학교 정보컴퓨터공학부의 **1학년과 3학년**인 오브젝트들이 담겨있다.

## 조건문

GodDB는 또한 조건문을 제공합니다.

조건의 종류에는 총 4가지가 있습니다.

### " == " : 해당 항목이 일치하는 오브젝트들을 찾습니다.

##### 사용예시

```java
JSONArray retStudents;

retStudent = godDB.get("/Korea/Busan/University/PNU/*", "age==23");
```
// retStudent에는 부산대학교에서 **나이가 23세인** 오브젝트들이 담겨있다.

### " > " : 해당 항목보다 큰 값을 가지는 오브젝트들을 찾습니다.

##### 사용예시

```java
JSONArray retStudents;

retStudent = godDB.get("/Korea/Busan/University/PNU/*", "age>23");
```
// retStudent에는 부산대학교에서 **나이가 23세보다 많은** 오브젝트들이 담겨있다.

### " < " : 해당 항목보다 작은 값을 가지는 오브젝트들을 찾습니다.

##### 사용예시

```java
JSONArray retStudents;

retStudent = godDB.get("/Korea/Busan/University/PNU/CSE/*", "age<23");
```
// retStudent에는 부산대학교에서 **나이가 23세보다 적은** 오브젝트들이 담겨있다.

### " - " : 특정 조건을 제외하고 찾습니다.

##### 사용예시

```java
JSONArray retStudents;

retStudent = godDB.get("/Korea/Busan/University/PNU/CSE/*", "-age<23");
```
// retStudent에는 **나이가 23세 미만이 아닌(23세 이상인)** 오브젝트들이 담겨있다.

## 옵션

마지막으로 GodDB는 옵션을 설정할 수 있습니다.

옵션의 종류에는 총 2가지가 있습니다.

### " << " : 결과를 오름차순으로 정렬합니다.

##### 사용예시

```java
JSONArray retStudents;

retStudent = godDB.get("/Korea/Busan/University/PNU/CSE/*", "age>23", "age<<");
```

### " >> " : 결과를 내림차순으로 정렬합니다.

##### 사용예시

```java
JSONArray retStudents;

retStudent = godDB.get("/Korea/Busan/University/PNU/CSE/*", "age>23", "age>>");
```

## 설치방법
To Add...
