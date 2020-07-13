package com.glamrock.example.stream.grouping;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.*;

public class GroupingExercise {

    enum CaloricLevel { DIET, NORMAL, FAT };

    public static void main(String[] args) {
        System.out.println("Dishes grouped by type and caloric level: " + groupDishedByTypeAndCaloricLevel());
        System.out.println("Most caloric dishes by type: " + mostCaloricDishesByType());
        System.out.println("Most caloric dishes by type: " + mostCaloricDishesByTypeWithoutOprionals());
        System.out.println("Caloric levels by type: " + caloricLevelsByType());
    }

    private static Map<Dish.Type, Map<CaloricLevel, List<Dish>>> groupDishedByTypeAndCaloricLevel() {
        Map<Dish.Type, Map<CaloricLevel, List<Dish>>> dishedByTypeCalroricLevel =
                Dish.menu.stream().collect(
                        groupingBy(Dish::getType,
                                groupingBy(dish -> {
                                    if(dish.getCalories() <= 400)
                                        return CaloricLevel.DIET;
                                    else if (dish.getCalories() < 700)
                                        return CaloricLevel.NORMAL;
                                    else
                                        return CaloricLevel.FAT;
                                })

                        )
                );

        return dishedByTypeCalroricLevel;
    }

    private static Map<Dish.Type, Optional<Dish>> mostCaloricDishesByType() {
        Map<Dish.Type, Optional<Dish>> mostCaloricByType =
                Dish.menu.stream()
                        .collect(groupingBy(Dish::getType,
                            maxBy(comparingInt(Dish::getCalories)))
                        );
        return mostCaloricByType;
    }

    private static Map<Dish.Type, Dish> mostCaloricDishesByTypeWithoutOprionals() {
        Map<Dish.Type, Dish> mostCaloricByType =
                Dish.menu.stream()
                        .collect(groupingBy(Dish::getType,
                                collectingAndThen(
                                        maxBy(comparingInt(Dish::getCalories)), Optional::get)
                                )
                        );
        return mostCaloricByType;
    }

    private static Map<Dish.Type, Set<CaloricLevel>> caloricLevelsByType() {
        return Dish.menu.stream()
                .collect(
                    groupingBy(Dish::getType,
                        mapping(dish -> {
                                if(dish.getCalories() <= 400)
                                    return CaloricLevel.DIET;
                                else if (dish.getCalories() < 700)
                                    return CaloricLevel.NORMAL;
                                else
                                    return CaloricLevel.FAT;
                            }, toSet())
                    )
                );
    }
}
