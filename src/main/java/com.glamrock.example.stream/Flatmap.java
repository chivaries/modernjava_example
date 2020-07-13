package com.glamrock.example.stream;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Flatmap {
    public static void main(String[] args) {
        List<Integer> numbers1 = Arrays.asList(1, 2, 3);
        List<Integer> numbers2 = Arrays.asList(3, 4);

        List<Integer[]> pairs = numbers1.stream()
                .flatMap(a -> numbers2.stream()
                        .map(b -> new Integer[]{a,b})
                ).collect(toList());

        pairs.stream()
                .map(a -> Arrays.toString(a))
                .forEach(System.out::println);

        pairs.forEach(pair -> System.out.printf("(%d, %d)", pair[0], pair[1]));
    }
}
