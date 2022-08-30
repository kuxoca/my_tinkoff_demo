package com.kuxoca.mytinkoffdemo.repo;

import com.kuxoca.mytinkoffdemo.entity.DBEntity;
import com.kuxoca.mytinkoffdemo.entity.Key;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DBEntityRepo extends JpaRepository<DBEntity, Long> {

    @Query(value = "select distinct new com.kuxoca.mytinkoffdemo.entity.Key (u.category, u.fromCode, u.toCode) " +
            "from DBEntity u ")
//    ищет уникальные значения Key
    List<Key> findAllKey();

    @Query(value = "select distinct new com.kuxoca.mytinkoffdemo.entity.Key (u.category, u.fromCode, u.toCode) " +
            "       from DBEntity u " +
            "       where u.category = :category ")
//    ищет уникальные значения Key по категориям
    List<Key> findAllKeyByCategory(
            @Param("category") String category
    );

    @Query(value = "select distinct u.category from DBEntity u")
    List<String> findAllCategory();

    @Query(value = "select d " +
            "from DBEntity d " +
            "where (d.milliseconds = " +
            "   (select max(u.milliseconds) from DBEntity u " +
            "       where (u.category=:category and u.fromCode=:fromCode and u.toCode=:toCode))" +
            "   and d.category=:category and d.fromCode=:fromCode and d.toCode=:toCode)"
    )
//    ищет крайнее значенеи по category fromCode toCode
    List<DBEntity> findMaxByCategoryAndFromCodeAndToCode(
            @Param("category") String category,
            @Param("fromCode") Integer fromCode,
            @Param("toCode") Integer toCode
    );

    @Query(value = "select d from DBEntity d " +
            "where (d.milliseconds = (select max(u.milliseconds) from DBEntity u " +
            "   where u.category=:category) " +
            "and d.category=:category )")
//   некоректный поиск, что-то там ищет
    List<DBEntity> findLastDataByCategory(@Param("category") String category);

    @Query(value = "select u " +
            "from DBEntity u " +
            "where u.milliseconds = (select max(d.milliseconds) " +
            "                        from DBEntity d " +
            "                        where d.category = u.category " +
            "                          and d.fromCode = u.fromCode " +
            "                          and d.toCode = u.toCode)")

//   ищет все уникальности по category fromCode toCode
    List<DBEntity> fsd();

    @Query(value = "select u " +
            "from DBEntity u " +
            "where u.category = :category " +
            "  and u.fromCode = :fromCode " +
            "  and u.toCode = :toCode " +
            "  and u.milliseconds BETWEEN :start and :stop")
//  выводит список значений за период start - stop, category fromCode toCode
    List<DBEntity> findRange(
            @Param("category") String category,
            @Param("fromCode") int fromCode,
            @Param("toCode") int toCode,
            @Param("start") long start,
            @Param("stop") long stop
    );
}








































