package com.bio.common.utils;

import com.bio.common.utils.excel.ExcelFormat;
import com.bio.common.utils.excel.ExcelHeaderInfo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.locale.converters.DateLocaleConverter;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author kundy
 * @create 2019/2/15 10:28 AM
 * <p>
 * excel报表导出工具类
 */

public class ExcelUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtils.class);

    public static final int ROW_ACCESS_WINDOW_SIZE = 100;
    public static final int SHEET_MAX_ROW = 100000;

    private List list;
    private List<ExcelHeaderInfo> excelHeaderInfos;
    private Map<String, ExcelFormat> formatInfo;

    private static int send=-1;
    private static int scount=1;




    public ExcelUtils(List list, List<ExcelHeaderInfo> excelHeaderInfos) {
        this.list = list;
        this.excelHeaderInfos = excelHeaderInfos;
    }

    public ExcelUtils(List list, List<ExcelHeaderInfo> excelHeaderInfos, Map<String, ExcelFormat> formatInfo) {
        this.list = list;
        this.excelHeaderInfos = excelHeaderInfos;
        this.formatInfo = formatInfo;
    }

    public Workbook getWorkbook() {
        Workbook workbook = new SXSSFWorkbook(ROW_ACCESS_WINDOW_SIZE);
        String[][] datas = transformData();
        Field[] fields = list.get(0).getClass().getDeclaredFields();
        CellStyle style = setCellStyle(workbook);
        int pageRowNum = 0;
        Sheet sheet = null;

        long startTime = System.currentTimeMillis();
        LOGGER.info("开始处理excel文件。。。");

        for (int i = 0; i < datas.length; i++) {
            // 如果是每个sheet的首行
            if (i % SHEET_MAX_ROW == 0) {
                // 创建新的sheet
                sheet = createSheet(workbook, i);
                pageRowNum = 0; // 行号重置为0
                for (int j = 0; j < getHeaderRowNum(excelHeaderInfos); j++) {
                    sheet.createRow(pageRowNum++);
                }
                createHeader(sheet, style);
            }
            // 创建内容
            Row row = sheet.createRow(pageRowNum++);
            createContent(row, style, datas, i, fields,sheet,send,scount);

        }
        LOGGER.info("处理文本耗时" + (System.currentTimeMillis() - startTime) + "ms");
        return workbook;
    }

    // 创建表头
    private void createHeader(Sheet sheet, CellStyle style) {
        for (ExcelHeaderInfo excelHeaderInfo : excelHeaderInfos) {
            Integer lastRow = excelHeaderInfo.getLastRow();
            Integer firstRow = excelHeaderInfo.getFirstRow();
            Integer lastCol = excelHeaderInfo.getLastCol();
            Integer firstCol = excelHeaderInfo.getFirstCol();

            // 行距或者列距大于0才进行单元格融合
            if ((lastRow - firstRow) != 0 || (lastCol - firstCol) != 0) {
                sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
            }
            // 获取当前表头的首行位置
            Row row = sheet.getRow(firstRow);
            // 在表头的首行与首列位置创建一个新的单元格
            Cell cell = row.createCell(firstCol);
            // 赋值单元格
            cell.setCellValue(excelHeaderInfo.getTitle());
            cell.setCellStyle(style);
            sheet.setColumnWidth(firstCol,cell.getStringCellValue().getBytes().length*2*256);
        }
    }

    // 创建正文
    private void createContent(Row row, CellStyle style, String[][] content, int i, Field[] fields,Sheet sheet,int send,int scount) {
        List<String> columnNames = getBeanProperty(fields);
        for (int j = 0; j < columnNames.size(); j++) {
            Cell cell = row.createCell(j);
            cell.setCellStyle(style);
            if(null==content[i][j] || "".equals(content[i][j].trim())){
                row.createCell(j).setCellValue("无");
                continue;
            }
            if (i!=0 && content[i][j].equals(content[i-1][j])) {
                scount++;
                send=j;
            } else {
                if(i!=0 && send==j && send!=-1){
                    CellRangeAddress range = new CellRangeAddress(i-scount+1, i, j, j);
                    sheet.addMergedRegion(range);
                    scount=1;
                    send=-1;
                }
                // 如果格式化Map为空，默认为字符串格式
                if (formatInfo == null) {
                    cell.setCellValue(content[i][j]);
                    continue;
                }
                if (formatInfo.containsKey(columnNames.get(j))) { ;
                    switch (formatInfo.get(columnNames.get(j)).getValue()) {
                        case "DOUBLE":
                            cell.setCellValue(Double.parseDouble(content[i][j]));
                            break;
                        case "INTEGER":
                            cell.setCellValue(Integer.parseInt(content[i][j]));
                            break;
                        case "PERCENT":
                            style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
                            cell.setCellValue(Double.parseDouble(content[i][j]));
                            break;
                        case "DATE":
                            style.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy-MM-dd HH:mm:ss"));
                            Cell cell1 = row.createCell(j);
                            cell1.setCellStyle(style);
                            row.createCell(j).setCellValue(this.parseDate(content[i][j]));
                    }
                } else {
                    cell.setCellValue(content[i][j]);
                }
            }
        }
    }

    // 将原始数据转成二维数组
    private String[][] transformData() {
        int dataSize = this.list.size();
        String[][] datas = new String[dataSize][];
        // 获取报表的列数
        Field[] fields = list.get(0).getClass().getDeclaredFields();
        // 获取实体类的字段名称数组
        List<String> columnNames = this.getBeanProperty(fields);
        for (int i = 0; i < dataSize; i++) {
            datas[i] = new String[fields.length];
            for (int j = 0; j < fields.length; j++) {
                try {
                    // 赋值
                    ConvertUtils.register((Converter) new DateLocaleConverter(), Date.class);
                    datas[i][j] = BeanUtils.getProperty(list.get(i), columnNames.get(j));
                } catch (Exception e) {
                    LOGGER.error("获取对象属性值失败");
                    e.printStackTrace();
                }
            }
        }
        return datas;
    }

    // 获取实体类的字段名称数组
    private List<String> getBeanProperty(Field[] fields) {
        List<String> columnNames = new ArrayList<>();
        for (Field field : fields) {
            String[] strings = field.toString().split("\\.");
            String columnName = strings[strings.length - 1];
            columnNames.add(columnName);
        }
        return columnNames;
    }

    // 新建表格
    private static Sheet createSheet(Workbook workbook, int i) {
        Integer sheetNum = i / SHEET_MAX_ROW;
        workbook.createSheet("sheet" + sheetNum);
        //动态指定当前的工作表
        return workbook.getSheetAt(sheetNum);
    }

    // 获取标题总行数
    private static Integer getHeaderRowNum(List<ExcelHeaderInfo> headerInfos) {
        Integer maxRowNum = 0;
        for (ExcelHeaderInfo excelHeaderInfo : headerInfos) {
            Integer tmpRowNum = excelHeaderInfo.getLastRow();
            if (tmpRowNum > maxRowNum) maxRowNum = tmpRowNum;
        }
        return maxRowNum + 1;
    }

    // 设置总体表格样式
    private static CellStyle setCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setWrapText(true);
        return style;
    }

    // 字符串转日期
    private Date parseDate(String strDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(strDate);
        } catch (Exception e) {
            LOGGER.error("字符串转日期错误");
            e.printStackTrace();
        }
        return date;
    }

    // 发送响应结果
    public void sendHttpResponse(HttpServletResponse response,String fileName, Workbook workbook) {
        try {
            /* String filepath="E:\\Test\\";*/
//            fileName = new String(fileName.getBytes(), "UTF-8");
            fileName = URLEncoder.encode(fileName,"UTF-8");
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setLocale(new java.util.Locale("zh","CN"));
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            OutputStream os = response.getOutputStream();
            workbook.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //写入本地文件夹
    public void SaveExcelFile(String fileName, Workbook workbook) {
        try {
            //如果是本地运行请删掉这一行或者改为utf-8编码
            fileName = new String(fileName.getBytes(), "GBK");
            FileOutputStream fileOutputStream=new FileOutputStream(fileName);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}