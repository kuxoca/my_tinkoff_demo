package com.kuxoca.mytinkoffdemo.service;


import com.kuxoca.mytinkoffdemo.entity.DBEntity;
import com.kuxoca.mytinkoffdemo.entity.Key;

import java.util.Map;

public interface MainService {
    Map<Key, DBEntity> getCashMap();
}
