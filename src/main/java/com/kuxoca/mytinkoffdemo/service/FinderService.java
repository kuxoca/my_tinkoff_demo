package com.kuxoca.mytinkoffdemo.service;

import com.kuxoca.mytinkoffdemo.entity.DBEntity;
import com.kuxoca.mytinkoffdemo.repo.DBEntityRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class FinderService {
    private final DBEntityRepo dbEntityRepo;

    public FinderService(DBEntityRepo dbEntityRepo) {
        this.dbEntityRepo = dbEntityRepo;
    }

    public List<DBEntity> getEntityByDate(LocalDateTime date) {
//        date = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0);
        Long start = getMillis(date);
        Long end = getMillis(date.plusDays(1));
        String category = "DebitCardsTransfers";
        int fromCode = 840;
        int toCode = 643;

        return dbEntityRepo.findRange(category, fromCode, toCode, start, end).stream()
                .sorted((e1,e2)-> (int) (e2.getMilliseconds()- e1.getMilliseconds()))
                .collect(Collectors.toList());
    }

    private Long getMillis(LocalDateTime date) {
        return date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
