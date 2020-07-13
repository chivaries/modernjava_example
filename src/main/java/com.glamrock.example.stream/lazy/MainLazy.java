package com.glamrock.example.stream.lazy;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class MainLazy {
    public static void main(String[] args) {
//        Stream.iterate(0, i -> i + 1)
//                .map(i -> i + 1)
//                .peek(i -> System.out.println("Map: " + i))
//                .limit(5)
//                .forEach(i -> {});
//
//        System.out.println();
//        System.out.println();
//
//        Stream.iterate(0, i -> i + 1)
//                .limit(5)
//                .map(i -> i + 1)
//                .peek(i -> System.out.println("Map: " + i))
//                .forEach(i -> {});
//
//        System.out.println();
//        System.out.println();
//
//        Stream.iterate(0, i -> i + 1)
//                .flatMap(i -> Stream.of(i, i, i, i))
//                .map(i -> i + 1)
//                .peek(i -> System.out.println("Map: " + i))
//                .limit(5)
//                .forEach(i -> {});
//
//        System.out.println();
//        System.out.println();
//
//        Stream.iterate(0, i -> i + 1)
//                .flatMap(i -> Stream.of(i, i, i, i))
//                .limit(5)
//                .map(i -> i + 1)
//                .peek(i -> System.out.println("Map: " + i))
//                .forEach(i -> {});
//
//        long startTime = System.currentTimeMillis();
//
//        getValueUsingMethodResult(true, getExpensiveValue());
//        getValueUsingMethodResult(false, getExpensiveValue());
//        getValueUsingMethodResult(false, getExpensiveValue());
//
//        System.out.println("eager evaluation passed Time: "+ (System.currentTimeMillis()-startTime)/1000+"sec" );
//
//        long startTime2 = System.currentTimeMillis();
//
//        getValueUsingSupplier(true, () -> getExpensiveValue());
//        getValueUsingSupplier(false, () -> getExpensiveValue());
//        getValueUsingSupplier(false, () -> getExpensiveValue());
//
//        System.out.println("lazy evaluation passed Time: "+ (System.currentTimeMillis()-startTime2)/1000+"sec" );

    }

    private static String getExpensiveValue() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "Hello World";
    }

    private static void getValueUsingMethodResult(boolean valid, String value) {
        if(valid)
            System.out.println("Success: The value is "+ value);
        else
            System.out.println("Failed: Invalid action");
    }

    private static void getValueUsingSupplier(boolean valid, Supplier<String> valueSupplier) {
        if (valid)
            System.out.println("Success: The value is " + valueSupplier.get());
        else
            System.out.println("Failed: Invalid action");
    }


}
