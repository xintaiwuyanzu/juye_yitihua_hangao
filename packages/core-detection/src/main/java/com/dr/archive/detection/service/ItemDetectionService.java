package com.dr.archive.detection.service;

import com.dr.archive.detection.enums.LinkType;
import com.dr.archive.manage.category.entity.Category;
import org.springframework.core.Ordered;

/**
 * 四性检测接口
 * <p>
 * 所有四性检测接口都实现该接口，该接口是实现的是具体的四性检测小项
 *
 * @author dr
 */
public interface ItemDetectionService extends Ordered {


    /**
     * 四性检测配置类型
     *
     * @author dr
     */
    enum ConfigType {
        /**
         * 不管啥情况都执行四性检测，这种情况是自定义配置的
         */
        ALWAYS,
        /**
         * 四性检测配置了本类编码的{@link #code()}才执行检测，
         * 这种情况本类对应的编码是可以选择配置的
         */
        DETECT_WITH_CONFIG_CODE;
        //TODO 还有其他情况需要处理
    }

    /**
     * 是否可勾选配置
     *
     * @param linkType
     * @param category
     * @param year
     * @return
     */
    ConfigType configAble(LinkType linkType, Category category, String year);

    /**
     * 每个实现类只需要实现自己的编码即可
     * <p>
     * 因为检测编码对应的描述信息是相对固定的，所以描述信息可以写在配置文件中，通过上层处理加载出来即可
     * <p>
     * 实现方式编号
     *
     * @return
     */
    String modeCode();

    /**
     * 与智能归档检测编号相同
     * <p>
     * 检测编号
     *
     * @return
     */
    String code();

    /**
     * 在执行{@link #detection(ItemDetectContext)}方法后，检测结果数量没有变化时。
     * 是否自动追加检测成功结果。
     * <br>
     * <br>
     * 只有{@link #configAble(LinkType, Category, String)}返回结果不是
     * {@link ConfigType#ALWAYS}的类型才走下面的判断
     *
     * @return
     */
    default boolean autoAppendRecordWhenSuccess() {
        return true;
    }

    /**
     * 当{@link #autoAppendRecordWhenSuccess()}为true时，自动追加检测记录的额外字符串
     *
     * @return
     */
    default String getAppendString() {
        return "";
    }

    /**
     * 执行四性检测操作
     * <p>
     * 这个方法可以大胆抛出异常，只要不是runtime异常就可以，
     * 上层会接住异常，如果有异常，则该项四性检测不通过
     *
     * @param context 所有检测逻辑的结果都放到context中
     */
    void detection(ItemDetectContext context);

    @Override
    default int getOrder() {
        return 0;
    }

    ;
}
