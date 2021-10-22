package jpastudy.jpashop;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupByTest {
    @Test
    public void groupby() {
        List<Dish> dishList = Arrays.asList(new Dish("pork", 700, Type.MEAT), new Dish("떡볶이", 70, Type.VEGE));

        // Dish의 이름만 출력
        List<String> nameList = dishList.stream()
                .map(Dish::getName)
                .collect(Collectors.toList());

        nameList.forEach(dishName -> System.out.println(dishName));

        // Dish 이름을 구분자를 포함한 문자열로 출력
        String nameStrs = dishList.stream()
                .map(dish -> dish.getName())
                .collect(Collectors.joining(","));
        System.out.println(nameStrs);

        // Dish 칼로리 합계, 평균
        Integer totalCalorie = dishList.stream()
                .collect(Collectors.summingInt(dish -> dish.getCalorie()));
        System.out.println(totalCalorie);

        IntSummaryStatistics statistics = dishList.stream()
                .collect(Collectors.summarizingInt(Dish::getCalorie));
        System.out.println(statistics);

        // Dish의 Type별로 그룹핑
        Map<Type, List<Dish>> dishesByType = dishList.stream()
                .collect(Collectors.groupingBy(dish -> dish.getType()));
        System.out.println(dishesByType);
    }

    static class Dish {
        String name;
        int calorie;
        Type type;

        public Dish(String name, int calorie, Type type) {
            this.name = name;
            this.calorie = calorie;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public int getCalorie() {
            return calorie;
        }

        public Type getType() {
            return type;
        }

        @Override
        public String toString() {
            return "Dish{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    enum Type {
        MEAT, NOODLE, VEGE
    }
}
