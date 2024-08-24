package com.lubase.core.service.exportmanage.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.lubase.core.service.exportmanage.ExportService;
import com.lubase.model.DbEntity;
import com.lubase.model.DbField;
import com.lubase.model.EAccessGrade;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.TypeConverterUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ExportServiceImpl implements ExportService {
    @Autowired
    DataAccess dataAccess;

    @Override
    public void ExportByQuery(DbCollection collection, HttpServletResponse response, String name) throws IOException {
        if (StringUtils.isEmpty(name)) {
            name = collection.getTableInfo().getName();
        }
        //excel文件名
        String fileName = name + "-" + LocalDateTime.now().toString().substring(0, 10);
        // 这里URLEncoder.encode可以防止中文乱码
        fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        //sheetName
        String sheetName = name + "-" + LocalDateTime.now().toString().substring(0, 10);

        //表头
        List<List<String>> headList = new ArrayList<List<String>>();
        List<String> columnList = new ArrayList<>();
        for (DbField filed : collection.tableInfo.getFieldList()) {
            //不可见字段不导出
            if (filed.getAccessRight() == EAccessGrade.Invisible.getIndex()) {
                continue;
            }
            columnList.add(filed.getCode());

            List<String> tmp = new ArrayList<>(1);
            tmp.add(filed.getName());
            headList.add(tmp);
        }
        // 获取数据
        List<List<String>> contentList = new ArrayList<List<String>>();
        List<DbEntity> dataList = collection.getData();
        for (DbEntity dbEntity : dataList) {
            List<String> tmp = new ArrayList<>();
            for (String code : columnList) {
                if (dbEntity.get(code) != null && !(dbEntity.get(code).equals(""))) {
                    // 获取关联字段的显示名称
                    String displayName = TypeConverterUtils.object2String(dbEntity.getRefData().get(code + "NAME"));
                    if (displayName != null) {
                        tmp.add(displayName);
                    } else {
                        tmp.add(TypeConverterUtils.object2String(dbEntity.get(code)));
                    }
                } else {
                    tmp.add("");
                }
            }
            contentList.add(tmp);
        }

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
            //响应首部 Access-Control-Expose-Headers 就是控制“暴露”的开关，它列出了哪些首部可以作为响应的一部分暴露给外部。
            //此处设置了开放Content-Disposition，前端可获取该响应参数获取文件名称
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream()).autoCloseStream(Boolean.FALSE)
                    .registerWriteHandler(horizontalCellStyleStrategy).sheet(sheetName).head(headList).doWrite(contentList);
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
}
