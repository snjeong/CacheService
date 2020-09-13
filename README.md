In-Memory Cache Service
=======================

## 빌드 환경 ##

- JDK 14 - OpenJDK 설치되어 있어야 합니다.

## 빌드 및 실행 ##

- git clone https://github.com/snjeong/cacheservice.git

- cd into cacheservice

- ./mvnw spring-boot:run

## 테스트 ##
- Swagger 를 통해 API 테스트를 수행할 수 있습니다.

  REST-API : http://localhost:8080/swagger-ui.html
  
- H2 메모리DB 콘솔 : http://localhost:8080/h2-console

```
  JDBC URL:	jdbc:h2:mem:testdb;
  
  User Name: sa
```

- 캐시 설정값 변경은 application.properties 를 통해 할수 있습니다. 

```
#카테고리 캐시설정정보

  categorycache.timetoliveSeconds=100   # 데이터 유효기간(단위:초)

  categorycache.timerintervalSeconds=60 # 만료데이터 처리를 위한 쓰레드의 실행 간격(단위:초)

  categorycache.maxItems=10             # 캐시 크기

#상품 캐시설정정보

  productcache.timetoliveSeconds=100

  productcache.timerintervalSeconds=60

  productcache.maxItems=500
```
