package com.glamrock.example.concurrent.shop.v2;

import java.util.Random;

import static com.glamrock.example.concurrent.Util.delay;
import static com.glamrock.example.concurrent.Util.format;

public class Shop {

  private final String name;
  private final Random random;

  public Shop(String name) {
    this.name = name;
    random = new Random(name.charAt(0) * name.charAt(1) * name.charAt(2));
  }

  public String getPriceStr(String product) {
    double price = calculatePrice(product);
    Discount.Code code = Discount.Code.values()[random.nextInt(Discount.Code.values().length)];
    return name + ":" + price + ":" + code;
  }

  public double getPrice(String product) {
    return calculatePrice(product);
  }

  public double calculatePrice(String product) {
    delay();
    return format(random.nextDouble() * product.charAt(0) + product.charAt(1));
  }

  public String getName() {
    return name;
  }

}
