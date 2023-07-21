package com.dr.archive.fuzhou;

import com.alibaba.fastjson.JSONObject;
import com.dr.archive.common.Application;
import com.dr.archive.fuzhou.bsp.BspSyncService;
import com.dr.archive.fuzhou.bsp.autoconfig.BspConfig;
import com.dr.archive.fuzhou.bsp.bo.BspOrganise;
import com.dr.archive.fuzhou.bsp.bo.BspRegion;
import com.dr.archive.fuzhou.bsp.bo.BspRole;
import com.dr.archive.fuzhou.configManager.service.FondDataSyncService;
import com.dr.archive.tag.entity.TagType;
import com.dr.archive.tag.service.TagTypeService;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.core.security.entity.SysMenu;
import com.inspur.service.ApplicationService;
import com.inspur.service.OrganizationService;
import com.inspur.service.UserAuthorityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class BspTest {

    Logger logger = LoggerFactory.getLogger(BspTest.class);
    @Autowired
    OrganizationService organizationService;
    @Autowired
    ApplicationService applicationClient;
    @Autowired
    BspSyncService bspSyncService;
    @Autowired
    BspConfig bspConfig;
    @Autowired
    UserAuthorityService userAuthorityService;
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    TagTypeService tagTypeService;

    @Test
    public void testRegionOrgenByRegionCode() {
        List<BspRegion> data = organizationService.getChildRegionByRegionCode(bspConfig.getRootRegionCode());
        logger.info("aa:{}", data);
        logger.info("aa:{}", data.size());
    }

    @Test
    public void testOrganInfo() {
        List<BspOrganise> data = organizationService.getOrganInfo(bspConfig.getConfigSysCode());
        logger.info("aa:{}", data);
        logger.info("aa:{}", data.size());
    }

    @Test
    public void getRegionOrgenByRegionCode() {
        List<BspOrganise> data = organizationService.getRegionOrgenByRegionCode(bspConfig.getRootRegionCode());
        logger.info("aa:{}", data);
        logger.info("aa:{}", data.size());
    }

    @Test
    public void testSync() {
        String result = bspSyncService.executeTask(null, null, null, null);
        logger.info(result);
    }

    @Test
    public void testGetPerson() {
        Object data = organizationService.getPostUserTree("12350100MB03153366");
        logger.info("testGetPerson:{}", data);
    }

    @Test
    public void testPersonInfo() {
        Object data = userAuthorityService.getUserInfoById("88888888888888888888888888888888");
        logger.info("testPersonInfo:{}", data);
        Object data1 = userAuthorityService.getUserByOrg("12350100MB03153366", bspConfig.getConfigSysCode(), "");
        logger.info("testPersonInfo:{}", data1);
    }


    @Test
    public void testApplist() {
        Object data = applicationClient.findAppinfoByKey(bspConfig.getPortalCode());
        logger.info("Applist:{}", data);
    }

    @Test
    public void testAppRole() {
        List<BspRole> data = applicationClient.getAppRoleTree(bspConfig.getDagCode());
        logger.info("getAppRoleTree:{}", data);
        for (BspRole datum : data) {
            BspRole role = userAuthorityService.getRoleInfoById(datum.getId());
            logger.info(role.toString());
        }
    }

    @Autowired
    FondDataSyncService fondDataSyncService;

    @Test
    public void dataSyncFondByOrgCode() {
        fondDataSyncService.dataSyncFondByOrgCode("11350100003604440K", new HashMap<>(), new HashSet<>());
    }

    @Test
    public void insertMenu() throws IOException {

        File file = ResourceUtils.getFile("classpath:init/menu.json");
        InputStream inputStream = Files.newInputStream(file.toPath());
        String o1 = JSONObject.parseObject(inputStream, String.class);
        List<Map> list = JSONObject.parseObject(o1, List.class);
        for (Map map : list) {
            SysMenu sysMenu = new SysMenu();
            sysMenu.setParentId("default");
            sysMenu.setName(map.get("name").toString());
            if (map.containsKey("url")) {
                sysMenu.setLeaf(true);
                sysMenu.setUrl(map.get("url").toString());
            } else {
                sysMenu.setLeaf(false);
            }
            sysMenu.setCreateDate(System.currentTimeMillis());
            sysMenu.setUpdateDate(System.currentTimeMillis());
            sysMenu.setCreatePerson("admin");
            sysMenu.setUpdatePerson("admin");
            sysMenu.setId(UUID.randomUUID().toString());
            commonMapper.insert(sysMenu);
            if (map.containsKey("children")) {
                getChildren(sysMenu.getId(), (List<Map>) map.get("children"));
            }
        }
    }

    public void getChildren(String parent, List<Map> maps) {

        for (Map map : maps) {
            SysMenu sysMenu = new SysMenu();
            sysMenu.setName(map.get("name").toString());
            if (map.containsKey("url")) {
                sysMenu.setLeaf(true);
                sysMenu.setUrl(map.get("url").toString());
            } else {
                sysMenu.setLeaf(false);
            }
            sysMenu.setParentId(parent);
            sysMenu.setCreateDate(System.currentTimeMillis());
            sysMenu.setUpdateDate(System.currentTimeMillis());
            sysMenu.setCreatePerson("admin");
            sysMenu.setUpdatePerson("admin");
            sysMenu.setId(UUID.randomUUID().toString());
            commonMapper.insert(sysMenu);
            if (map.containsKey("children")) {
                getChildren(sysMenu.getId(), (List<Map>) map.get("children"));
            }
        }
    }

    @Test
    public void testImp() {
        // String dirPath = "F:\\标签关键字\\标签关键字";
        String dirPath = "F:\\标签关键字\\标签关键字\\城市、地区、地址\\教育机构";
        File dir = new File(dirPath);
        aa(dir, "126c401b97ca40fe98f4153a048c3cc9");
    }

    public void aa(File dir, String parentId) {
        String fName = dir.getName();
        TagType tagType = new TagType();
        String id = UUIDUtils.getUUID();
        tagType.setTypeName(fName);
        tagType.setLeaf(0);
        tagType.setId(id);
        tagType.setParentId(parentId);
        tagTypeService.insert(tagType);
        dirdataImp(dir, id);
    }

    public void dirdataImp(File dir, String parentId) {

        for (File file : dir.listFiles()) {
            String fName = file.getName();
            if (file.isDirectory()) {
                TagType tagType = new TagType();
                String id = UUIDUtils.getUUID();
                tagType.setTypeName(fName);
                tagType.setLeaf(0);
                tagType.setId(id);
                tagType.setParentId(parentId);
                tagTypeService.insert(tagType);
                dirdataImp(file, id);
            } else if (file.getName().endsWith(".txt")) {
                TagType tagType1 = new TagType();
                String id1 = UUIDUtils.getUUID();
                tagType1.setTypeName(fName);
                tagType1.setLeaf(0);
                tagType1.setId(id1);
                tagType1.setParentId(parentId);
                tagTypeService.insert(tagType1);
                try {
                    InputStream is = Files.newInputStream(file.toPath());
                    String code = resolveCode(is);
                    InputStreamReader isr = new InputStreamReader(is, code);
                    BufferedReader br = new BufferedReader(isr);
                    String strLin = null;
                    while ((strLin = br.readLine()) != null) {
                        TagType tagType = new TagType();
                        String id = UUIDUtils.getUUID();
                        tagType.setTypeName(strLin);
                        tagType.setLeaf(1);
                        tagType.setId(id);
                        tagType.setParentId(id1);
                        tagTypeService.insert(tagType);
                    }
                } catch (IOException ignored) {

                }
            }
        }
    }

    public void txtdataImp(File txtfile, String parentId) {
        String fName = txtfile.getName();
        TagType tagType1 = new TagType();
        String id1 = UUIDUtils.getUUID();
        tagType1.setTypeName(fName);
        tagType1.setLeaf(0);
        tagType1.setId(id1);
        tagType1.setParentId(parentId);
        tagTypeService.insert(tagType1);
        try {
            InputStream is = Files.newInputStream(txtfile.toPath());
            String code = resolveCode(is);
            InputStreamReader isr = new InputStreamReader(is, code);
            BufferedReader br = new BufferedReader(isr);
            String strLin = null;
            while ((strLin = br.readLine()) != null) {
                TagType tagType = new TagType();
                String id = UUIDUtils.getUUID();
                tagType.setTypeName(strLin);
                tagType.setLeaf(1);
                tagType.setId(id);
                tagType.setParentId(id1);
                tagTypeService.insert(tagType);
            }
        } catch (IOException ignored) {

        }
    }

    public String resolveCode(InputStream is) throws IOException {
        String code = "GBK";
        byte[] head = new byte[3];

        is.read(head);


        if (head[0] == -1 && head[1] == -2)

            code = "UTF-16";

        else if (head[0] == -2 && head[1] == -1)

            code = "Unicode";

        else if (head[0] == -17 && head[1] == -69 && head[2] == -65)

            code = "UTF-8";
        return code;
    }
}
