package com.lcp.qibao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lcp.core.service.DataAccess;
import com.lcp.core.model.LookupMode;
import com.lcp.core.QueryOption;
import com.lcp.core.util.ServerMacroService;
import com.lcp.core.model.DbCollection;
import com.lcp.core.TableFilter;
import com.lcp.coremodel.DbEntity;
import com.lcp.coremodel.DbField;
import com.lcp.coremodel.EDBEntityState;
import com.lcp.qibao.model.CustomFormVO;
import com.lcp.qibao.model.FormButtonVO;
import com.lcp.qibao.model.SaveFormParamDTO;
import com.lcp.qibao.model.customForm.ColumnLookupInfoVO;
import com.lcp.qibao.model.customForm.ColumnLookupParamModel;
import com.lcp.qibao.util.ClientMacro;
import com.lcp.qibao.service.RenderFormService;
import com.lcp.qibao.service.FormRuleService;
import com.lcp.qibao.service.PageDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RenderFormServiceTest {

    @Autowired
    RenderFormService renderFormService;
    @Autowired
    ServerMacroService serverMacroService;
    @Autowired
    PageDataService pageDataService;

    @Autowired
    FormRuleService formRuleService;

    @Test
    void testGetFormButton() {
        String formId = "123";
        List<FormButtonVO> list = formRuleService.getFormButtonListById(formId);
        System.out.println(JSON.toJSONString(list));
        assert list.size() == 1;
    }

//    @Test
//    void testGetAutoForm() {
//        ClientMacro clientMacro = ClientMacro.init("{\"treeId\":\"123123\"}");
//        CustomFormVO customFormVO = pageDataService.getAddDataByFuncCode("010101_btnAddBtn", clientMacro);
//        System.out.println(JSON.toJSONString(customFormVO.getData()));
//    }
//
//    @Test
//    void testEditAutoForm() {
//        CustomFormVO customFormVO = pageDataService.getEditDataByFuncCode("010109_btnAdd", "000018BIZKR230000A0G", null);
//        System.out.println(JSON.toJSONString(customFormVO));
//        assert customFormVO.getData().getId().equals("000018BIZKR230000A0G");
//    }
//
//    @Test
//    void testEditAutoFormTestDate() {
//        CustomFormVO customFormVO = pageDataService.getEditDataByFuncCode("010106_btnEdit", "000000399FK8G0000A01", null);
//        System.out.println(JSON.toJSONString(customFormVO));
//        assert customFormVO.getData().getId().equals("000000399FK8G0000A01");
//    }

//    @Test
//    void testFormTrigger() {
//        ClientMacro clientMacro = ClientMacro.init("");
//        CustomFormVO customFormVO = pageDataService.getAddDataByFuncCode("010209_btnBatchAdd", clientMacro);
//        System.out.println(JSON.toJSONString(customFormVO));
//    }

    @Test
    void testAddSaveAutoForm() {
        DbEntity newEntity = new DbEntity();
        newEntity.setState(EDBEntityState.Added);
        newEntity.put("TYPEID", "000018BIZHZ8N0000A0E");
        newEntity.put("CODE", "005");
        newEntity.put("NAME", "ADD2");
        int cc = pageDataService.saveFormDataByFuncCode("010109_btnAdd", newEntity);
        assert cc == 1;
    }


    @Test
    void testAddDataFromBrowser(@Autowired TestRestTemplate restTemplate) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(type);

        SaveFormParamDTO saveFormParamModel = new SaveFormParamDTO();
        saveFormParamModel.setFuncCode("010209_btnAdd");
        DbEntity newEntity = new DbEntity();
        newEntity.setState(EDBEntityState.Added);
        newEntity.put("TYPEID", "000018BIZHZ8N0000A0E");
        newEntity.put("CODE", "005");
        newEntity.put("NAME", "ADD2");
        saveFormParamModel.setData(newEntity);
        HttpEntity<String> requestEntity = new HttpEntity<String>(JSON.toJSONString(saveFormParamModel), headers);

        String resultStr = restTemplate.postForObject("/form/saveFormData", requestEntity, String.class);
        JSONObject obj = JSON.parseObject(resultStr, JSONObject.class);
        assert obj.containsKey("success");
        assert obj.get("success").toString().equals("1");

        assert obj.containsKey("data");
        assert obj.get("data").toString().equals("1");
    }

    @Test
    void testEditSaveAutoForm() {
        DbEntity newEntity = new DbEntity();
        newEntity.put("ID", "000009FFRV3M80000A01");
        newEntity.setState(EDBEntityState.Modified);
        newEntity.put("CODE", "004-2");
        newEntity.put("NAME", "ADD2-2");
        int cc = pageDataService.saveFormDataByFuncCode("010109_btnAdd", newEntity);
        assert cc == 1;
    }

//    @Test
//    void testGetCustomForm() {
//        CustomFormVO customFormVO = pageDataService.getAddDataByFuncCode("010109_btnBatchAdd", null);
//        System.out.println(JSON.toJSONString(customFormVO));
//    }
//
//    @Test
//    void testEditCustomForm() {
//        CustomFormVO customFormVO = pageDataService.getEditDataByFuncCode("010209_btnBatchAdd", "000005I6JHX630001A96", null);
//        System.out.println(JSON.toJSONString(customFormVO));
//        assert customFormVO.getData().getId().equals("000005I6JHX630001A96");
//    }

    @Test
    void testGetLookupInfo() {
        ColumnLookupParamModel columnLookupParamModel = new ColumnLookupParamModel();
        //SUUSER	ORGID
        columnLookupParamModel.setColumnId("000005GKWXUM10000143");
        columnLookupParamModel.setFormId("xxxx");
        columnLookupParamModel.setFuncCode("xxxx");
        columnLookupParamModel.setQueryParam("{\"PageIndex\":1,\"PageSize\":3}");
        ColumnLookupInfoVO columnLookupInfoVO = renderFormService.getColLookupData(columnLookupParamModel);

        System.out.println(columnLookupInfoVO);
        assert columnLookupInfoVO.getDbCollection().getTotalCount() == 8;
        assert columnLookupInfoVO.getDbCollection().getData().size() == 3;
        assert columnLookupInfoVO.getSearchCols().equals("CODE,NAME,PYDM");
    }

//    @Test
//    void testFieldRelyFormTest() {
//        ClientMacro clientMacro = new ClientMacro();
//        CustomFormVO customFormVO = pageDataService.getAddDataByFuncCode("683138963380113408", clientMacro);
//        System.out.println(JSON.toJSONString(customFormVO));
//        assert customFormVO.getId().equals("683068312124395520");
//    }

    @Autowired
    DataAccess dataAccess;

    @Test
    void testQueryParentTableField() {
        QueryOption queryOption = new QueryOption("ss_page");
        queryOption.setFixField("app_id.display_name,page_name,type,description");
        queryOption.setTableFilter(new TableFilter("id", "670733622264729600"));
        Map<String, LookupMode> refMap = new HashMap<>();
        refMap.put("app_id", new LookupMode("id", "", "ss_app"));
        queryOption.setRefFields(refMap);
        DbCollection collection = dataAccess.query(queryOption);
        assert collection.getData().size() == 1;
        DbEntity entity = collection.getData().get(0);
        for (DbField f : collection.getTableInfo().getFieldList()) {
            System.out.println(String.format("table:%s code: %s", f.getTableCode(), f.getCode()));
        }
        System.out.println(entity);
    }

    @Test
    void testQueryParentTableField2() {
        ClientMacro clientMacro = new ClientMacro();
        CustomFormVO form = ReflectionTestUtils.invokeMethod(renderFormService, "getCustomFormByFormId", "688228144351547392", "670733622264729600", clientMacro);
        assert form != null;
        System.out.println(form.getName());

    }

    @Test
    void testColDefaultValue() {
        ClientMacro clientMacro = new ClientMacro();
        clientMacro.put(ClientMacro.clientMacroPre + "treeId", "t1");
        clientMacro.put(ClientMacro.clientMacroPre + "treeName", "t2");
        String colDefault = "@@C.treeId";
        String[] values = ReflectionTestUtils.invokeMethod(renderFormService, "getColumnDefaultValue", colDefault, clientMacro);
        assert values.length == 2;
        assert values[0].equals("t1");
        assert values[1].equals("");

        colDefault = "@@C.treeId,@@C.treeName";
        values = ReflectionTestUtils.invokeMethod(renderFormService, "getColumnDefaultValue", colDefault, clientMacro);
        assert values.length == 2;
        assert values[0].equals("t1");
        assert values[1].equals("t2");

        colDefault = "@@S.datetime";
        values = ReflectionTestUtils.invokeMethod(renderFormService, "getColumnDefaultValue", colDefault, clientMacro);
        assert values.length == 2;
        System.out.println(JSON.toJSONString(values));

        colDefault = "@@S.datetime2,@@S.date";
        values = ReflectionTestUtils.invokeMethod(renderFormService, "getColumnDefaultValue", colDefault, clientMacro);
        assert values.length == 2;
        System.out.println(JSON.toJSONString(values));

        colDefault = "@@S.datetime,@@S.date";
        values = ReflectionTestUtils.invokeMethod(renderFormService, "getColumnDefaultValue", colDefault, clientMacro);
        assert values.length == 2;
        System.out.println(JSON.toJSONString(values));

        colDefault = "a";
        values = ReflectionTestUtils.invokeMethod(renderFormService, "getColumnDefaultValue", colDefault, clientMacro);
        assert values.length == 2;
        System.out.println(JSON.toJSONString(values));

        colDefault = "a,b";
        values = ReflectionTestUtils.invokeMethod(renderFormService, "getColumnDefaultValue", colDefault, clientMacro);
        assert values.length == 2;
        System.out.println(JSON.toJSONString(values));
    }

    @Test
    void testUniqueValue() {
        String columnId = "2022052821002914881";// sa_account user_code
        assert !renderFormService.checkFieldUniqueValue(columnId, "admin", "123");
        assert renderFormService.checkFieldUniqueValue(columnId, "admin", "688163728524316672");
        assert renderFormService.checkFieldUniqueValue(columnId, "admin12312312", "123");
    }


    @Test
    void testGetLookupColumn() {
        String str = "{\"clientMacro\":\"{\\\"treeKey\\\":\\\"010101\\\",\\\"treeName\\\":\\\"应用管理\\\",\\\"pageId\\\":\\\"2022052921021020059\\\",\\\"appId\\\":\\\"671085014334574592\\\"}\",\"funcCode\":\"2022052900012589019\",\"formId\":\"2022052900201351016\",\"columnId\":\"2022052821058100818\",\"queryParam\":\"{}\",\"formData\":\"{}\"}";
        ColumnLookupParamModel columnLookupParamModel = JSON.parseObject(str, ColumnLookupParamModel.class);
        assert columnLookupParamModel != null;
        ColumnLookupInfoVO infoVO = renderFormService.getColLookupData(columnLookupParamModel);
        assert infoVO != null;
        System.out.println(infoVO);
        assert infoVO.getDisplayCol().equals("form_name");
        assert infoVO.getTableKey().equals("id");
    }
}
