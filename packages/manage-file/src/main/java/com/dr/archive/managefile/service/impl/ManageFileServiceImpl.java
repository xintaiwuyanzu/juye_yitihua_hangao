package com.dr.archive.managefile.service.impl;

import com.dr.archive.managefile.entity.ManageFile;
import com.dr.archive.managefile.entity.ManageFileInfo;
import com.dr.archive.managefile.freemarker.RichHtmlHandler;
import com.dr.archive.managefile.freemarker.WordGeneratorWithFreemarker;
import com.dr.archive.managefile.service.ManageFileService;
import com.dr.archive.util.Constants;
import com.dr.archive.util.UUIDUtils;
import com.dr.archive.utils.PdfHelper;
import com.dr.framework.common.entity.TreeNode;
import com.dr.framework.common.file.autoconfig.CommonFileConfig;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.resource.FileSystemFileResource;
import com.dr.framework.common.file.service.CommonFileService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

/**
 * @Author: caor
 * @Date: 2022-04-07 17:26
 * @Description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ManageFileServiceImpl extends DefaultBaseService<ManageFile> implements ManageFileService {
    @Autowired
    CommonFileService commonFileService;
    @Autowired
    CommonFileConfig commonFileConfig;
    @Value("${server.port}")
    String serverPort;

    @Override
    public long insert(ManageFile entity) {
        if (commonMapper.countByQuery(SqlQuery.from(ManageFile.class).equal(ManageFileInfo.FILEINFOID, entity.getFileInfoId()).equal(ManageFileInfo.BUSINESSID, entity.getBusinessId())) > 0) {
            return 0;
        }
        FileInfo fileInfo = commonFileService.fileInfo(entity.getFileInfoId());
        entity.setFileName(fileInfo.getName());
        entity.setFileDescription(fileInfo.getDescription());
        entity.setSuffix(fileInfo.getSuffix());
        entity.setSaveDate(fileInfo.getSaveDate());
        return super.insert(entity);
    }

    @Override
    public List<TreeNode> getFileTree(String refId) {
        Assert.isTrue(StringUtils.hasText(refId), "业务id不能为空！");
        List<ManageFile> manageFileList = getManageFileListByBussinessId(refId);
        List<TreeNode> fileTree = new ArrayList<>();
        manageFileList.forEach(manageFile -> {
            TreeNode treeNode = new TreeNode(manageFile.getFileInfoId(), manageFile.getFileName(), refId, manageFile.getFileInfoId());
            treeNode.setParentId(refId);
            fileTree.add(treeNode);
        });
        return fileTree;
    }

    @Override
    public long deleteByRefId(String refId) {
        return commonMapper.deleteByQuery(SqlQuery.from(ManageFile.class).equal(ManageFileInfo.BUSINESSID, refId));
    }

    @Override
    public void htmlToPdf(String refId, String fileName, String html, boolean onlyBody) {
        Assert.isTrue(StringUtils.hasText(refId), "id不能为空");
        String filePath = commonFileConfig.getUploadDirWithDate("temp");
        File file = new File(filePath + File.separator + UUIDUtils.getUUID() + ".pdf");
        if (!file.exists()) {
            try (OutputStream outputStream = Files.newOutputStream(file.toPath())) {
                file.createNewFile();
                PdfHelper.htmlToPdf("http://localhost:" + serverPort, html, onlyBody, outputStream);
                FileSystemFileResource fileSystemFileResource = new FileSystemFileResource(file, "自动生成") {
                    @Override
                    public String getName() {
                        return fileName + ".pdf";
                    }
                };
                FileInfo fileInfo = commonFileService.addFile(fileSystemFileResource, refId, Constants.FILE_REFTYPE_MANAGE_FILE);
                ManageFile manageFile = new ManageFile();
                manageFile.setBusinessId(refId);
                manageFile.setFileInfoId(fileInfo.getId());
                insert(manageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //把自动生成的文件删掉
            file.delete();
        }
    }

    @Override
    public void htmlToPdf(String fileName, String html, boolean onlyBody, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        String name = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", name + ".pdf"));
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        try {
            PdfHelper.htmlToPdf("http://localhost:" + serverPort, html, onlyBody, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ManageFile> getManageFileListByBussinessId(String bussinessId) {
        return commonMapper.selectByQuery(SqlQuery.from(ManageFile.class).equal(ManageFileInfo.BUSINESSID, bussinessId).orderByDesc(ManageFileInfo.SAVEDATE));
    }

    @Override
    public void htmlToWords(String TitleName, String html, HttpServletResponse response, HttpServletRequest request) throws Exception {
        String wordPath = commonFileConfig.getFullDirPath("htmlToWord", "", new Date()) + File.separator + UUIDUtils.getUUID() + ".doc";
        String wordImgPath = commonFileConfig.getFullDirPath("htmlToWordImg", "", new Date()) + File.separator + UUIDUtils.getUUID() + ".png";
        Map<String, Object> res = new HashMap<String, Object>();
        if (html != null) {
            RichHtmlHandler handler = new RichHtmlHandler(html);
            handler.setDocSrcLocationPrex("file:///C:/268BA2D4");//用到前面mht文件中的值
            handler.setDocSrcParent("test.files");//用到前面mht文件中的值
            handler.setNextPartId("01D88659.C5F79CA0");//用到前面mht文件中的值
            handler.setShapeidPrex("_x56fe__x7247__x0020");
            handler.setSpidPrex("_x0000_i");
            handler.setTypeid("#_x0000_t75");

            handler.handledHtml(false, wordImgPath);

            String bodyBlock = handler.getHandledDocBodyBlock();

            String handledBase64Block = "";
            if (handler.getDocBase64BlockResults() != null
                    && handler.getDocBase64BlockResults().size() > 0) {
                for (String item : handler.getDocBase64BlockResults()) {
                    handledBase64Block += item + "\n";
                }
            }
            res.put("imagesBase64String", handledBase64Block);

            String xmlimaHref = "";
            if (handler.getXmlImgRefs() != null
                    && handler.getXmlImgRefs().size() > 0) {
                for (String item : handler.getXmlImgRefs()) {
                    xmlimaHref += item + "\n";
                }
            }
            res.put("imagesXmlHrefString", xmlimaHref);
            res.put("content", bodyBlock);

            File f = new File(wordPath);
            OutputStream out;
            try {
                out = new FileOutputStream(f);
                WordGeneratorWithFreemarker.createDoc(res, "html2word.ftl", out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
