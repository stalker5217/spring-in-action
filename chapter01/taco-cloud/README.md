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
 