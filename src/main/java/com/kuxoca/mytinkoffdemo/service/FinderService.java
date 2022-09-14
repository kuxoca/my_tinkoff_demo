package com.kuxoca.mytinkoffdemo.service;

import com.kuxoca.mytinkoffdemo.entity.CodeEnum;
import com.kuxoca.mytinkoffdemo.entity.DBEntity;
import com.kuxoca.mytinkoffdemo.repo.DBEntityRepo;
import com.kuxoca.mytinkoffdemo.utils.Utils;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class FinderService {
    private final DBEntityRepo dbEntityRepo;
    private final Utils utils;

    public FinderService(DBEntityRepo dbEntityRepo, Utils utils) {
        this.dbEntityRepo = dbEntityRepo;
        this.utils = utils;
    }

    public List<DBEntity> getEntityByDate(LocalDateTime date) {
        Long start = utils.getMillis(date);
        Long end = utils.getMillis(date.plusDays(1));
        String category = "DebitCardsTransfers";
        int fromCode = 840;
        int toCode = 643;

        return dbEntityRepo.findRange(category, fromCode, toCode, start, end).stream()
                .sorted((e1, e2) -> (int) (e1.getMilliseconds() - e2.getMilliseconds()))
                .collect(Collectors.toList());
    }

    public DBEntity getCurrentRate(CodeEnum code, String category) {

        int fromCode = code.getValue();
        int toCode = CodeEnum.RUR.getValue();
        return dbEntityRepo.findMaxByCategoryAndFromCodeAndToCode(category, fromCode, toCode);
    }

    public List<DBEntity> findRange(LocalDateTime dateStart, LocalDateTime dateEnd) {
        String category = "DebitCardsTransfers";
        Long start = utils.getMillis(dateStart);
        Long end = utils.getMillis(dateEnd.plusDays(0));
        int fromCode = 840;
        int toCode = 643;
        return dbEntityRepo.findRange(category, fromCode, toCode, start, end);
    }

}































