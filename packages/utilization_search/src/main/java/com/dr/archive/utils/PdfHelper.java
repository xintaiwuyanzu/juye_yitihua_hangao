package com.dr.archive.utils;

import com.dr.archive.utils.itextpdf.HighLightSelector;
import com.dr.archive.utils.itextpdf.WaterMarker;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.pdfcleanup.autosweep.PdfAutoSweepTools;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


/**
 * 描述：Pdf帮助类
 *
 * @author tuzl
 * @date 2020/7/17 8:44
 */
public class PdfHelper {

    /**
     * 单页关键字高亮
     *
     * @param ios
     * @param keyWord
     * @param outputStream
     */
    public static void highLightKeyWord(InputStream ios, String keyWord, OutputStream outputStream) throws Exception {
        PdfReader reader = new PdfReader(ios);
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument document = new PdfDocument(reader, writer);
        List<String> keywords = AnalyzerUtils.separateWord(keyWord);

        HighLightSelector highLightSelector = new HighLightSelector(keywords);

        PdfAutoSweepTools autoSweep = new PdfAutoSweepTools(highLightSelector);
        autoSweep.highlight(document);
        document.close();
    }

    /**
     * 添加水印
     *
     * @param ios
     * @param markerStr
     * @param outputStream
     * @throws Exception
     */
    public static void waterMark(InputStream ios, String markerStr, float colorGray, String color, float size, int textH, int textW, int h0, int w0, OutputStream outputStream) throws IOException {
        PdfReader reader = new PdfReader(ios);
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument document = new PdfDocument(reader, writer);
        WaterMarker marker = new WaterMarker(markerStr);
        marker.mark(document, colorGray, color, size, textH, textW, h0, w0);
        document.close();
    }


    private static void paint(String path) throws Exception {
        int width = 400;
        int height = 200;
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2d = (Graphics2D) bi.getGraphics();
        //设置颜色为白色的填充形矩形
        graphics2d.setColor(Color.WHITE);
        graphics2d.fillRect(0, 0, 400, 200);
        FileOutputStream fos = new FileOutputStream(path);
        ImageIO.write(bi, "PNG", fos);

        fos.flush();
        fos.close();

        graphics2d.dispose();
    }

    public static void transferAlpha(String photoPath, String newPath) {
        File file = new File(photoPath);
        InputStream is;
        try {
            is = Files.newInputStream(file.toPath());
            // 如果是MultipartFile类型，那么自身也有转换成流的方法：is = file.getInputStream();
            BufferedImage bi = ImageIO.read(is);
            ImageIcon imageIcon = new ImageIcon(bi);
            BufferedImage bufferedImage = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
            g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
            int alpha = 0;
            for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); j1++) {
                for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); j2++) {
                    int rgb = bufferedImage.getRGB(j2, j1);

                    int R = (rgb & 0xff0000) >> 16;
                    int G = (rgb & 0xff00) >> 8;
                    int B = (rgb & 0xff);
                    if (((255 - R) < 30) && ((255 - G) < 30) && ((255 - B) < 30)) {
                        rgb = ((alpha + 1) << 24) | (rgb & 0x00ffffff);
                    }

                    bufferedImage.setRGB(j2, j1, rgb);

                }
            }

            g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());
            is.close();
            boolean delete = file.delete();
            ImageIO.write(bufferedImage, "png", new File(newPath));// 直接输出文件
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能描述: 某页pdf复制为新的pdf
     *
     * @param pdfFile
     * @param newFile
     * @param from
     * @param end
     * @return :
     * @author : tzl
     * @date : 2020/7/21 9:50
     */
    public static void partitionPdfFile(String pdfFile, String newFile, int from, int end) throws IOException {
        partitionPdfFile(Files.newInputStream(Paths.get(pdfFile)), Files.newOutputStream(Paths.get(newFile)), from, end);
    }

    public static void partitionPdfFile(InputStream pdfInput, OutputStream pdfOutput, int from, int end) throws IOException {
        PdfDocument source = new PdfDocument(new PdfReader(pdfInput));
        PdfDocument target = new PdfDocument(new PdfWriter(pdfOutput));
        new PdfMerger(target).setCloseSourceDocuments(true).merge(source, from, end).close();
    }

    /**
     * 处理Image
     * <p>
     * 以左上角顶点为基准  截取图片
     *
     * @param posX   左上角顶点的x轴
     * @param posY   左上角顶点的x轴
     * @param width  图片的宽度
     * @param height 图片的高度
     * @return img
     */
    public static Image getImage(String imgPath, int posX, int posY, int width, int height) {
        File file = new File(imgPath);
        Rectangle rect = new Rectangle(posX, posY, width, height);
        FileInputStream fis = null;
        ImageInputStream iis = null;
        try {
            String suffix = StringUtils.substringAfterLast(file.getName(), ".");
            fis = new FileInputStream(file);
            // 将FileInputStream 转换为ImageInputStream
            iis = ImageIO.createImageInputStream(fis);
            // 根据图片类型获取该种类型的ImageReader
            ImageReader reader = ImageIO.getImageReadersBySuffix(suffix).next();
            reader.setInput(iis, true);
            ImageReadParam param = reader.getDefaultReadParam();
            param.setSourceRegion(rect);
            BufferedImage image = reader.read(0, param);
            // 获取抽象路径
            String path = file.getPath();
            // 判断目录是否存在
            if (!file.isDirectory()) {
                path = file.getParent();
            }
            // 判断路径最后是否是目录转换符
            if (!path.endsWith(File.separator)) {
                path = path + File.separator;
            }
            // 写入新文件
            String pathNew = path.substring(0, path.lastIndexOf(File.separator)) + File.separator + StringUtils.substringBeforeLast(file.getName(), ".jpg") + "." + StringUtils.substringAfterLast(file.getName(), ".");
            ImageIO.write(image, suffix, new File(pathNew));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (iis != null) {
                    iis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将html文本转换为pdf文件
     *
     * @param html
     * @param onlyBody     html内容是不是只是div之类的，没有html标签
     * @param outputStream
     */
    public static void htmlToPdf(String baseUri, String html, boolean onlyBody, OutputStream outputStream) {
        PdfWriter writer = new PdfWriter(outputStream);
        //设置图片访问地址
        ConverterProperties properties = new ConverterProperties();
        properties.setBaseUri(baseUri);
        // 设置字体（得在资源文件resources/fonts中加入字体文件，微软雅黑、宋体等）
        String fonts = new ClassPathResource("simsun.ttf").getPath();
        FontProvider fontProvider = new DefaultFontProvider();
        fontProvider.addFont(fonts);
        properties.setFontProvider(fontProvider);
        String content = html;
        if (onlyBody) {
            content = "<html><body style=\"font-family: 宋体, SimHei;\">" + html + "</body></html>";
        }
        //TODO 图片、样式未处理
        HtmlConverter.convertToDocument(content, writer, properties).close();
    }

    /**
     * 读取pdf流的页数
     *
     * @param stream
     * @return
     * @throws IOException
     */
    public static long readPdfNumbers(InputStream stream) throws IOException {
        PdfReader reader = new PdfReader(stream);
        PdfDocument document = new PdfDocument(reader);
        long result = document.getNumberOfPages();
        document.close();
        return result;
    }
}
