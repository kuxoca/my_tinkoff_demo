package com.kuxoca.mytinkoffdemo.entity.tinkoffApi;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RootEntity {
    private String resultCode;
    private String trackingId;
    private Payload payload;


}




