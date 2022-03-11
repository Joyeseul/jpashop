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

        /* UTF-8 로 인코딩 되어있는지 확인해봐야 함
        * default encoding: UTF-8
        * file encoding : with NO BOM
        * */
    }

}
