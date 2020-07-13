package com.glamrock.example.chapter2.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class MainApple4 {
    public static void main(String[] args) {
        List<Apple> inventory = Arrays.asList(
                new Apple(80, "green"),
                new Apple(155, "green"),
                new Apple(120, "red")
        );

        // [Apple{color='green', weight=80}, Apple{color='green', weight=155}]
        List<Apple> greenApples2 = filterList(inventory, (Apple a) -> "green".equals(a.getColor()));
        System.out.println(greenApples2);
    }


    public static <T> List<T> filterList(List<T> inventory, Predicate<T> p) {
        List<T> result = new ArrayList<>();
        for (T t : inventory) {
            if (p.test(t)) {
                result.add(t);
            }
        }
        return result;
    }
}
