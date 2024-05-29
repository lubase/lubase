package com.lubase.core.service.exportmanage.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.lubase.orm.QueryOption;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.TableFilterWrapper;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.model.DbEntity;
import com.lubase.model.DbField;
import com.lubase.core.service.exportmanage.ExportService;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExportServiceImpl implements ExportService {
    @Autowired
    DataAccess dataAccess;

    @Override
    public void ExportByQuery(DbCollection collection, HttpServletResponse response, String name) throws IOException {
        //表头
        List<List<String>> headList = new ArrayList<List<String>>();
        List<String> codes = new ArrayList<>();
        HashMap<String, String> codeType = new HashMap<String, String>();
        HashMap<String, DbCollection> codeList = new HashMap<String, DbCollection>();
        for (DbField filed : collection.tableInfo.getFieldList()) {
            //跳过主键
            if (filed.getName().equals("主键")) {
                continue;
            } else if (filed.getCodeTypeId() != null && !StringUtils.isEmpty(filed.getCodeTypeId())) {
                //获取码表信息
                DbCollection t = getCodeType(filed.getCodeTypeId());
                codeList.put(filed.getCode(), t);
            }
            List<String> tmp = new ArrayList(1);
            tmp.add(filed.getName());
            codes.add(filed.getCode());
            headList.add(tmp);
        }
        List<List<String>> contentList = new ArrayList<List<String>>();

        List<DbEntity> dataList = collection.getData();
        for (DbEntity DbEntity : dataList) {
            List<String> tmp = new ArrayList();
            for (String code : codes) {
                if (DbEntity.get(code) != null && !(DbEntity.get(code).equals(""))) {
                    //获取码表信息
                    DbCollection t = codeList.get(code);
                    int a = 0;
                    if (t != null) {
                        for (DbEntity dmCode : t.getData()) {
                            if (dmCode.get("code_value").toString().equals(DbEntity.get(code))) {
                                tmp.add(dmCode.get("code_name").toString());
                                a++;
                            }
                        }
                    }
                    //如果码值没有
                    if (0 == a) {
                        String rowValue = TypeConverterUtils.object2String(DbEntity.getRefData().get(code + "NAME"));
                        if (rowValue == null) {
                            tmp.add(DbEntity.get(code).toString());
                        } else {
                            tmp.add(rowValue);
                        }
                    }
                } else {
                    tmp.add("");
                }
            }
            contentList.add(tmp);
        }
        //excel文件名
        final String FILENAME = name + "-" + LocalDateTime.now().toString().substring(0, 10);
        //sheetName
        final String SHEETNAME = name + "-" + LocalDateTime.now().toString().substring(0, 10);
        try {
            //表头样式策略
            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            //设置头居中
            headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            //内容策略
            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            //设置 水平居中
            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
            //初始化表格样式
            HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码
            String fileName = URLEncoder.encode(FILENAME, "UTF-8").replaceAll("\\+", "%20");
            //响应首部 Access-Control-Expose-Headers 就是控制“暴露”的开关，它列出了哪些首部可以作为响应的一部分暴露给外部。
            //此处设置了开放Content-Disposition，前端可获取该响应参数获取文件名称
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream()).autoCloseStream(Boolean.FALSE)
                    .registerWriteHandler(horizontalCellStyleStrategy).sheet(SHEETNAME).head(headList).doWrite(contentList);
        } catch (IOException e) { //下载失败情况的处理
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = MapUtils.newHashMap();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }
    }

    private DbCollection getCodeType(String code_type_id) {
        QueryOption queryOption = new QueryOption("dm_code", 0, 0);
        TableFilterWrapper wrapper = new TableFilterWrapper(true);
        wrapper.eq("code_type_id", code_type_id);
        queryOption.setTableFilter(wrapper.build());
        queryOption.setFixField("*");
        DbCollection codeCollection = dataAccess.query(queryOption);
        return codeCollection;
    }
}
