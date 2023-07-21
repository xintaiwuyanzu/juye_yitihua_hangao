package com.dr.archive.utils.itextpdf;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import java.io.IOException;

import static com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy.PREFER_NOT_EMBEDDED;

/**
 * @author dr
 */
public class WaterMarker {
    //水印字体
    final String waterMarkString;

    public WaterMarker(String waterMarkString) {
        this.waterMarkString = waterMarkString;
    }

    /**
     * 添加水印
     *
     * @param document
     */
    public void mark(PdfDocument document, float colorGray, String color, float size, int textH, int textW, int h0, int w0) throws IOException {
        int total = document.getNumberOfPages() + 1;
        for (int i = 1; i < total; i++) {
            PdfPage reader = document.getPage(i);
            Rectangle rectangle = reader.getPageSizeWithRotation();
            for (int height = h0; height < rectangle.getHeight();
                 height = height + textH) {
                for (int width = w0; width < rectangle.getWidth() + textW;
                     width = width + textW) {
                    PdfCanvas canvas = new PdfCanvas(reader);
                    //为汉字创建专门的字体
                    PdfFont f = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H", PREFER_NOT_EMBEDDED);
                    //透明度
                    canvas.setFillColorGray(colorGray)
                            .beginText()
                            .setFontAndSize(f, size)
                            .setWordSpacing(3f);

                    switch (color) {
                        case "0":
                            canvas.setFillColor(ColorConstants.LIGHT_GRAY);
                            break;
                        case "1":
                            canvas.setFillColor(ColorConstants.BLACK);
                            break;
                        case "2":
                            canvas.setFillColor(ColorConstants.BLUE);
                            break;
                        case "3":
                            canvas.setFillColor(ColorConstants.CYAN);
                            break;
                        case "4":
                            canvas.setFillColor(ColorConstants.DARK_GRAY);
                            break;
                        case "5":
                            canvas.setFillColor(ColorConstants.GRAY);
                            break;
                        case "6":
                            canvas.setFillColor(ColorConstants.GREEN);
                            break;
                        case "7":
                            canvas.setFillColor(ColorConstants.MAGENTA);
                            break;
                        case "8":
                            canvas.setFillColor(ColorConstants.ORANGE);
                            break;
                        case "9":
                            canvas.setFillColor(ColorConstants.PINK);
                            break;
                        case "10":
                            canvas.setFillColor(ColorConstants.RED);
                            break;
                        case "11":
                            canvas.setFillColor(ColorConstants.WHITE);
                            break;
                        case "12":
                            canvas.setFillColor(ColorConstants.YELLOW);
                            break;
                        default:
                            canvas.setFillColor(ColorConstants.LIGHT_GRAY);
                            break;
                    }
                    canvas.setTextMatrix(1f, 0f, 0f, 1f, width, height);
                    canvas.circle(10, 10, 10);
                    canvas.stroke();
                    canvas.setFlatnessTolerance(8f);
                    canvas.setMiterLimit(5f);
                    //TODO 位置大小颜色需要控制
                    canvas.newlineShowText(waterMarkString)
                            .endText();
                }
            }
        }
    }

}
