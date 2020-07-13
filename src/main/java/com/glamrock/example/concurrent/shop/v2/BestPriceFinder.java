package com.glamrock.example.concurrent.shop.v2;

import com.glamrock.example.concurrent.shop.v2.ExchangeService.Money;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BestPriceFinder {
    private final List<Shop> shops = Arrays.asList(
            new Shop("BestPrice"),
            new Shop("LetsSaveBig"),
            new Shop("MyFavoriteShop"),
            new Shop("BuyItAll"),
            new Shop("ShopEasy"));

    private final Executor executor = Executors.newFixedThreadPool(shops.size(), (Runnable r) -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    });

    public List<String> findPricesSequential(String product) {
        return shops.stream()
                .map(shop -> shop.getPriceStr(product))
                .map(Quote::parse)
                .map(Discount::applyDiscount)
                .collect(Collectors.toList());
    }

    public List<String> findPricesParallel(String product) {
        List<CompletableFuture<String>> priceFutures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPriceStr(product), executor))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote), executor)))
                .collect(Collectors.toList());

        return priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    public List<String> findPricesInUSDJava7(String product) {
        ExecutorService executor = Executors.newCachedThreadPool();
        List<Future<Double>> priceFutures = new ArrayList<>();

        for( Shop shop : shops) {
            final Future<Double> futureRate = executor.submit(new Callable<Double>() {
                @Override
                public Double call() {
                    return ExchangeService.getRate(Money.EUR, Money.USD);
                }
            });

            Future<Double> futurePriceInUSD = executor.submit(new Callable<Double>() {
                @Override
                public Double call() {
                    try {
                        double priceInEUR = shop.getPrice(product);
                        return priceInEUR * futureRate.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
            });
            priceFutures.add(futurePriceInUSD);
        }

        List<String> prices = new ArrayList<>();
        for (Future<Double> priceFuture: priceFutures) {
            try {
                prices.add(" price is " + priceFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return prices;
    }

    public List<String> findPricesInUSD(String product) {
        List<CompletableFuture<Double>> priceFutures = new ArrayList<>();

        shops.stream().forEach(shop -> {
            CompletableFuture<Double> futurePriceInUSD =
                    CompletableFuture.supplyAsync(() -> shop.getPrice(product))
                    .thenCombine(
                            CompletableFuture.supplyAsync(() -> ExchangeService.getRate(Money.EUR, Money.USD))
                                    .completeOnTimeout(ExchangeService.DEFAULT_RATE, 1, TimeUnit.SECONDS),
                            (price, rate) -> price * rate
                    ).orTimeout(3, TimeUnit.SECONDS);

            priceFutures.add(futurePriceInUSD);
        });

        List<String> prices = priceFutures.stream()
                .map(CompletableFuture::join)
                .map(price -> " price is " + price)
                .collect(Collectors.toList());

        return prices;
    }

    public List<String> findPricesInUSD2(String product) {
        List<CompletableFuture<String>> priceFutures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPrice(product))
                        .thenCombine(
                             CompletableFuture.supplyAsync(() -> ExchangeService.getRate(Money.EUR, Money.USD)),
                                (price, rate) -> price * rate
                        )
                        .thenApply(price -> shop.getName() + " price is " + price)
                ).collect(Collectors.toList());

        List<String> prices = priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        return prices;
    }

    public Stream<CompletableFuture<String>> findPricesStream(String product) {
        return shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPriceStr(product), executor))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote), executor)));
    }

    public void printPricesStream(String product) {
        long start = System.nanoTime();
        CompletableFuture[] futures = findPricesStream(product)
                .map(future -> future.thenAccept(s -> System.out.println(s + " (done in " + ((System.nanoTime() - start) / 1_000_000) + " msecs)")))
                .toArray(size -> new CompletableFuture[size]);

        CompletableFuture.allOf(futures).join();
        //CompletableFuture.anyOf(futures).join();
        System.out.println("All shops have now responded in " + ((System.nanoTime() - start) / 1_000_000) + " msecs");
    }
}
