package jpastudy.jpashop;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

public class StreamTest {
    @Test
    public void stream() {

        List<User> users = List.of(new User("민주", 25), new User("몽타", 1));

        //User의 Name 추출해서 List<String>으로 반환해서 출력하세요
        List<String> nameList =
                users.stream()      // Stream<User>
                        //.map(user -> user.getName())        // Stream<String>
                        .map(User::getName)     // 메서드 레퍼런스 사용하면 위와 같다
                        .collect(Collectors.toList());      // List<String>

        //List에 담긴 문자열 출력
//        nameList.forEach(name -> System.out.println(name));
        nameList.forEach(System.out::println);

        //20살 이상인 user 추출해서 List<String>으로 반환해서 출력하세요
        //방법1
        users.stream()
                .filter(user -> user.getAge() >= 20)
                .forEach(user -> System.out.println(user.getName()));
        //방법2
        List<String> names = users.stream()
                .filter(user -> user.getAge() >= 20)    // Stream<User>
                .map(user -> user.getName())    // Stream<String>
                .collect(Collectors.toList());  // List<String>
        names.forEach(System.out::println);

        //User들의 나이 합계
        //IntStream
        int sum = users.stream()      //Stream<User>
                .mapToInt(user -> user.getAge())    //IntStream
                .sum();
        System.out.println("나이합계 : "+ sum);
    }

    @Data
    @AllArgsConstructor
    static class User {
        private String name;
        private int age;
    }
}
