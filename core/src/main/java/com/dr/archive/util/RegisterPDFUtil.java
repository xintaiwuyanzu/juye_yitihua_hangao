package com.dr.archive.util;


import com.dr.archive.manage.form.entity.RegisterWarehousing;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class RegisterPDFUtil {
    /**
     * 利用模板生成pdf导出
     */
    /**
     *
     * @param dataMap  数据
     * @param templatePath 模板地址 "E:\\000000AAAestJun\\模板1.pdf";
     * @param fileName 生成路径 "E:\\000000AAAestJun\\导出模板_success.pdf";
     */
    public static void pdfExportInfo(Map<String, String> dataMap,String templatePath,String fileName) {


        OutputStream out = null;
        ByteArrayOutputStream bos = null;
        PdfStamper stamper = null;
        PdfReader reader = null;
        try {
            // 读取PDF模板表单
            reader = new PdfReader(templatePath);
            // 字节数组流，用来缓存文件流
            bos = new ByteArrayOutputStream();
            // 根据模板表单生成一个新的PDF
            stamper = new PdfStamper(reader, bos);
            // 获取新生成的PDF表单
            AcroFields form = stamper.getAcroFields();
            // 给表单生成中文字体，这里采用系统字体，不设置的话，中文显示会有问题
            BaseFont font = BaseFont.createFont("C:/WINDOWS/Fonts/SIMSUN.TTC,1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            // Linux 需要安装字体。
            //BaseFont font = BaseFont.createFont("/SIMYOU.TTF", BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
            form.addSubstitutionFont(font);
            // 遍历data，给pdf表单赋值
            for (String key : dataMap.keySet()) {
                // 图片要单独处理
                if ("img".equals(key)) {
                    form.addSubstitutionFont(BaseFont.createFont("STSong-Light",
                            "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED));
                    //通过域名获取所在页和坐标，左下角为起点
                    int pageNo = form.getFieldPositions(key).get(0).page;
                    Rectangle signRect = form.getFieldPositions(key).get(0).position;
                    float x = signRect.getLeft();
                    float y = signRect.getBottom();
                    String studentImage = dataMap.get(key);
                    //根据路径或Url读取图片
                    Image image = Image.getInstance(studentImage);

                    //获取图片页面
                    PdfContentByte under = stamper.getOverContent(pageNo);
                    //图片大小自适应
                    image.scaleToFit(signRect.getWidth(), signRect.getHeight());
                    //添加图片
                    image.setAbsolutePosition(x, y);
                    under.addImage(image);
                } else {
                    // 设置普通文本数据
                    form.setField(key, dataMap.get(key));
                }
            }
            // 表明该PDF不可修改
            stamper.setFormFlattening(true);
            // 关闭资源
            stamper.close();
            // 将ByteArray字节数组中的流输出到out中（即输出到浏览器）
            Document doc = new Document();

            // 保存到本地
            out = new FileOutputStream(fileName);
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();
            doc.newPage();
            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importPage);

            doc.close();

            System.out.println("*****************************PDF导出成功*********************************");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void newPFD(String outPath, String content){

        Document document = new Document();
        try {
            // 创建PdfWriter对象
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outPath));
            // 打开文档
            document.open();

            // 方式三：使用iTextAsian.jar中的字体
            BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            Font font = new Font(baseFont);
            // 设置字体大小
            font.setSize(13);
            // 设置字体颜色
            font.setColor(new BaseColor(255, 0, 0));
            // 设置类型，加粗
            font.setStyle(Font.BOLD);
            // 设置类型，倾斜
            font.setStyle(Font.ITALIC);
            // 设置类型，下划线
            font.setStyle(Font.UNDERLINE);
            // 设置类型，可组合，倾斜+删除线
            font.setStyle(Font.ITALIC | Font.STRIKETHRU);
            // 设置类型，为正常
            font.setStyle(Font.NORMAL);

            // 块
            Chunk chunk = new Chunk("下标");
            // 设置字体，字体定宽
            chunk.setFont(new Font(baseFont, 4));
            // 设置背景颜色
            chunk.setBackground(new BaseColor(0xFF, 0xFF, 0x00));
            // 设置上表下标
            chunk.setTextRise(-3f);

            Paragraph paragraph = new Paragraph(content, font);
            // 试图将一个段落放在同一页中，该方法并不是始终有效
            paragraph.setKeepTogether(true);
            paragraph.add(chunk);
            document.add(paragraph);

            // low level
            PdfContentByte cb = writer.getDirectContent();
            cb.fill();
            cb.sanityCheck();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            // 关闭文档
            document.close();
        }
    }
    /**
     * @Description  默认处理中文显示
     * @return com.itextpdf.text.Font
     **/
    public static Font getColorFont() {
        Font font = new Font(getFont());

        return font;
    }

    /**
     * @Description 设置中文支持
     * @Param []
     * @return com.itextpdf.text.pdf.BaseFont
     **/
    public static BaseFont getFont() {
        BaseFont bf = null;
        try {
//
            bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            System.out.println("Exception = " + e.getMessage());
        }
        return bf;
    }

    /**
     * 自定义模板
     * @param registerWarehousing
     * @param list
     * @param path
     * @throws IOException
     * @throws DocumentException
     */
    public static void pdfZDY(RegisterWarehousing registerWarehousing, List<String[]> list,String path) throws IOException, DocumentException {

        Document document = new Document(PageSize.A4);
        //第二步，创建Writer实例
        PdfWriter.getInstance(document, new FileOutputStream(path));
        //创建中文字体
        BaseFont bfchinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font fontChinese = new Font(bfchinese, 12, Font.NORMAL);
        //第三步，打开文档
        document.open();



        //第四步，写入内容
        //创建一列的格子
        PdfPTable goodTable = new PdfPTable(6);

        goodTable.setWidthPercentage(100);
        //
        String fromName = registerWarehousing.getStatus().equals("1")?"入库登记表":"出库登记表";
        PdfPCell cell = new PdfPCell(new Phrase(fromName, getColorFont()));
        cell(goodTable,cell,6,35,1);


        PdfPCell  cell2 = new PdfPCell(new Phrase("操作人", getColorFont()));
        cell(goodTable,cell2,3,35,1);

        PdfPCell  cell3= new PdfPCell(new Phrase(registerWarehousing.getPerson_name(), getColorFont()));
        cell(goodTable,cell3,3,35,1);

        PdfPCell cell4 = new PdfPCell(new Phrase("入库时间", getColorFont()));
        cell(goodTable,cell4,3,35,1);

        String time = DateTimeUtils.longToDate(registerWarehousing.getCreateDate(), "yyyy-MM-dd HH:mm");
        PdfPCell cell5 = new PdfPCell(new Phrase(time, getColorFont()));
        cell(goodTable,cell5,3,35,1);

        PdfPCell cell6 = new PdfPCell(new Phrase("数量", getColorFont()));
        cell(goodTable,cell6,3,35,1);

        PdfPCell cell7 = new PdfPCell(new Phrase(registerWarehousing.getQuantity()+"", getColorFont()));
        cell(goodTable,cell7,3,35,1);

        PdfPCell cell8 = new PdfPCell(new Phrase("备注", getColorFont()));
        cell(goodTable,cell8,3,35,1);

        PdfPCell cell9 = new PdfPCell(new Phrase("", getColorFont()));
        cell(goodTable,cell9,3,35,1);


        //详情
        PdfPTable goodTable1 = new PdfPTable(15);
        goodTable1.setWidthPercentage(100);


        //表头
        String[] str = new String[]{"题名","档号","年度","文件形成日期","保管期限"};

        for (int i=0;i<str.length;i++){
            PdfPCell cellx = new PdfPCell(new Phrase(str[i], getColorFont()));
            cell(goodTable1,cellx,3,35,1);

        }

        //内容
        for (int i=0;i<list.size();i++){
            for (int j=0;j<list.get(i).length;j++){
                PdfPCell cellx = new PdfPCell(new Phrase(list.get(i)[j], getColorFont()));
                cell(goodTable1,cellx,3,35,1);
            }

        }



        document.add(goodTable);

        document.add(goodTable1);

        //第五步，关闭文档
        document.close();
        System.out.println("成功");
    }

    public static void cell(PdfPTable goodTable,PdfPCell cell,int kuan, int gaoPx, int gao){

        //格子横跨 x 个格子
        cell.setColspan(kuan);

//格子高度  xx px
        cell.setMinimumHeight(gaoPx);
//格子纵跨  xx  个格子
        cell.setRowspan(gao);
//格子内容左右居中
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//格子内容上下居中
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        goodTable.addCell(cell);
    }


}
