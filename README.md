# JSP_Project_Bootstrap

JSP 개발을 용이하게 진행하기 위하여 제작한 프로젝트입니다. 
기본적인 db 연결이 구현되어있고, relflection method를 이용한 API가 구현되어있습니다. 
본 프로젝트는 JSP 프로젝트만을 포함하므로 mysql 서버등은 별도로 구축하여야합니다. 

본 프로젝트는 intelliJ에서 생성된 프로젝트입니다. 
eclipse에서 작성된 프로젝트는 http://github.com/yoogomja/whistle_JSP/ 링크를 참조해주시기 바랍니다.

# API 사용 규칙
API의 사용방법에 대해서는 ReflectionMethod파일을 참조하면 됩니다. 
필수적인 사용법은 아래와 같습니다.

**localhost기준**

1. GET 요청시 : http://localhost:포트번호/API/서블릿파일이름?fn=함수명&params=매개변수 
2. POST 요청시 : http://localhost:포트번호/API/서블릿파일이름?fn=함수명 
 위 데이터 요청시에는 body에 params을 JSON 형태로 전달하여야합니다.

## 개발 환경 

- IntelliJ
- jdk 12.*
- mysql 8.0.,17
- tomcat 9.0.42

## 개발 환경 세팅 유의사항 

1. javax.* 항목들을 사용하므로 java6이 필요합니다.
2. 현재 포함되어있지 않은 아래와 같은 라이브러리들이 추가로 필요하므로, 아래 서술되는 링크에서 다운로드 받아 포함하여 사용하여야 합니다.
3. 본 프로젝트는 intelliJ에서 작업되었습니다. 

### 1. intelliJ 학생 버전 설치 방법

https://whitepaek.tistory.com/6

위 링크를 참조하여 Ultimate Version을 다운받아 사용합니다. 
졸업 전까지 intelliJ의 Ultimate Version을 사용할 수 있습니다.

### 2. 사용 라이브러리 목록

1. mysql-connector (https://dev.mysql.com/downloads/connector/j/)
: jdbc의 mysql-connector 최신 버전을 사용하고 있습니다. platform independent 버전을 다운받아 사용합니다.

2. json-simple (https://repo1.maven.org/maven2/com/googlecode/json-simple/json-simple/1.1.1/json-simple-1.1.1.jar)
: JSON 파싱등을 담당하는 라이브러리입니다. API의 데이터 통신이 대다수 JSON으로 이루어져 반드시 필요합니다. 

## 실행 구조 & 순서

![구조](https://github.com/YOOGOMJA/JSP_Project_Bootstrap/blob/master/readme_images/structure.png?raw=true)

### 1. CommonVariables

공통 변수 / 상수를 관리하는 클래스입니다. 현재는 데이터베이스 관련 정보를 갖고 있습니다.

### 2. DBConnector

db와 연결을 용이하게 하기 위한 클래스입니다. 

### 3. ReflectionMethod

API 구현을 위한 클래스입니다. 쿼리스트링으로 넘겨받은 'fn'값에 들어있는 함수를 찾아 실행합니다. 

## 실행 화면 

![실행화면](https://github.com/YOOGOMJA/JSP_Project_Bootstrap/blob/master/readme_images/result.png?raw=true)

## API 결과 JSON 구조 

```
{
  "RESULT_CD": 1,
  "RESULT_DATA": {
    // 실제 데이터 부
    "METHOD_RESULT_DATA": [
      {
        "idUser": "1",
        "name": "Person_1",
        "hp": "010-1234-1234",
        "email": "person_1@likelion.org"
      },
      {
        "idUser": "2",
        "name": "Person_2",
        "hp": "010-1234-1234",
        "email": "person_1@likelion.org"
      },
      // ...
    ],
    "METHOD_RESULT_CD": 1
  },
  "RESULT": "SUCCESS"
}
```

### 주의점 

: 본 프로젝트는 API를 분리하여 사용하고 있으므로 비동기 요청 및 처리 방법을 따로 숙지해야합니다. 
: CommonVariables 클래스의 커넥션 스트링에서 타임존 쿼리스트링은 남겨두어야합니다. 제거시 톰캣 버전에 따라 오류를 일으킬 수 있습니다.




