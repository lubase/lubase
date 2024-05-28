package com.lcp.wfengine;

import com.lubase.core.model.DbCollection;
import com.lubase.core.service.DataAccess;
import com.lubase.core.util.ServerMacroService;
import com.lubase.core.util.TypeConverterUtils;
import com.lubase.wfengine.auto.entity.WfServiceEntity;
import com.lubase.wfengine.dao.WfFInsDao;
import com.lubase.wfengine.dao.WfServiceDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class WfFInsDaoTest {

    @Autowired
    WfFInsDao fInsDao;

    @Autowired
    WfServiceDao wfServiceDao;

    @Autowired
    DataAccess dataAccess;

    @Autowired
    ServerMacroService serverMacroService;

    @Test
    void testGetFInsName() {
        String serviceId = "769343035275218944";
        WfServiceEntity serviceEntity = wfServiceDao.getWfServiceById(TypeConverterUtils.object2Long(serviceId));
        DbCollection bisData = dataAccess.queryById("bs_demo", 806085210205589504L);

        fInsDao.getFInsName(serviceEntity, bisData);
    }

    @Test
    void test() {
        String str = "{@@S.time}--{address}{@@S.datetime}";
        String pattern = "\\{@@[^{]*}";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        while (m.find()) {
            String matcherStr = m.group(0);
            String key = m.group(0).replaceAll("[{}]", "");
            System.out.println(key);
            String key2 = serverMacroService.getServerMacroByKey(key);
            str = str.replace(matcherStr, key2);
        }
        System.out.println(str);
    }
}
