package com.kuxoca.mytinkoffdemo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "db_entity")
public class DBEntity extends AbstractEntity {
    private Long milliseconds;
    private String category;
    private Double buy;
    private Double sell;
    private Integer fromCode;
    private Integer toCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DBEntity dbEntity = (DBEntity) o;

        if (category != null ? !category.equals(dbEntity.category) : dbEntity.category != null) return false;
        if (buy != null ? !buy.equals(dbEntity.buy) : dbEntity.buy != null) return false;
        if (sell != null ? !sell.equals(dbEntity.sell) : dbEntity.sell != null) return false;
        if (fromCode != null ? !fromCode.equals(dbEntity.fromCode) : dbEntity.fromCode != null) return false;
        return toCode != null ? toCode.equals(dbEntity.toCode) : dbEntity.toCode == null;
    }

    @Override
    public int hashCode() {
        int result = category != null ? category.hashCode() : 0;
        result = 31 * result + (buy != null ? buy.hashCode() : 0);
        result = 31 * result + (sell != null ? sell.hashCode() : 0);
        result = 31 * result + (fromCode != null ? fromCode.hashCode() : 0);
        result = 31 * result + (toCode != null ? toCode.hashCode() : 0);
        return result;
    }


}
