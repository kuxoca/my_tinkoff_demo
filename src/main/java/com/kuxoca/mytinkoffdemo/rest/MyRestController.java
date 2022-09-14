package com.kuxoca.mytinkoffdemo.rest;

import com.kuxoca.mytinkoffdemo.entity.CodeEnum;
import com.kuxoca.mytinkoffdemo.entity.DBEntity;
import com.kuxoca.mytinkoffdemo.service.FinderService;
import com.kuxoca.mytinkoffdemo.utils.Utils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1")
public class MyRestController {

    @Autowired
    FinderService finderService;
    @Autowired
    Utils utils;

    @RequestMapping(value = "/currentrate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DBEntity> getCurrentRate() {
        DBEntity entity = finderService.getCurrentRate(CodeEnum.USD, "DebitCardsTransfers");
        if (entity == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @RequestMapping(value = "/{stringDate:^\\d{4}-\\d{2}-\\d{2}$}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DBEntity>> getDBEntityByDate(@PathVariable String stringDate) {
        if (stringDate.isEmpty()) {
            log.info("Date isEmpty");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!utils.matchesDate(stringDate)) {
            log.info("Date no format yyyy-MM-dd");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            LocalDateTime localDateTime = utils.toLocalDateTime(stringDate);
            List<DBEntity> entityByDate = finderService.getEntityByDate(localDateTime);
            if (entityByDate.isEmpty()) {
                log.info("query returned an empty list: "+stringDate);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(entityByDate, HttpStatus.OK);
        } catch (DateTimeParseException e) {
            log.error(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{stringDateRange:^\\d{4}-\\d{2}-\\d{2}:\\d{4}-\\d{2}-\\d{2}$}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DBEntity>> getDBEntityByDateRange(@PathVariable String stringDateRange) {
        try {
            String[] d = stringDateRange.split(":");
            LocalDateTime dateStart = utils.toLocalDateTime(d[0]);
            LocalDateTime dateStop = utils.toLocalDateTime(d[1]);
            List<DBEntity> entityRange = finderService.findRange(dateStart, dateStop);
            if (entityRange.isEmpty()) {
                log.info("Query returned an empty list: " + stringDateRange );
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(entityRange, HttpStatus.OK);
        } catch (DateTimeParseException e) {
            log.error(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}




























