package com.glamrock.example.stream.hashmap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HashMapExample {
    public static void main(String[] args) {
        Map<String, Integer> movies = new HashMap<>();
        movies.put("JamesBond", 20);
        movies.put("Matrix", 15);
        movies.put("Harry Potter", 5);
        Iterator<Map.Entry<String, Integer>> iterator = movies.entrySet().iterator();

        while(iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            if(entry.getValue() < 10) {
                iterator.remove();
            }
        }

        System.out.println(movies);

        movies.entrySet().removeIf(entry -> entry.getValue() < 10);

        System.out.println(movies);

    }
}
