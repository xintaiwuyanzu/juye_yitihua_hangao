package com.dr.archive.utils.itextpdf;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.DefaultPdfTextLocation;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IPdfTextLocation;
import com.itextpdf.pdfcleanup.autosweep.ICleanupStrategy;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 用来捕获pdf高亮的位置
 *
 * @author dr
 */
public class HighLightSelector implements ICleanupStrategy {
    /**
     * 所有关键字
     */
    private final List<String> keyWords;
    /**
     * 忽略大小写
     */
    private final boolean ignoreCase;

    /**
     * 高亮颜色
     * WebColors.getRGBColor("rgba(1,0,0,0.1)")
     */
    private Color color = ColorConstants.RED;
    /**
     * 用来缓存所有扫描到的pdf路径
     */
    private final List<IPdfTextLocation> pdfTextLocations = new ArrayList<>();

    final Set<EventType> supportEventType;

    public HighLightSelector(String... keyWords) {
        this(false, keyWords);
    }

    public HighLightSelector(List<String> keyWords) {
        this(false, keyWords.toArray(new String[0]));
    }

    /**
     * 传入所有的关键子
     *
     * @param keyWords
     */
    public HighLightSelector(boolean ignoreCase, String... keyWords) {
        this.keyWords = new ArrayList();
        this.ignoreCase = ignoreCase;
        for (String key : keyWords) {
            if (!StringUtils.isEmpty(key)) {
                //TODO 这里应该用分词器分开
                this.keyWords.add(key.trim());
            }
        }
        supportEventType = new HashSet<>();
        //只监听文本渲染
        supportEventType.add(EventType.RENDER_TEXT);
        Assert.isTrue(!this.keyWords.isEmpty(), "关键字不能为空！");
    }

    @Override
    public void eventOccurred(IEventData data, EventType type) {
        if (type == EventType.RENDER_TEXT) {
            if (data instanceof TextRenderInfo) {
                TextRenderInfo textRenderInfo = (TextRenderInfo) data;
                String text = textRenderInfo.getText();
                for (String key : keyWords) {
                    if (text.contains(key)) {
                        //计算起始位置长宽
                        Rectangle rect = textRenderInfo.getBaseline().getBoundingRectangle().clone();
                        //rect.setHeight(textRenderInfo.getFontSize());
                        //TODO 没找到透明颜色，所以这里设置成高度为1的横线
                        rect.setHeight(1);
                        rect.setY(rect.getY() - 3);
                        //TODO 大小写问题
                        IPdfTextLocation location = new DefaultPdfTextLocation(0, rect, text);
                        pdfTextLocations.add(location);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public Set<EventType> getSupportedEvents() {
        return supportEventType;
    }

    @Override
    public Color getRedactionColor(IPdfTextLocation location) {
        return color;
    }

    @Override
    public ICleanupStrategy reset() {
        pdfTextLocations.clear();
        return this;
    }

    @Override
    public Collection<IPdfTextLocation> getResultantLocations() {
        return pdfTextLocations;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
