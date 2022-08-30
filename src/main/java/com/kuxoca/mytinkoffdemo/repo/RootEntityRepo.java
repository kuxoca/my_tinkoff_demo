package com.kuxoca.mytinkoffdemo.repo;


import com.kuxoca.mytinkoffdemo.entity.tinkoffApi.Payload;
import com.kuxoca.mytinkoffdemo.entity.tinkoffApi.RootEntity;

import java.net.URL;


public interface RootEntityRepo {
    RootEntity getRootEntity(URL url);

    //    RootEntity getRootEntityByCode(String from, String to);
    Payload getPayload(URL url);

}

