package tacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 제일 먼저 실행되는 부트스트랩 클래스
 */

/**
 * @SrpingBootApplication은 아래 세 개의 어노테이션의 결합이다
 * ------------------------------------------------------
 * @SpringBootConfiguration: 현재 클래스를 구성 클래스로 지정하며 @Configuration 한 형태이다
 * @EnableAutoConfiguration: autowiring을 활성화한다
 * @ComponentScan: 컴포넌트 검색을 활성화 한다. @Component, @Service, @Controller 등을 찾아 빈에 등록한다
 */
@SpringBootApplication
public class TacoCloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(TacoCloudApplication.class, args);
    }

}
