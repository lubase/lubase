package com.lubase.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lubase.core.model.customForm.ChildTableSetting;
import com.lubase.core.model.customForm.FormGroup;
import com.lubase.core.model.customForm.FormTab;
import com.lubase.core.model.customForm.Tabs;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class CustomFormTest {

    @Test
    void testGetFieldFromLayout() {
        Tabs tabs2 = new Tabs();
        FormTab t = new FormTab();
        t.setTitle("title");
        FormGroup group = new FormGroup();
        group.setTitle("group");
        List<FormGroup> listGroup = new ArrayList<>();
        listGroup.add(group);
        t.setGroups(listGroup);
        List<FormTab> listTab = new ArrayList<>();
        listTab.add(t);
        tabs2.setTabs(listTab);
        System.out.println(JSON.toJSONString(tabs2));

        String layoutStr = "{\"tabs\":[{\"title\":\"基本信息\",\"groups\":[{\"title\":\"注册信息\",\"type\":\"form\",\"fieldList\":[{\"Id\":\"000005GKWXUM10000135\",\"OrderId\":30,\"FieldAccess\":4,\"IsNull\":1}]},{\"title\":\"角色信息\",\"type\":\"form\",\"fieldList\":[]},{\"title\":\"其他\",\"type\":\"form\",\"fieldList\":[{\"Id\":\"000005GKWXUM1000013D\",\"OrderId\":10,\"FieldAccess\":2,\"IsNull\":1},{\"Id\":\"000005GKWXUM1000013C\",\"OrderId\":20,\"FieldAccess\":4,\"IsNull\":1}]}],\"name\":\"tab_0\"},{\"title\":\"联系信息\",\"groups\":[{\"title\":\"联系信息\",\"type\":\"form\",\"fieldList\":[]},{\"title\":\"地址信息\",\"type\":\"form\",\"fieldList\":[]}],\"name\":\"tab_1\"},{\"title\":\"银行信息\",\"groups\":[{\"title\":\"银行信息\",\"type\":\"form\",\"fieldList\":[]}],\"name\":\"tab_2\"},{\"title\":\"tab页\",\"groups\":[{\"title\":\"分组名称\",\"type\":\"form\",\"fieldList\":[]}],\"name\":\"tab_3\"}]}";
        Object obj = JSONObject.parseObject(layoutStr, Tabs.class);
        assert obj instanceof Tabs;
        Tabs tabs = (Tabs) obj;
        for (FormTab tab : tabs.getTabs()) {
            System.out.println(tab.getTitle());
        }
    }

    @Test
    void testChildTableData() {
        String str = "[{\"serialNum\":\"NNFYEKJPZ6zRe3E7m0whgoDDSzJIpbeC\",\"queryOption\":{\"TableType\":\"2022052817067516926\",\"TableName\":\"ss_button\",\"TableTypeNAME\":\"系统配置\",\"TableNameNAME\":\"按钮设置表\",\"QueryMode\":\"2\",\"TableFilter\":{\"FilterDisplay\":\"过滤条件设置\",\"And\":true,\"Not\":false,\"FilterName\":null,\"FilterValue\":null,\"FilterValueNAME\":null,\"OperateMode\":3,\"ChildFilters\":[]},\"FixField\":\"\",\"FixFieldNAME\":\"\",\"Sort\":\"\",\"SortNAME\":\"\"}}]";
        List<ChildTableSetting> childTables = JSONObject.parseArray(str, ChildTableSetting.class);
        assert childTables.size() == 1;
        ChildTableSetting childTable = childTables.get(0);
        assert childTable.getSerialNum().equals("NNFYEKJPZ6zRe3E7m0whgoDDSzJIpbeC");
        assert childTable.getQueryOption() != null;
        assert childTable.getQueryOption().getTableName().equals("ss_button");
    }
}
