package tag;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author: qiuyf
 * @date: 2022/3/11 9:22
 */
public class TestNeo4j {
    public static void main(String[] args) {
        InputStreamReader fr = null;
        BufferedReader br = null;
        createCSV();
        try {
            fr = new InputStreamReader(Files.newInputStream(Paths.get("F:\\vertex.csv")));
            br = new BufferedReader(fr);
            String rec = null;
            String[] argsArr = null;
            while ((rec = br.readLine()) != null) {
                argsArr = rec.split(",");
                String aa = rec + ",ENTITY";
                writeCSVLine(aa, "F:\\vertex_output_all.csv");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fr != null)
                    fr.close();
                if (br != null)
                    br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    //创建CSV文件
    public static void createCSV() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        FileOutputStream fos = null;
        String sTitle = ":ID,name,:LABEL";
        try {
            out.write(sTitle.getBytes());
            out.write(",".getBytes());
            out.write("\n".getBytes());
            File newfile = new File("F:\\vertex_output_all.csv");
            fos = new FileOutputStream(newfile);
            fos.write(out.toByteArray());
            fos.flush();
            out.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) out.close();
                if (null != fos) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("生成" + "vertex_output_all.csv" + "完成");
    }

    //向CSV文件追加一行数据
    public static void writeCSVLine(String string1, String fName) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fName, true)); // 附加
            // 添加新的数据行
            bw.write(string1);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            // File对象的创建过程中的异常捕获
            e.printStackTrace();
        }// BufferedWriter在关闭对象捕捉异常

    }


}
