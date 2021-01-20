# 스프링 시작하기

Spring 프로젝트는 Spring Initializr를 사용하면 쉽게 기본 구조를 생성할 수 있다.

[Spring Initializr](https://start.spring.io/)

위 링크를 통해 만들 수도 있다. 
그러나 이는 REST 기반의 API 시스템이기에 curl 등의 툴로 호출하여 만들수도 있고 IDE에 내장되어 있기도 하다.

기본적으로 생성되는 파일들은 아래와 같다.
- mvnw, mvnw.cmd : 메이븐 wrapper 스크립트로 메이븐 설치 없이 프로젝트를 빌드할수 있게 한다.
- pom.xml : 메이븐 빌드 명세 파일.
- application.properties : 프로젝트의 속성 값을 지정.
- template : 브라우저에 제공할 템플릿 파일(mustache, thymeleaf 등)의 위치.
- TacoCloudApplication : 스프링 부트를 시작하는 메인 클래스.
- TacoCloudApplicationTests : 스프링 애플리케이션의 로드를 검증하는 테스트 클래스.

 ## pom.xml

POM는 'Project Object Model'을 의미하며 프로젝트의 구조를 묘사하며, 
프로젝트의 의존성 설정 및 빌드에 관한 정보를 관리한다. 

``` xml
<parent>
    <!-- Project Object Model 지정-->
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.4.2</version>
    <relativePath/> <!-- lookup parent from repository -->
</parent>
```

parent는 부모 pom을 지정한다. 
이 프로젝트는 스프링부트 프로젝트로 여기서 그 버전을 지정한다.

``` xml
<!-- 의존성 정의 -->
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

dependencies는 의존성을 관리한다. 
프로젝트에서 필요로 하는 일련의 라이브러리들이 jar 파일을 직접 위치시켜서 사용하지 않고 편리하게 관리할 수 있다.

간단하게 틀을 만든 이 프로젝트에서의 하위 의존성을 몇 가지 확인해본다.

1. srping-boot-devtools  

- 코드가 변경될 때 자동으로 애플리케이션을 다시 시작시킨다. 클래스 로더만 다시 로드하고 스프링 애플리케이션 컨텍스트를 다시 시작한다. (의존성 변경 제외)
- 브라우저로 전송되는 템플릿, 스크립트 등의 캐시를 비활성화하고 수정이 발생하면 브라우저를 새록 고침한다.
- 개발용으로 H2 DB를 사용하면 H2 콘솔을 자동으로 활성화한다.

2. starter  

스타터 의존성 같은 경우에는 특정 기능을 사용하기 위한 것이다. 
스타터 각각은 사실 여러 개의 의존성으로 구성되어 있고 이들이 모여 특정 기능을 사용할 수 있도록 한다. 
필요한 모든 라이브러리를 각각 선언하는 것이 아니라 스타터로 한 번에 선언함으로써 관리가 쉬워진다. 
또한, 내부에서 필요한 각 라이브러리의 호환성을 보장을 해준다는 장점도 있다. 


``` xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <excludes>
                    <exclude>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                    </exclude>
                </excludes>
            </configuration>
        </plugin>
    </plugins>
</build>
```

여기서 플러그인은 메이븐을 사용하는 애플리케이션을 실행할 수 있게 한다. 
또한, 의존성에 지정된 라이브러리가 실행 가능한 JAR 파일에 포함되는지 확인하고 
JAR 파일에 부트 스트랩 클래스를 메인 클래스로 사용되게 설정한다. 

## TacoCloudApplication  

``` java
package tacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TacoCloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(TacoCloudApplication.class, args);
    }

}
```

프로젝트를 기본적으로 생성했을 때 만들어지는 클래스이다. 
jar을 실행했을 때 시작되는 부트 스트랩 클래스이며 ```@SpringBootApplication```으로 지정을 한다.

해당 어노테이션은 세 개의 어노테이션 역할을 한다.
- ```@SpringBootConfiguration``` : 현재 클래스를 구성 클래스로 지정하며 @Configuration 한 형태이다.
- ```@EnableAutoConfiguration``` : autowiring을 활성화한다.
- ```@ComponentScan``` : @Component, @Service, @Controller 등을 찾아 빈에 등록한다.