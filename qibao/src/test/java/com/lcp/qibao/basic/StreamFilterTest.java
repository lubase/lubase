package com.lcp.qibao.basic;

import com.lcp.coremodel.DbEntity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class StreamFilterTest {

    @Test
    void testFilter() {
        List<DbEntity> list = new ArrayList<>();
        DbEntity entity = new DbEntity();
        entity.setId(11L);
        list.add(entity);
        entity = new DbEntity();
        entity.setId(22L);
        list.add(entity);
        entity = new DbEntity();
        entity.setId(33L);
        list.add(entity);

        DbEntity entity1 = list.stream().filter(f -> f.getId().equals("44")).findFirst().orElse(null);
        assert entity1 == null;
        DbEntity entity2 = list.stream().filter(f -> f.getId().equals("11")).findFirst().get();
        assert entity2.getId().equals("11");
    }
}
