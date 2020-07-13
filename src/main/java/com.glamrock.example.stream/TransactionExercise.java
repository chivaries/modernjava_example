package com.glamrock.example.stream;

import java.util.*;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class TransactionExercise {
    public static void main(String[] args) {
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario", "Milan");
        Trader alan = new Trader("Alan", "Cambridge");
        Trader brian = new Trader("Brian", "Cambridge");

        List<Transaction> transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );

        // 질의 1: 2011년부터 발생한 모든 거래를 찾아 값으로 정렬(작은 값에서 큰 값).
        List<Transaction> tr2011  = transactions.stream()
                .filter(transaction -> transaction.getYear() >= 2011)
                .sorted(Comparator.comparing(Transaction::getValue))
                .collect(toList());

        System.out.println(tr2011);

        // 질의 2: 거래자가 근무하는 모든 고유 도시는?
        List<String> cities = transactions.stream()
                .map(transaction -> transaction.getTrader().getCity())
                .distinct()
                .collect(toList());

        Set<String> cities2 = transactions.stream()
                .map(transaction -> transaction.getTrader().getCity())
                .collect(toSet());

        System.out.println(cities);

        // 질의 3: Cambridge의 모든 거래자를 찾아 이름으로 정렬.
        List<Trader> cambridges = transactions.stream()
                .map(Transaction::getTrader)
                .filter(trader -> trader.getCity().equals("Cambridge"))
                .sorted(Comparator.comparing(Trader::getName))
                .collect(toList());

        System.out.println(cambridges);

        // 질의 4: 알파벳 순으로 정렬된 모든 거래자의 이름 문자열을 반환
        String strNames = transactions.stream()
                .map(transaction -> transaction.getTrader().getName())
                .distinct()
                .sorted()
                .reduce("",(s1, s2) -> s1 + " " + s2);

        String strNames2 = transactions.stream()
                .map(transaction -> transaction.getTrader().getName())
                .distinct()
                .sorted()
                .collect(joining());

        System.out.println(strNames);

        // 질의 5: Milan에 거주하는 거래자가 있는가?
        boolean booleanMilan = transactions.stream()
                .anyMatch(transaction -> transaction.getTrader().getCity().equals("Milan"));

        System.out.println(booleanMilan);

        // 질의 6: Cambridge에 사는 거래자의 모든 거래내역 출력.
        transactions.stream()
                .filter(transaction -> transaction.getTrader().getCity().equals("Cambridge"))
                .map(Transaction::getValue)
                .forEach(System.out::println);

        // 질의 7: 모든 거래에서 최고값은 얼마인가?
        Optional<Integer> optMaxValue = transactions.stream()
                .map(Transaction::getValue)
                .max(Comparator.comparing(Integer::intValue));

        OptionalInt optMaxValue2 = transactions.stream()
                .mapToInt(Transaction::getValue)
                .max();

        System.out.println("Max value : " + optMaxValue.get());

        int maxValue = transactions.stream()
                .map(Transaction::getValue)
                .reduce(0, (a1, a2) -> Integer.max(a1, a2));

        int maxValue2 = transactions.stream()
                .map(Transaction::getValue)
                .reduce(0, Integer::max);

        // 가장 작은 값을 가진 거래 탐색
        Optional<Transaction> smallestTransaction = transactions.stream()
                .min(Comparator.comparing(Transaction::getValue));

        System.out.println(smallestTransaction.map(String::valueOf).orElse("No transaction Found"));
    }
}
