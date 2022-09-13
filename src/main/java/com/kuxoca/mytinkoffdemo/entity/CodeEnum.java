package com.kuxoca.mytinkoffdemo.entity;

public enum CodeEnum {
    EUR(978),
    GBP(826),
    USD(840),
    RUR(643),
    KZT(398),
    BYN(933),
    UZS(860),
    CNY(156);
    private final Integer value;

    CodeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
