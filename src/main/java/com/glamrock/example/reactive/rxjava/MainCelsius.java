package com.glamrock.example.reactive.rxjava;

import com.glamrock.example.reactive.TempInfo;
import io.reactivex.Observable;

public class MainCelsius {
    public static void main(String[] args) {
        Observable<TempInfo> observable = TempObservable.getCelsiusTemperatures("New York", "Chicago", "San Francisco");
        observable.subscribe(new TempObserver());

        try {
            Thread.sleep(10000L);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
