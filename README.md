# spring-backend-template

## Backend API Service Template with SpringBoot3

This is just an application created with SpringBoot3,
you can just develop as you normally would.

## RCM Framework

### Maven repository

```xml
<!-- 생략 -->
<repositories>
  <!-- 생략 -->
  <repository>
    <id>rchemist.releases</id>
    <url>https://github.com/rchemist/rcm-framework-release/raw/main/releases</url>
    <snapshots>
      <enabled>true</enabled>
      <updatePolicy>always</updatePolicy>
    </snapshots>
  </repository>
  <!-- 생략 -->
</repositories>
  <!-- 생략 -->
```

### Modules

- Parent BOM

rcm-framework-bom 에는 해당 프로젝트가 참조해야 하는 다양한 라이브러리의 버전 정보와 SpringBoot3 의 기본 참조 정보가 정의되어 있다. 최상위 pom.xml 의 parent 를 rcm-framework-bom 으로 설정한다.

```xml
<!-- 최상위 pom 에 다음 parent 를 추가해야 한다 -->
<parent>
  <groupId>io.rchemist</groupId>
  <artifactId>rcm-framework-bom</artifactId>
  <version>0.0.1</version>
  <relativePath/> <!-- lookup parent from repository -->
</parent>
```

- Common Modules
  - **common-class-transformer** (필수)<br/> 
    RCM 전용 어노테이션 및 Template Class 와 같은 ClassTransform 을 사용하려면 이 모듈을 참조해야 한다.<br/>
    common-utils 가 내장 되어 있다.
    <br/><br/>
  - **common-data** (필수)<br/>
    Data 처리 및 Web 요청(Request/Response) 처리, Search 에 대한 필수적 기능 제공<br/>
    common-class-transformer, common-documentation 이 내장 되어 있다.
    <br/><br/>
  - common-data-atomikos<br/>
    Atomikos 트랜잭션을 사용하기 위한 모듈
    <br/><br/>
  - **common-data-jpa**<br/>
    SpringDataJPA(Hibernate) 를 사용하기 위한 모듈<br/>
    **RDB를 사용할 때 반드시 참조한다.** 
    <br/><br/>
  - **common-data-mongo**<br/>
    SpringDataMongodb 를 사용하기 위한 모듈<br/>
    **mongodb를 사용할 때 반드시 참조한다.**
    <br/><br/>
  - **common-documentation**<br/>
    RCM 자동 문서 작성 처리를 위한 모듈<br/>
    @Document @Description 등 전용 어노테이션에 의한 자동 문서화 지원
    <br/><br/>
  - **common-extension-manager**<br/>
    Proxy ExtensionManager 를 사용하려면 이 모듈을 추가한다.<br/> 
    common-data 에 기본 내장됨<br/><br/>
  - **common-framework**<br/>
    SpringWebMVC 프로젝트를 구성하는 경우 이 모듈을 사용한다.<br/>
    common-data 가 내장되어 있다<br/><br/>
  - **common-security**<br/>
    SpringSecurity 에 의한 보안 처리를 하려면 이 모듈을 참조한다.<br/><br/>
  - **common-utils** (필수)<br/>
    RCM Framework 에서 제공하는 다양한 Utility library<br/>
    common-extension-manager 가 내장되어 있다.<br/><br/>
  - **common-webflux**<br/>
    SpringWebflux 프로젝트를 구성하는 경우 common-framework 대신 common-webflux 를 사용한다.<br/>
    common-data 를 내장하고 있다.



## What you should do

- application*.yml 에서 DB, REDIS, Elasticache 관련 정보를 변경해야 한다.

### Database

- application*.yml

```yaml
spring:
  datasource:
    rcm-rdb:
      # EntityManager 를 설정할 때 사용한 bean 이름
      # io.rchemist.common.config.CommonEntityManagerSetting.DATA_SOURCE_NAME 과 일치해야 한다.
      common:
        driver-class-name: com.mysql.cj.jdbc.Driver #org.mariadb.jdbc.Driver
        # SpringDataJPA 의 EntityManager 설정에서 property 가 제대로 지정되지 않는 버그가 있음. jbdc-url 과 url 을 모두 동일한 값으로 지정해야 함  
        jdbc-url: jdbc:mysql://${MYSQL_ADDRESS}/${DATABASE}?useUnicode=yes&characterEncoding=UTF-8&serverTimezone=Asia/Seoul&zeroDateTimeBehavior=convertToNull
        url: jdbc:mysql://${MYSQL_ADDRESS}/${DATABASE}?useUnicode=yes&characterEncoding=UTF-8&serverTimezone=Asia/Seoul&zeroDateTimeBehavior=convertToNull
        # mariadb 를 사용하는 경우 jdbc:mariadb 를 사용함
        # jdbc-url: jdbc:mariadb://${MYSQL_ADDRESS}/${DATABASE}?useUnicode=yes&characterEncoding=UTF-8&serverTimezone=Asia/Seoul&zeroDateTimeBehavior=convertToNull
        # url: jdbc:mariadb://${MYSQL_ADDRESS}/${DATABASE}?useUnicode=yes&characterEncoding=UTF-8&serverTimezone=Asia/Seoul&zeroDateTimeBehavior=convertToNull
        username: ${USERNAME}
        password: ${PASSWORD}
```

<br/>
<br/>

### REDIS

SpringDataRedis 를 사용하기 위한 정보 설정

- application*.yml

```yaml
spring:
  data:
    redis:
      host: ${REDIS_URL} #host: 152.67.208.203
      port: ${REDIS_PORT}
      database: ${DB_NUMBER}
      password: ${PASSWORD}
```

<br/>

REDISSON 2차 캐시를 사용하기 위한 정보 설정

- redisson-*.yml

```yaml
singleServerConfig:
  password: "${PASSWORD}"
  address: "redis://${REDIS_URL}"
  database: ${DB_NUMBER}
```

### HibernateSearch

검색엔진으로 ElasticSearch 와 HibernateSearch 를 사용하려면 다음과 같이 설정한다.

- bootstrap.yml

```yaml
platform:
  provider:
    #이 값이 QUERY 일 때는 DB 자체 검색 사용, HIBERNATE 일 때는 ES + HibernateSearch 사용 
    search: HIBERNATE 
```

<br/>

ES + HibernateSearch 를 사용하는 경우 관련 설정을 반드시 application*.yml 에 해야 한다.

```yaml
spring:
  # SpringDataES 를 사용하기 위한 정보
  elasticsearch:
    rest:
      port: ${ES_PORT}
      host: ${ES_URL}  # ES 주소, http/https 프로토콜 제외한 순수 URL
      username: ${USERNAME}  # ES 접속 사용자 이름
      password: ${PASSWORD}  # ES 접속 비밀번호
  # HibernateSearch 를 사용하기 위한 정보
  jpa:
    properties:
      hibernate:
        search:
          enabled: true # 이 값이 false 인 경우 HibernateSearch 를 사용하지 않음
          default:
            elasticsearch:
              port: ${ES_PORT} #ES 접속 포트
              host: ${ES_URL} #ES 접속 주소
              required_index_status: yellow
          configuration_property_checking:
            strategy: ignore    # 설정 정보가 unused 상태인 경우에도 별도의 warning 을 하지 않도록 변경
          schema_management:
            #strategy: drop-and-create #create-or-update # drop-and-create   # 개발 중일 때만 drop-and-create 를 한다. analyzer 가 변경되면 기존 스키마를 지워야 하기 때문이다.
            strategy: drop-and-create #drop-and-create #create-or-update
          backend:
            schema_management:
              minimal_required_status: yellow
              minimal_required_status_wait_timeout: 100000
            layout:
              strategy: io.rchemist.common.config.HibernateSearchIndexNameStrategy
            username: ${USERNAME}   # ES 접속 사용자 이름
            password: ${PASSWORD}   # ES 접속 비밀번호
            index_name_prefix: ${ES_PREFIX}  # ES Document 를 생성할 때 prefix - 하나의 ES 에서 여러 서비스를 운영할 때 서비스 별로 prefix 를 붙여 준다.
            connection_timeout: 3000
            read_timeout: 12000
            version: ${ES_VERSION}  # ES 버전
            version_check:
              enabled: false
            uris: https://${ES_URL}   # ES 접속 주소
            analysis:
              configurer: class:io.rchemist.common.search.config.ElasticsearchKoreanAnalyzerConfigurer
```

## Run application

- Maven install
```shell
mvn install # auto clean included
```

- VM options
```bash
--add-opens java.base/java.lang=ALL-UNNAMED -javaagent:library/spring-instrument-6.0.7.jar
```