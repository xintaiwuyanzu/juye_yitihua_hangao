package com.dr.archive.service.impl;

import com.dr.archive.entity.DataResourceMarkResult.*;
import com.dr.archive.entity.*;
import com.dr.archive.service.RealmClassService;
import com.dr.archive.service.RelationService;
import com.dr.archive.service.StructuredTextService;
import com.dr.archive.service.TripletDataService;
import com.dr.archive.util.JsonUtils;
import com.dr.framework.common.file.autoconfig.CommonFileConfig;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.service.FormDataService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.google.gson.Gson;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: yang
 * @create: 2022-06-03 15:27
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class StructuredTextServiceImpl extends DefaultBaseService<StructuredText> implements StructuredTextService {

    @Value("${struct.url}")
    String excelUrl;
    private static final String FILETYPE = ".xlsx";

    @Autowired
    CommonFileConfig commonFileConfig;
    @Autowired
    FormDataService formDataService;
    @Autowired
    RelationService relationService;
    @Autowired
    TripletDataService tripletDataService;
    @Autowired
    RealmClassService realmClassService;
    // 创建workbook对象，读取整个文档
    Workbook workbook = null;
    Sheet sheet = null;

    static final Logger logger = LoggerFactory.getLogger(StructuredTextServiceImpl.class);

    @Override
    public String upload(MultipartFile file) {
        //获取文件类型
        String fileType = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
        String uuid = System.currentTimeMillis() + "";
        String rowNum = "";
        try {
            //保存文件
            String fromPath = excelUrl + File.separator + uuid + fileType;
            new FileOutputStream(fromPath).write(file.getBytes());
            if (FILETYPE.equals(fileType)) {
                // 后缀为.xlsx则用XSSFWorkbook
                //excel表单的行数
                rowNum = String.valueOf(xssfDeal(file.getInputStream()));
            } else {
                rowNum = String.valueOf(hssfDeal(file.getInputStream()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowNum;
    }

    /**
     * xlsx文件用XSSFWorkbook处理，获取文件的数据数量
     */
    private int xssfDeal(InputStream in) {
        try {
            workbook = new XSSFWorkbook(in);
            sheet = workbook.getSheetAt(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 获取sheet的最大row下标
        return sheet.getLastRowNum();
    }

    /**
     * xls文件用HSSFWorkbook处理，获取文件的数据数量
     */
    private int hssfDeal(InputStream in) {
        try {
            workbook = new HSSFWorkbook(in);
            sheet = workbook.getSheetAt(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 获取sheet的最大row下标
        return sheet.getLastRowNum();
    }

    /**
     * 格式化单元格日期内容
     *
     * @param cell
     * @return
     */
    private static String convertCellValueDataToString(Cell cell) {
        int dformat = cell.getCellStyle().getDataFormat();
        Date dateCellValue = cell.getDateCellValue();
        SimpleDateFormat sdf = null;
        String cellValue = "";
        switch (dformat) {
            case 14:
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                cellValue = sdf.format(dateCellValue);
                break;
            case 31:
                sdf = new SimpleDateFormat("yyyy年MM月dd日");
                cellValue = sdf.format(dateCellValue);
                break;
            case 57:
                sdf = new SimpleDateFormat("yyyy年MM月");
                cellValue = sdf.format(dateCellValue);
                break;
            case 58:
                sdf = new SimpleDateFormat("mm月dd日");
                cellValue = sdf.format(dateCellValue);
                break;
            case 32:
                sdf = new SimpleDateFormat("hh时mm分");
                cellValue = sdf.format(dateCellValue);
                break;
            case 20:
                sdf = new SimpleDateFormat("HH:mm");
                cellValue = sdf.format(dateCellValue);
                break;
            default:
                cellValue = new DataFormatter().formatCellValue(cell);
                break;

        }
        return cellValue;
    }

    /**
     * 获取文本内容，统计数量，刷新数据库内容
     */
    @Override
    public void loadData() throws IOException {
        List<StructuredText> structuredTexts = selectList(SqlQuery.from(StructuredText.class));
        //根据数据库文件id从本地获取文件
        for (StructuredText structuredText : structuredTexts) {
            File file = new File(structuredText.getFilePath());
            String fileType = file.getName().substring(file.getName().lastIndexOf("."));
            FileInputStream inputStream = new FileInputStream(file);
            //获取文件类型
            if (FILETYPE.equals(fileType)) {
                //xlsx
                structuredText.setDataNum(xssfDeal(inputStream) + "");
                this.updateById(structuredText);
            } else {
                //xls
                structuredText.setDataNum(hssfDeal(inputStream) + "");
                this.updateById(structuredText);
            }
            inputStream.close();
        }
    }

    /**
     * 数据标注
     */
    @Override
    public long findExcelText(String resultStr, String id, int type) {
        //梳理出的数据数量
        long n = 0;
        try {
            Result result = JsonUtils.jsonToObject(resultStr, Result.class);
            List<Map<String, String>> list = getConditionData(result, id, type);
            //把数据放入对象库
            List<TransData> properties = result.getProperties();
            Ids ids = result.getObjIds();
            boolean b = false;
            String toName = "";
            for (TransData property : properties) {
                if (property.getTo().equals("NAME")) {
                    b = true;
                    toName = property.getFrom();
                    break;
                }
            }
            if (b) {
                for (Map<String, String> map : list) {
                    if (StringUtils.hasText(map.get(toName))) {
                        FormData formData = new FormData(ids.getSourceId());
                        String content = "";
                        for (TransData property : properties) {
                            content = map.get(property.getFrom()).trim();
                            if (StringUtils.hasText(content)) {
                                formData.put(property.getTo(), content);
                            }
                        }
                        //先查询对象库有没有该名字的数据
                        List<FormData> data = getNameFormData(result.getObjIds().getSourceId(), map.get(toName));
                        if (formData.size() > 1 && data.size() == 0) {
                            formData.setId(UUID.randomUUID().toString());
                            formDataService.addFormData(formData);
                            n++;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("新增数据失败！", e.getMessage());
        }
        return n;
    }

    /**
     * 保存梳理的关系结果
     */
    @Override
    public long saveRelationResult(String resultStr, int type) {
        Assert.hasText(resultStr, "关系不能为空！");
        long n = 0;
        try {
            Result result = JsonUtils.jsonToObject(resultStr, Result.class);
            InnerRelation relation = result.getRelation();
            Ids ids = result.getObjIds();
            StructuredText structuredText = selectById(ids.getBaseId());
            List<Map<String, String>> data = getConditionData(result, structuredText.getId(), type);
            for (Map<String, String> map : data) {
                String source = relation.getSource();
                String target = relation.getTarget();
                String relationSource = "";
                String relationTarget = "";
                if (map.containsKey(source)) {
                    relationSource = map.get(source);
                }
                if (map.containsKey(target)) {
                    relationTarget = map.get(target);
                }
                List<FormData> sourceData = getNameFormData(ids.getSourceId(), relationSource);
                List<FormData> targetData = getNameFormData(ids.getSourceId(), relationTarget);
                if (sourceData.size() > 0 && targetData.size() > 0) {
                    TripletData tripletData = tripletDataService.selectOne(SqlQuery.from(TripletData.class)
                            .equal(TripletDataInfo.SOURCEID, sourceData.get(0).getId())
                            .equal(TripletDataInfo.TARGETID, targetData.get(0).getId())
                            .equal(TripletDataInfo.RELATIONID, relation.getRelation()));
                    if (tripletData == null) {
                        if (sourceData.size() > 0 && targetData.size() > 0) {
                            if (StringUtils.hasText(relationSource) && StringUtils.hasText(relationTarget)) {
                                RealmClass realmClass_s = realmClassService.selectOne(SqlQuery.from(RealmClass.class).equal(RealmClassInfo.FORMID, ids.getSourceId()));
                                RealmClass realmClass_t = realmClassService.selectOne(SqlQuery.from(RealmClass.class).equal(RealmClassInfo.FORMID, ids.getTargetId()));
                                tripletData = new TripletData(ids.getBaseId(), realmClass_s.getFormId(), realmClass_s.getFormAlias(), sourceData.get(0).getId(), relationSource, realmClass_t.getFormId(), realmClass_t.getFormAlias(), targetData.get(0).getId(), relationTarget, relation.getRelation(), "", source, target);
                                tripletData.setRelationName(relationService.selectById(relation.getRelation()).getRelationName());
                                long re = tripletDataService.insert(tripletData);
                                if (re > 0) {
                                    n++;
                                }
                            }
                            if (n > 0) {
                                //标注对象序号
                                markRelationClass(ids.getBaseId(), relation);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n;
    }

    /**
     * 读取excel文件表头
     *
     * @param id
     * @param type 1为横向，2为纵向
     * @return
     */
    @Override
    public List<String> excelTitle(String id, int type) {
        List<String> titleList = new ArrayList<>();
        // 获取文件的输入流
        InputStream inputStream = null;
        //从本地获取文件
        File file = new File(selectById(id).getFilePath());
        //获取文件类型
        String fileType = file.getName().substring(file.getName().lastIndexOf("."));
        try {
            // 获取文件的输入流
            inputStream = new FileInputStream(file);
            if (FILETYPE.equals(fileType)) {
                // 后缀为.xlsx则用XSSFWorkbook
                workbook = new XSSFWorkbook(inputStream);
            } else {
                // 后缀为.xls则用HSSFWorkbook
                workbook = new HSSFWorkbook(inputStream);
            }
            Sheet sheet = workbook.getSheetAt(0);
            //获取第一行
            Row titleRow = sheet.getRow(0);
            //有多少列
            int cellNum = titleRow.getLastCellNum();
            //纵向
            if (type == 2) {
                //有多少行;
                int lastRowNum = sheet.getLastRowNum() + 1;
                for (int i = 0; i < lastRowNum; i++) {
                    //根据索引获取对应的每一行的第一个列
                    // getRow获取第i行
                    String titleValue = new DataFormatter().formatCellValue(sheet.getRow(i).getCell(0));
                    titleList.add(titleValue);
                }
            } else {//横向
                for (int i = 0; i < cellNum; i++) {
                    String titleValue = new DataFormatter().formatCellValue(titleRow.getCell(i));
                    titleList.add(titleValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return titleList;
    }

    /**
     * 保存对象标记位置
     */
    private void markRelationClass(String id, InnerRelation relation) {
        String source = relation.getSource();
        String target = relation.getTarget();
        StructuredText structuredText = selectOne(SqlQuery.from(StructuredText.class).equal(StructuredTextInfo.ID, id));
        String markResult = structuredText.getMarkResult();
        Map<String, Integer> map = new HashMap<>();
        Gson gson = new Gson();
        try {
            if (StringUtils.hasText(markResult)) {
                map = gson.fromJson(markResult, Map.class);
                //获取map的value最大值
                Object[] array = map.values().toArray();
                Arrays.sort(array);
                int max = Double.valueOf(array[array.length - 1].toString()).intValue() + 1;
                if (!map.containsKey(source)) {
                    map.put(source, max);
                    max++;
                }
                if (!map.containsKey(target)) {
                    map.put(target, max);
                }
            } else {
                map.put(source, 1);
                map.put(target, 2);
            }
            structuredText.setMarkResult(gson.toJson(map));
            updateById(structuredText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取纵向排列档案的内容为对象
     *
     * @param id
     * @return
     */
    public List<Map<String, String>> readMarriageFileExcel(String id) {
        //读取表头
        List<String> titleList = excelTitle(id, 2);
        //从本地获取文件
        File file = new File(selectById(id).getFilePath());
        //获取文件类型
        String fileType = file.getName().substring(file.getName().lastIndexOf("."));
        InputStream inputStream = null;
        //用来存储总对象的list
        List<Map<String, String>> excelList = new ArrayList<>();
        Workbook workbook;
        try {
            // 获取文件的输入流
            inputStream = new FileInputStream(file);
            if (FILETYPE.equals(fileType)) {
                // 后缀为.xlsx则用XSSFWorkbook
                workbook = new XSSFWorkbook(inputStream);
            } else {
                // 后缀为.xls则用HSSFWorkbook
                workbook = new HSSFWorkbook(inputStream);
            }
            Sheet sheet = workbook.getSheetAt(0);
            //获取第一行
            Row titleRow = sheet.getRow(0);
            //有多少列
            int cellNum = titleRow.getLastCellNum();
            //有多少行;
            int lastRowNum = sheet.getLastRowNum() + 1;
            for (int j = 1; j < cellNum; j++) {
                //一条对象
                Map<String, String> excelMap = new HashMap<>();
                //第一列为表头，所以从第二列开始
                for (int i = 0; i < lastRowNum; i++) {
                    //getRow是第几行  getCell是第几列  getStringCellValue() 当前cell列对应的数据
                    Cell cell = sheet.getRow(i).getCell(j);
                    int length = cell.toString().length();
                    String cellValue;
                    CellType cellType = cell.getCellType();
                    //判断为poi无法直接识别的数字串，判断是不是日期
                    if (cellType.name().equals("NUMERIC") && length < 18) {
                        cellValue = convertCellValueDataToString(cell);
                    } else {
                        cellValue = new DataFormatter().formatCellValue(sheet.getRow(i).getCell(j));
                        //如果当前格为空，则向前一列寻找
                        if (!StringUtils.hasText(cellValue)) {
                            cellValue = new DataFormatter().formatCellValue(sheet.getRow(i).getCell(j - 1));
                        }
                    }
                    excelMap.put(titleList.get(i), cellValue);
                }
                excelList.add(excelMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return excelList;
    }

    /**
     * 从服务器下载文件
     */
    @Override
    public void download(String id, HttpServletResponse response) {
        File file = new File(selectById(id).getFilePath());
        //获取文件完整路径
        FileInputStream fis;
        try {
            //读取Excel文件
            InputStream inputStream = new FileInputStream(file);
            // 构造 XSSFWorkbook 对象，strPath 传入文件路径
            Workbook workbook = null;
            //获取Excel工作薄
            if (file.getName().endsWith("xlsx")) {
                workbook = new XSSFWorkbook(inputStream);
            } else {
                workbook = new HSSFWorkbook(inputStream);
            }
            if (workbook == null) {
                throw new RuntimeException("Excel文件有问题,请检查！");
            }
            // 读取第一个sheet页表格内容
            Sheet sheet = workbook.getSheetAt(0);
            OutputStream os = response.getOutputStream();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            // 返回数据到输出流对象中
            os.write(baos.toByteArray());
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过条件从excel读出数据，返回list<map<String,String>>,map理解为一个对象，里面有很多字段数据
     */
    public List<Map<String, String>> getConditionData(Result result, String id, int type) {
        //excel全部数据
        List<Map<String, String>> list = new ArrayList<>();
        //excel表头
        List<String> excelTitle = excelTitle(id, type);
        InputStream inputStream = null;
        //从本地获取文件
        File file = new File(selectById(id).getFilePath());
        //获取文件类型
        String fileType = file.getName().substring(file.getName().lastIndexOf("."));
        try {
            // 获取文件的输入流
            inputStream = new FileInputStream(file);
            //过滤条件
            List<Condition> conditions = result.getCondition();
            if (type == 1) {
                if (FILETYPE.equals(fileType)) {
                    // 后缀为.xlsx则用XSSFWorkbook
                    workbook = new XSSFWorkbook(inputStream);
                } else {
                    // 后缀为.xls则用HSSFWorkbook
                    workbook = new HSSFWorkbook(inputStream);
                }
                Sheet sheet = workbook.getSheetAt(0);
                //获取excel表单的所有内容
                for (int j = 1; j < sheet.getLastRowNum() + 1; j++) {
                    HashMap<String, String> excelMap = null;
                    String cellValue = "";
                    for (int i = 0; i < excelTitle.size(); i++) {
                        //getRow 第几行 getCell第几列 getStringCellValue 具体的值
                        Row row = sheet.getRow(j);
                        excelMap = new HashMap<>(excelTitle.size());
                        for (int k = 0; k < excelTitle.size(); k++) {
                            Cell cell = row.getCell(k);
                            if (cell == null) continue;
                            CellType cellType = cell.getCellType();
                            int length = cell.toString().length();
                            if (cellType.name().equals("NUMERIC") && length < 10) {
                                cellValue = convertCellValueDataToString(cell);
                            } else {
                                cellValue = new DataFormatter().formatCellValue(cell);
                            }
                            //new DataFormatter().formatCellValue格式化单元格内容，防止类型报错
                            excelMap.put(excelTitle.get(k), cellValue);
                        }
                    }
                    list.add(excelMap);
                }
            } else {
                list = readMarriageFileExcel(id);
            }
            //根据过滤条件筛选内容
            for (Condition condition : conditions) {
                if (StringUtils.hasText(condition.getValue())) {
                    //接收过滤后的数据
                    List<Map<String, String>> arrayList = new ArrayList<>();
                    String name = condition.getName();
                    for (int j = 0; j < list.size(); j++) {
                        Map<String, String> row = list.get(j);
                        String value = row.get(name);
                        if (StringUtils.hasText(value)) {
                            switch (condition.getWhen()) {
                                case "=":
                                    if (condition.getValue().equals(value)) {
                                        arrayList.add(row);
                                    }
                                    break;
                                case "<":
                                    if (condition.getValue().matches("-?[0-9]+.?[0-9]*") && value.matches("-?[0-9]+.?[0-9]*")) {
                                        if (Integer.parseInt((value)) < Integer.parseInt(condition.getValue())) {
                                            arrayList.add(row);
                                        }
                                    }
                                    break;
                                case ">":
                                    if (condition.getValue().matches("-?[0-9]+.?[0-9]*") && value.matches("-?[0-9]+.?[0-9]*")) {
                                        if (Integer.parseInt((value)) > Integer.parseInt(condition.getValue())) {
                                            arrayList.add(row);
                                        }
                                    }
                                    break;
                                case "like":
                                    if (value.contains(condition.getValue())) {
                                        arrayList.add(row);
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    list = arrayList;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 通过名字从对象库中找数据
     *
     * @param formId
     * @param value
     * @return
     */
    public List<FormData> getNameFormData(String formId, String value) {
        return formDataService.selectFormData(formId, (sqlQuery, wrapper) -> sqlQuery.equal(wrapper.getColumn("NAME"), value.trim()));
    }
}
