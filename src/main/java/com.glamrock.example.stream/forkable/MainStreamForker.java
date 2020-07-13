package com.glamrock.example.stream.forkable;

import com.glamrock.example.stream.grouping.Dish;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.reducing;

public class MainStreamForker {
    public static void main(String[] args) {
        Stream<Dish> menuStream = Dish.menu.stream();

        StreamForker.Results results = new StreamForker<>(menuStream)
                .fork("shortMenu", s -> s.map(Dish::getName).collect(joining(",")))
                .fork("totalCalories", s -> s.mapToInt(Dish::getCalories).sum())
                .fork("mostCaloricDish", s -> s.collect(reducing(
                        (d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2
                )).get())
                .fork("dishByType", s -> s.collect(groupingBy(Dish::getType)))
                .getResults();

        String shortMenu = results.get("shortMenu");
        int totalCalories = results.get("totalCalories");
        Dish mostCaloricDish = results.get("mostCaloricDish");
        Map<Dish.Type, List<Dish>> dishesByType = results.get("dishByType");

        System.out.println("Short menu: " + shortMenu);
        System.out.println("Total calories: " + totalCalories);
        System.out.println("Most Caloric dish: " + mostCaloricDish);
        System.out.println("Dished by type: " + dishesByType);
    }
}
