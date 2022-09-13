package com.kuxoca.mytinkoffdemo.rest;

import com.kuxoca.mytinkoffdemo.entity.DBEntity;
import com.kuxoca.mytinkoffdemo.service.FinderService;
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
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1")
public class MyRestController {

    @Autowired
    FinderService finderService;

    @RequestMapping(value = "/{date}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DBEntity>> getDBEntity(@PathVariable String date) {
        if (date.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            date = date + "-00:00";
            LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm"));
            return new ResponseEntity<>(finderService.getEntityByDate(dateTime), HttpStatus.OK);
        } catch (DateTimeParseException e) {
            log.error(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}




























