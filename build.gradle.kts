import org.gradle.api.tasks.compile.JavaCompile

plugins {
    java
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "juwoncode"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    /*
        웹 관련 디펜던시
        - Spring Boot Starter Web       : Spring MVC 기반 웹 애플리케이션에 필요한 기능을 제공한다.
        - Spring Boot Starter Thymeleaf : 서버 사이드 템플릿 엔진 Thymeleaf을 지원한다.
     */
    implementation("org.springframework.boot:spring-boot-starter-web:3.2.0")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:3.2.0")

    /*
        생산성 관련 디펜던시
        - Spring Boot Devtools                : Automatic Restart, LiveReload 등의 기능을 제공하여 개발 과정을 쉽게 만들어준다.
        - Spring Boot Starter Validation      : JSR380 기반 입력 데이터 검증 기능을 제공한다.
        - JSON Simple                         : Java 기반으로 JSON 데이터를 파싱하고 생성하는 기능을 제공한다.
        - SpringDoc OpenAPI Starter WebMVC UI : Spring Boot 애플리케이션의 API 문서를 생성하고 웹 인터페이스로 제공한다.
        - Apache Commons Lang 3               : StringUtils등의 다양한 유틸리티 클래스를 제공한다.
        - Project Lombok                      : Getter, Setter, 생성자 등을 어노테이션으로 대체하여 반복적인 코드를 줄이기 위해 사용한다.
     */
    developmentOnly("org.springframework.boot:spring-boot-devtools:3.2.0")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.2.0")
    implementation("com.googlecode.json-simple:json-simple:1.1.1")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.4.0")
    implementation("org.apache.commons:commons-lang3:3.14.0")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    compileOnly("org.projectlombok:lombok:1.18.32")
    testCompileOnly("org.projectlombok:lombok:1.18.32")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.32")

    /*
        데이터베이스 관련 디펜던시
        - Spring Boot Data JPA           : JPA와 Hibernate를 사용한 ORM 인터페이스를 제공한다.
        - MariaDB Java Client            : 기본으로 사용할 MariaDB와 통신 기능을 제공한다.
        - Spring Boot Starter Data Redis : 리프레시 토큰을 저장할 인메모리 관계형 데이터베이스 Redis를 지원한다.
        - H2 Database Engine             : 테스트용으로 사용할 가벼운 인메모리 관계형 데이터베이스 H2를 지원한다.
     */
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.2.0")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.4.0")
    implementation("org.springframework.boot:spring-boot-starter-data-redis:3.2.0")
    testImplementation("com.h2database:h2:2.2.224")

    /*
        테스트 관련 디펜던시
        - Spring Boot Starter Test : JUnit5, AssertJ 등의 라이브러리를 제공하여 Spring Boot 애플리케이션의 테스트를 지원한다.
        - Spring Security Test     : Spring Security가 적용된 애플리케이션의 테스트를 지원한다.

     */
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.0")
    testImplementation("org.springframework.security:spring-security-test:6.2.0")

    /*
        인증 및 보안 관련 디펜던시
        - Spring Boot Starter Security      : 사용자 인증과 권한 부여 기능을 제공한다.
        - Spring Boot Starter Mail          : JavaMail 기반 메일 기능을 지원한다.
        - Spring Boot Starter Oauth2 Client : 소셜 로그인에 필요한 OAuth 2.0 클라이언트 기능을 지원한다.
        - jjwt-api                          : JWT와 관련된 인터페이스와 클래스를 정의한다.
        - jjwt-impl                         : jjwt-api에서 정의한 인터페이스를 실제로 구현한다.
        - jjwt-jackson                      : JWT 페이로드를 JSON 처리 모듈 Jackson을 사용하여 직렬화 및 역직렬화한다.
        - Jasypt                            : 설정파일의 속성 값들을 비밀키를 사용하여 암호화 및 복호화한다.
     */
    implementation("org.springframework.boot:spring-boot-starter-security:3.2.0")
    implementation("org.springframework.boot:spring-boot-starter-mail:3.2.0")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client:3.2.0")
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")
    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5")

    // Spring Boot Starter 관련 Dependency는 스프링 부트 버전으로 통일한다. (3.2.0)
}


tasks.withType<Test> {
    useJUnitPlatform()
}

// 한글 인코딩 설정.
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
