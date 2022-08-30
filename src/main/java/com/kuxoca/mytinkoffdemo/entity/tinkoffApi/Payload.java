package com.kuxoca.mytinkoffdemo.entity.tinkoffApi;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public class Payload {
    private LastUpdate lastUpdate;
    private ArrayList<Rates> rates;
}