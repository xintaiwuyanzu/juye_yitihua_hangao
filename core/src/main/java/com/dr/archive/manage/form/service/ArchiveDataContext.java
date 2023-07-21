package com.dr.archive.manage.form.service;

import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.framework.common.form.engine.model.core.FormModel;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.security.SecurityHolder;
import org.apache.commons.collections4.MapUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 表单数据操作上下文，用来保存表单操作的相关参数
 *
 * @author dr
 */
public class ArchiveDataContext {
    /**
     * 前端传过来命令的key
     */
    public static final String COMMAND_KEY = "command";
    /**
     * 不执行的plugin
     */
    private final Set<String> excludePlugins = Collections.synchronizedSet(new HashSet<>());
    /**
     * 这里用来存储变量
     */
    private final Map<String, Object> paramsMap = new HashMap<>();
    /**
     * 父上下文
     * 可以为空
     */
    private ArchiveDataContext parent;
    /**
     * 业务场景命令
     * 可能为空
     */
    private String command;
    /**
     * 全宗
     */
    private Fond fond;
    /**
     * 门类
     */
    private Category category;
    /**
     * 表单定义对象
     */
    private FormModel formModel;
    private Person person;
    private Organise organise;
    /**
     * 当前循环到第几级了，打印使用
     */
    private int level = 1;
    /**
     * 是否继续执行拦截
     * TODO 拦截跳转的功能将来再说
     */
    private boolean pluginContinue = true;

    public ArchiveDataContext(ArchiveDataContext parent, String command, Fond fond, Category category, FormModel formModel) {
        this.parent = parent;
        this.command = command;
        this.fond = fond;
        this.category = category;
        this.formModel = formModel;
        if (parent != null) {
            this.level = parent.level + 1;
            if (!StringUtils.hasText(command)) {
                this.command = parent.command;
            }
        }
        SecurityHolder securityHolder = SecurityHolder.get();
        if (securityHolder != null) {
            this.person = securityHolder.currentPerson();
            this.organise = securityHolder.currentOrganise();
        }
    }

    public boolean isEnable(ArchiveDataPlugin plugin) {
        Class className = AopUtils.getTargetClass(plugin);
        return !excludePlugins.contains(className.getName())
                && plugin.acceptCommand(this);
    }

    /**
     * 根据{@link ArchiveDataPlugin#getOrder()}排序的plugin，
     * 可以在前面plugin执行的时候，手动排除后面指定的plugin
     * <p>
     * 去掉指定的plugin
     *
     * @param pluginClass
     */
    public void excludePlugin(Class<? extends ArchiveDataPlugin> pluginClass) {
        if (pluginClass != null) {
            excludePlugins.add(pluginClass.getName());
        }
    }

    /**
     * 批量添加属性
     *
     * @param attrs
     */
    public void addParams(Map<String, Object> attrs) {
        if (attrs != null) {
            paramsMap.putAll(attrs);
        }
    }

    /**
     * 添加属性
     *
     * @param key
     * @param value
     */
    public void addParams(String key, Object value) {
        if (StringUtils.hasText(key)) {
            paramsMap.put(key, value);
        }
    }

    /**
     * 获取自负床类型的value
     *
     * @param key
     * @param useParent 是否用父环境的属性
     * @return
     */
    public String getString(String key, boolean useParent) {
        if (useParent && parent != null) {
            return Optional.ofNullable(getString(key)).orElseGet(() -> parent.getString(key, true));
        }
        return getString(key);
    }

    /**
     * 获取字符串类型的value
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        return MapUtils.getString(paramsMap, key);
    }

    /**
     * 获取Object类型的参数
     *
     * @param key
     * @param useParent
     * @param <T>
     * @return
     */
    public <T> T getObject(String key, boolean useParent) {
        if (useParent && parent != null) {
            return (T) Optional.ofNullable(getObject(key)).orElseGet(() -> parent.getObject(key, true));
        }
        return getObject(key);
    }

    /**
     * 获取Object类型的参数
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> T getObject(String key) {
        return (T) MapUtils.getObject(paramsMap, key);
    }

    /**
     * 获取boolean类型的参数
     *
     * @param key
     * @param useParent
     * @return
     */
    public Boolean getBoolean(String key, boolean useParent) {
        if (useParent && parent != null) {
            return Optional.ofNullable(getBoolean(key)).orElseGet(() -> parent.getBoolean(key, true));
        }
        return getBoolean(key);
    }

    /**
     * 获取boolean类型的参数
     *
     * @param key
     * @return
     */
    public Boolean getBoolean(String key) {
        return MapUtils.getBoolean(paramsMap, key);
    }

    /**
     * 获取int类型的参数
     *
     * @param key
     * @param useParent
     * @return
     */
    public Integer getInteger(String key, boolean useParent) {
        if (useParent && parent != null) {
            return Optional.ofNullable(getInteger(key)).orElseGet(() -> parent.getInteger(key, true));
        }
        return getInteger(key);
    }

    /**
     * 获取int类型的参数
     *
     * @param key
     * @return
     */
    public Integer getInteger(String key) {
        return MapUtils.getInteger(paramsMap, key);
    }

    /**
     * 获取long类型的参数
     *
     * @param key
     * @param useParent
     * @return
     */
    public Long getLong(String key, boolean useParent) {
        if (useParent && parent != null) {
            return Optional.ofNullable(getLong(key)).orElseGet(() -> parent.getLong(key, true));
        }
        return getLong(key);
    }

    /**
     * 获取long类型的参数
     *
     * @param key
     * @return
     */
    public Long getLong(String key) {
        return MapUtils.getLong(paramsMap, key);
    }

    public ArchiveDataContext getParent() {
        return parent;
    }

    public String getCommand() {
        return command;
    }

    public Fond getFond() {
        return fond;
    }

    public Category getCategory() {
        return category;
    }

    public FormModel getFormModel() {
        return formModel;
    }

    public int getLevel() {
        return level;
    }

    public Person getPerson() {
        return person;
    }

    public Organise getOrganise() {
        return organise;
    }
}
