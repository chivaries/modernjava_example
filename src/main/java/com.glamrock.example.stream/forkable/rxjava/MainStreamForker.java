package com.glamrock.example.stream.forkable.rxjava;

import com.glamrock.example.stream.grouping.Dish;
import hu.akarnokd.rxjava2.math.MathFlowable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.flowables.GroupedFlowable;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;

public class MainStreamForker {
    public static void main(String[] args) {

        Stream<Dish> dishStream = Dish.menu.stream();
        //Observable<Dish> observable = Observable.fromIterable(() -> Dish.menu.stream().iterator());

        //Flowable<Dish> flowable = Flowable.fromIterable(dishStream::iterator);
        Flowable<Dish> flowable = Flowable.fromIterable(() ->  Dish.menu.stream().iterator());

        Maybe<String> flowable1 = flowable.map(Dish::getName).reduce((d1, d2) -> d1 + ", " + d2);
        //Single<String> flowable1 = flowable.map(Dish::getName).reduce("", (d1, d2) -> d1 + ", " + d2);

        Flowable<Integer> flowable2 = flowable.map(Dish::getCalories).to(MathFlowable::sumInt);

        Maybe<Dish> flowable3 = flowable.reduce((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2);

        Flowable<GroupedFlowable<Dish.Type, Dish>> groupedFlowable = flowable.groupBy(Dish::getType);

        Single<Map<Dish.Type, Single<List<Dish>>>> groupedSingle = flowable.groupBy(Dish::getType).toMap(GroupedFlowable::getKey, GroupedFlowable::toList);


        flowable1.subscribe(str -> System.out.println("Short menu: " + str));
        flowable2.subscribe(sum -> System.out.println("Total calories: " + sum));
        flowable3.subscribe(mostCaloricDish -> System.out.println("Most Caloric dish: " + mostCaloricDish));
        groupedFlowable.subscribe(grouped -> grouped.subscribe(dish -> System.out.println("Key : " + grouped.getKey() + " value : " + dish.getName())));
        groupedSingle.subscribe(map -> map.get(Dish.Type.FISH).subscribe(list -> list.forEach(System.out::println)));
    }
}
