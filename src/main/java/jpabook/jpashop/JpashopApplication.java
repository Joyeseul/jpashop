package jpabook.jpashop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JpashopApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpashopApplication.class, args);

        /* jar 로 빌드 하는 방법.
        * Terminal 에서
        * ./gradlew clean build : 새롭게 다시 build
        * cd D:\Workspace\jpashop\build\libs : 여기에 jpashop-0.0.1-SNAPSHOT.jar 파일이 있다.
        * java -jar jpashop-0.0.1-SNAPSHOT.jar : jar 파일로 spring 을 실행할 수 있다.
        * */
    }

}
