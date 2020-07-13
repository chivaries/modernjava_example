package com.glamrock.example.stream.collector;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.stream.Collector.Characteristics.CONCURRENT;
import static java.util.stream.Collector.Characteristics.IDENTITY_FINISH;

public class ToListCollector<T> implements Collector<T, List<T>, List<T>> {
    @Override
    public Supplier<List<T>> supplier() {
        // 수집 연산의 시발점
        //return () -> new ArrayList<T>();
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<T>, T> accumulator() {
        // 탐색한 항목을 누적하고 바로 누적자를 고친다.
        //return (list, item) -> list.add(item);
        return List::add;
    }

    @Override
    public BinaryOperator<List<T>> combiner() {
        return (list1, list2) -> { // 두 번째 콘텐츠와 합쳐서 첫 번째 누적자를 고친다.
            list1.addAll(list2);   // 변경된 첫 번째 누적자를 반환한다.
            return list1;
        };
    }

    @Override
    public Function<List<T>, List<T>> finisher() {
        // 누적자 개체가 이미 최종 결과이므로 반환전에 변환 과정이 필요치 않음
        // 항등함수 반환
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        // IDENTITY_FINISH : finisher 과정 생략
        // CONCURRENT : 다중 스레드에서 accumulator 함수를 동시에 호출 가능 (스트림의 병렬 리듀싱 실행 가능)
        return Collections.unmodifiableSet(EnumSet.of(IDENTITY_FINISH, CONCURRENT));
    }
}
