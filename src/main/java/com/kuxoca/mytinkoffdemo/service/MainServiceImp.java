package com.kuxoca.mytinkoffdemo.service;

import com.kuxoca.mytinkoffdemo.entity.DBEntity;
import com.kuxoca.mytinkoffdemo.entity.Key;
import com.kuxoca.mytinkoffdemo.entity.tinkoffApi.Payload;
import com.kuxoca.mytinkoffdemo.entity.tinkoffApi.Rates;
import com.kuxoca.mytinkoffdemo.repo.DBEntityRepo;
import com.kuxoca.mytinkoffdemo.repo.RootEntityRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class MainServiceImp implements MainService {
    private final Logger logger = LogManager.getLogger(MainServiceImp.class);
    private final RootEntityRepo rootEntityRepo;
    private final DBEntityRepo dbEntityRepo;
    private final Map<Key, DBEntity> cashMap = new HashMap<>();
    @Value("${BASE_URL}")
    private String baseUrl;
    @Value("${CURRENCY}")
    private String currency;
    @Value("${CATEGORY}")
    private String category;
    private final List<URL> urls = new ArrayList<>();
    private final List<String> categorys = new ArrayList<>();

    private MainServiceImp(RootEntityRepo rootEntityRepo, DBEntityRepo dbEntityRepo) {
        this.rootEntityRepo = rootEntityRepo;
        this.dbEntityRepo = dbEntityRepo;
    }

    @PostConstruct
    private void init() {
        urls.addAll(getUrls(currency));
        categorys.addAll(getAllCategorys(category));
        initDataFromDB();
    }

    //**
    // делет работу каждые 10 секунд
    // загружает данные из API, изменения сохраняет в БД
    // */
    @Scheduled(fixedDelay = Long.MAX_VALUE, initialDelay = 1L)
    private void w() {
        while (true) {
            urls.forEach(el -> {
                mainMedhod(el);
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private List<URL> getUrls(String str) {
        List<URL> urls = new ArrayList<>();
        List<String> pair = Arrays.stream(str.split(","))
                .map(String::strip)
                .distinct()
                .collect(Collectors.toList());
        try {
            urls.add(new URL(baseUrl));
        } catch (MalformedURLException e) {
            logger.error("ERROR to make URL: " + baseUrl, e);
        }
        for (String el : pair) {
            String[] split = el.split(":");
            String s = baseUrl + "?from=" + split[0].strip() + "&to=" + split[1].strip();
            try {
                urls.add(new URL(s));
            } catch (MalformedURLException e) {
                logger.error("ERROR to make URL: " + s, e);
            }
        }
        return urls;
    }

    private List<String> getAllCategorys(String category) {
        List<String> categorys =
                new ArrayList<>(Arrays.stream(category.split(","))
                .map(String::strip)
                .distinct()
                .collect(Collectors.toList()));
        if (!categorys.contains("DebitCardsOperations")) {
            categorys.add("DebitCardsOperations");
        }
        return categorys;
    }

    //**
    // загружает данные из API, изменения сохраняет в БД
    // */
    private void mainMedhod(URL url) {
        logger.info("Start get data from API... " + url.toString());

        try {
            List<DBEntity> entresFromAPI = loadDBEntitisFromPayLoad(rootEntityRepo.getPayload(url));

            List<DBEntity> entresFromAPIFilter = new ArrayList<>();
            for (String catEl : categorys) {
                entresFromAPIFilter.addAll(entresFromAPI.stream().filter(el -> el.getCategory().equals(catEl)).collect(Collectors.toList()));
            }

            List<DBEntity> change = getChangeMap(entresFromAPIFilter);
            if (!change.isEmpty()) {
                logger.info("Changes: " + change.size());
                addAllToCashMap(change);
                saveEntityToDB(change);
            } else {
                logger.info("No changes...");
            }
        } catch (NullPointerException eNull) {
            logger.warn("Data not load from API", eNull);
        }
    }

    private void addAllToCashMap(List<DBEntity> change) {
        change.forEach(el -> cashMap.put(new Key(el.getCategory(), el.getFromCode(), el.getToCode()), el));
    }

    private List<DBEntity> getChangeMap(List<DBEntity> fromAPI) {
        List<DBEntity> local = new ArrayList<>(fromAPI);
        for (DBEntity elCash : cashMap.values()) {
            if (local.contains(elCash)) {
                local.remove(elCash);
            }
        }
        if (local.isEmpty()) {
            return Collections.emptyList();
        }
        return local;
    }

    private void saveEntityToDB(List<DBEntity> entityList) {
        logger.info("start save " + entityList.size() + " entities");
        entityList.forEach(logger::debug);
        try {
            dbEntityRepo.saveAll(entityList);
        } catch (Exception e) {
            logger.error("ERROR save to DB ", e);
            logger.error("ERROR save to DB " + entityList);
        }
        logger.info("save complite");
    }

    private List<DBEntity> loadDBEntitisFromPayLoad(Payload payload) {
        List<DBEntity> entities = new ArrayList<>();
        Long lastUpdate = payload.getLastUpdate().getMilliseconds();
        ArrayList<Rates> rates = payload.getRates();

        for (Rates el : rates) {
            DBEntity rate = new DBEntity();
            rate.setMilliseconds(lastUpdate);
            rate.setCategory(el.getCategory());
            rate.setBuy(el.getBuy());
            rate.setSell(el.getSell());
            rate.setFromCode(el.getFromCurrency().getCode());
            rate.setToCode(el.getToCurrency().getCode());
            entities.add(rate);
        }
        return entities;
    }

    List<Key> getAllKeys(List<String> categorys) {
        List<Key> keys = new ArrayList<>();
        for (String catEl : categorys) {
            keys.addAll(dbEntityRepo.findAllKeyByCategory(catEl));
        }
        return keys;
    }

    private void initDataFromDB() {
        logger.info("INIT FROM DB starts...");
        List<Key> keys = getAllKeys(categorys);
        AtomicInteger c = new AtomicInteger(1);
        keys.forEach(key -> {
            logger.info("" + c.getAndIncrement() + " init data...  " + key.getCategory() + " " + key.getFromCode() + " " + key.getToCode());

            addAllToCashMap(Collections.singletonList(dbEntityRepo.findMaxByCategoryAndFromCodeAndToCode(key.getCategory(), key.getFromCode(), key.getToCode())));
        });
        logger.info("Init cash size " + cashMap.size());

    }

    @Override
    public Map<Key, DBEntity> getCashMap() {
        return cashMap;
    }
}
