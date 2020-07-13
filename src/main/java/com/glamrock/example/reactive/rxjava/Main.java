package com.glamrock.example.reactive.rxjava;

import com.glamrock.example.reactive.TempInfo;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class Main {

  public static void main(String[] args) {
//    Observable<Long> onePerSec = Observable.interval(1, TimeUnit.SECONDS);
//
//    onePerSec.blockingSubscribe(i -> System.out.println(TempInfo.fetch("New York")));

    Observable<TempInfo> observable = TempObservable.getTemperature("New York");
    observable.subscribe(new TempObserver());

    try {
      Thread.sleep(10000L);
    }
    catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

}
