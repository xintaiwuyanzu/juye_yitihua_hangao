package com.dr.archive.init;

import com.dr.framework.core.security.entity.SubSystem;
import com.dr.framework.core.security.entity.SysMenu;
import com.dr.framework.sys.service.SysMenuService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 菜单初始化父类
 *
 * @author dr
 */
public abstract class AbstractMenuInit extends AbstractClassPathResourceDataInit {

    @Autowired
    SysMenuService menuService;

    @Override
    protected String getRefType() {
        return SysMenuService.RESOURCE_TYPE;
    }

    @Override
    protected void doInitData(JsonNode node) {
        AtomicInteger orderCount = new AtomicInteger();
        node.forEach(n -> {
            orderCount.getAndIncrement();
            addMenu(null, orderCount.get(), n);
        });
    }

    protected void addMenu(SysMenu parent, int order, JsonNode menuNode) {
        //执行添加菜单操作
        SysMenu menu = new SysMenu();
        if (menuNode.has("name")) {
            menu.setName(menuNode.get("name").asText());
        }
        if (menuNode.has("icon")) {
            menu.setIcon(menuNode.get("icon").asText());
        }
        if (menuNode.has("url")) {
            menu.setUrl(menuNode.get("url").asText());
        }
        if (menuNode.has("order")) {
            menu.setOrder(menuNode.get("order").intValue());
        } else {
            menu.setOrder(order);
        }
        if (parent != null) {
            menu.setParentId(parent.getId());
            menu.setLeaf(true);
        } else {
            menu.setLeaf(false);
            menu.setParentId(SubSystem.DEFAULT_SYSTEM_ID);
        }
        menuService.insert(menu);
        if (menuNode.has("children")) {
            AtomicInteger orderCount = new AtomicInteger();
            menuNode.get("children").forEach(n -> {
                orderCount.getAndIncrement();
                addMenu(menu, orderCount.get(), n);
            });
        }
    }

}
