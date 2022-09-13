package com.kuxoca.mytinkoffdemo.repo;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuxoca.mytinkoffdemo.entity.tinkoffApi.Payload;
import com.kuxoca.mytinkoffdemo.entity.tinkoffApi.RootEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URL;

@Repository
public class RootEntityRepoImp implements RootEntityRepo {
    Logger logger = LogManager.getLogger(RootEntityRepoImp.class);

    @Override
    public RootEntity getRootEntity(URL url) {
        RootEntity rootEntity = new RootEntity();
        try {
            rootEntity = new ObjectMapper().readValue(url, RootEntity.class);
        } catch (JsonParseException e) {
            logger.error("ERROR to JSON data parse. ", e);
        } catch (JsonMappingException e) {
            logger.error("ERROR to JSON data mapping. ", e);
        } catch (NullPointerException eNull) {
            logger.error("Data from API is NULL", eNull);
        } catch (IOException e) {
            logger.error("ERROR load data from API. ", e);
        }
//        if (rootEntity == null) {
//            throw new NullPointerException("Data from API is NULL");
//        }
        return rootEntity;
    }

    @Override
    public Payload getPayload(URL url) {
        return getRootEntity(url).getPayload();
    }
}
