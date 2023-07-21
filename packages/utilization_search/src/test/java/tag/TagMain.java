package tag;

import com.dr.archive.tag.TagConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: qiuyf
 * @date: 2022/3/4 18:38
 */
public class TagMain {
    public static void main(String[] args) {
        //获取目录
        String xlsPath = "F:\\0283目录.xls";
        String dirPath = "F:\\2022-3-10\\txtHb\\0283";
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(Files.newInputStream(Paths.get(xlsPath)));
            HSSFSheet sheet = workbook.getSheet("0283目录");//获取工作表 0283目录

            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                HSSFRow row = sheet.getRow(i);//Row
                HSSFCell cell = row.getCell((short) 11);//Cell
                // String xlsKy = cell.getStringCellValue();
                //判断目录表中该行是否已有关键词
                if (StringUtils.isEmpty(cell)) {
                    //根据档号查找txt
                    HSSFCell cell2 = row.getCell((short) 2);
                    String txtPath = dirPath + "\\" + cell2.getStringCellValue() + "\\" + cell2.getStringCellValue() + ".txt";
                    loadTag(txtPath, row);

                }
            }
            workbook.write(new File("F:\\0283目录1.xls"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        //遍历文件夹txt
        /*File dir = new File(dirPath);
        for (File file : dir.listFiles()) {
            //判断是否txt
            for (File file1 : file.listFiles()) {
                loadTag(file1);
            }

        }*/
    }

    private static void loadTag(String txtPath, HSSFRow row) {

        File file = new File(txtPath);
        //判断文件不存在或是文件夹则返回
        if (!file.exists() || file.isDirectory()) {
            return;
        }
        RestTemplate restTemplate = new RestTemplate();
        TagConfig tagConfig = new TagConfig();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            //读取标签数据
            InputStreamReader isr = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sbf = new StringBuilder();
            String strTxt;
            while ((strTxt = reader.readLine()) != null) {
                sbf.append(strTxt);
            }
            reader.close();
            strTxt = sbf.toString();
            if ("\"\"".equals(strTxt.trim())) {
                return;
            }
            String result = "";
            String fileDir = file.getParentFile().getPath().replace("F:\\2022-3-10\\txtHb", "F:\\2022-3-10\\clh");
            File fileph = new File(fileDir);
            if (!fileph.exists()) {
                fileph.mkdirs();
            }

            //根据txt获取关键词标签
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("text", strTxt);
            params.add("count", "30");
            String result1 = "";
            JsonNode tagNode1 = objectMapper.readTree(restTemplate.postForEntity("http://192.168.1.143:81/aiplus/nlp/keywords/api/v1", params, String.class).getBody());
            result1 = tagNode1.get("data").get("keywords").toString();
            //根据txt获取实体标签
            MultiValueMap<String, String> params2 = new LinkedMultiValueMap<>();
            params2.add("text", strTxt);
            params2.add("remove_stop_words", "a");
            String result2 = "";
            JsonNode tagNode2 = objectMapper.readTree(restTemplate.postForEntity("http://192.168.1.143:81/aiplus/nlp/word/api/v1", params2, String.class).getBody());
            List<Map<String, String>> list = objectMapper.readValue(tagNode2.get("data").traverse(), objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, HashMap.class));
            List<String> stList = new ArrayList<>();
            stList.add("PER");
            stList.add("LOC");
            stList.add("ORG");
            stList.add("TIME");
            //List<Map<String, String>> entitys = list.stream().filter(entry -> stList.contains(entry.get("type"))).collect(Collectors.toList());
            List<Map<String, String>> entitys = new ArrayList<>();

            for (Map<String, String> map : list) {
                boolean flag = true;
                //去重 没想到好方法 先用土方法
                for (Map<String, String> aa : entitys) {
                    if (aa.get("实体").equals(map.get("name"))) {
                        flag = false;
                    }
                }
                if (stList.contains(map.get("type")) && flag) {

                    Map<String, String> map1 = new HashMap<>();
                    map1.put("实体", map.get("name"));
                    switch (map.get("type")) {
                        case "PER":
                            map1.put("类型", "人名");
                            break;
                        case "LOC":
                            map1.put("类型", "地名");
                            break;
                        case "ORG":
                            map1.put("类型", "机构名");
                            break;
                        default:
                            map1.put("类型", "时间");
                            break;
                    }
                    entitys.add(map1);
                }
            }
            result2 = entitys.toString();
            //保存标签数据

            //result = result1 + result2;
            String fName = file.getName().substring(0, file.getName().lastIndexOf("."));
            //关键词 文件
            String tagPathKey = fileDir + "\\" + fName + "_ky.txt";
            StreamUtils.copy(result1, Charset.defaultCharset(), Files.newOutputStream(Paths.get(tagPathKey)));
            HSSFCell gjcCell = row.createCell((short) 11);
            gjcCell.setCellValue(result1);
            //实体 文件
            String tagPathSt = fileDir + "\\" + fName + "_st.txt";
            StreamUtils.copy(result2, Charset.defaultCharset(), Files.newOutputStream(Paths.get(tagPathSt)));
            HSSFCell stCell = row.createCell((short) 12);
            stCell.setCellValue(result2);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }


}
