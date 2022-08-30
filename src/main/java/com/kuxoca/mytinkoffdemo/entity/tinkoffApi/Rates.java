package com.kuxoca.mytinkoffdemo.entity.tinkoffApi;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Rates {
    private String category;
    private Double buy;
    private Double sell;
    private FromCurrency fromCurrency;
    private ToCurrency toCurrency;
}