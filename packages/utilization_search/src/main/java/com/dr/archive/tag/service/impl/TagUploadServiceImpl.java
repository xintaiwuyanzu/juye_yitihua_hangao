package com.dr.archive.tag.service.impl;

import com.dr.archive.tag.entity.FactTag;
import com.dr.archive.tag.entity.FactTagInfo;
import com.dr.archive.tag.entity.StdTag;
import com.dr.archive.tag.entity.StdTagInfo;
import com.dr.archive.tag.service.FactTagService;
import com.dr.archive.tag.service.TagUploadService;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.file.autoconfig.CommonFileConfig;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author: yang
 * @create: 2022-07-15 09:51
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class TagUploadServiceImpl implements TagUploadService {

    static final Logger logger = LoggerFactory.getLogger(TagUploadServiceImpl.class);
    @Autowired
    CommonFileConfig fileConfig;
    @Autowired
    StdTagServiceImpl stdTagService;
    @Autowired
    FactTagService factTagService;
    String charset;

    private static void deleteZipExtract(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            for (File listFile : file.listFiles()) {
                if (listFile.isFile()) {
                    listFile.delete();
                } else {
                    deleteZipExtract(listFile.getPath());
                }
            }
        }
        file.delete();
    }

    @Override
    public void upload(MultipartFile file, String tagType) {
        try {
            //解压文件
            String unZipPath = unZip(file);
            //保存到数据库
            if (tagType.equals("std")) {
                saveToMysql(unZipPath);
            } else if (tagType.equals("fact")) {
                saveFactToMysql(unZipPath);
            }
        } catch (ZipException e) {
            logger.error("标签词库 ZIP 解压失败：" + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("标签词库文件保存失败：" + e.getMessage());
        }
    }

    @Override
    public void importWordTxt(MultipartFile file, String id, String type) {
        String ch = "\n";
        try {
            String path = save(file);
            String content = getContent(new File(path));
            charset = "utf-8";
            if (type.equals("std")) {
                StdTag stdTag = stdTagService.selectById(id);
                stdTag.setContent(stdTag.getContent() + ch + content);
                stdTagService.updateById(stdTag);
            } else if (type.equals("fact")) {
                FactTag factTag = factTagService.selectById(id);
                factTag.setContent(factTag.getContent() + ch + content);
                factTagService.updateById(factTag);
            }
            deleteZipExtract(fileConfig.getFullDirPath("tagUpload", "tag", null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToMysql(String unZipPath) {
        charset = "gbk";
        File file = new File(unZipPath);
        try {
            for (File f : Objects.requireNonNull(file.listFiles())) {
                //保存一级标签
                String[] split = f.getName().split("\\.");
                String firstName = split[split.length - 1];
                int order;
                try {
                    order = Integer.parseInt(split[0]);
                } catch (NumberFormatException e) {
                    order = 0;
                }
                List<StdTag> tags = stdTagService.selectList(SqlQuery.from(StdTag.class).equal(StdTagInfo.LABELNAME1ST, firstName));
                StdTag stdTagF = new StdTag(firstName, "", order + "", "");
                if (tags.size() == 0) {
                    stdTagF.setLabelId1st(UUIDUtils.getUUID());
                } else {
                    stdTagF.setLabelId1st(tags.get(0).getLabelId1st());
                }
                stdTagService.insert(stdTagF);

                //检索下级标签
                if (f.isDirectory()) {
                    int s_order = 0;
                    for (File listFile : Objects.requireNonNull(f.listFiles())) {
                        String secondName = listFile.getName();
                        if (listFile.isFile()) {
                            String content = getContent(listFile);
                            String[] splitSecondName = secondName.split("\\.");
                            try {
                                s_order = Integer.parseInt(secondName.split("\\.")[0]);
                            } catch (NumberFormatException e) {
                                s_order++;
                            }
                            StdTag stdTag = new StdTag(firstName, splitSecondName[splitSecondName.length - 2], stdTagF.getOrderInfo1st(), content);
                            stdTag.setOrderInfo2nd(s_order + "");
                            stdTag.setLabelId1st(stdTagF.getLabelId1st());
                            stdTag.setLabelId2nd(UUIDUtils.getUUID());
                            stdTagService.insert(stdTag);
                        }
                    }
                }
            }
        } finally {
            String path = fileConfig.getFullDirPath("tagUpload", "tag", null);
            deleteZipExtract(path);
        }
    }

    private void saveFactToMysql(String unZipPath) {
        charset = "gbk";
        File one = new File(unZipPath);
        try {
            for (File f : Objects.requireNonNull(one.listFiles())) {
                //保存一级标签
                String[] split = f.getName().split("\\.");
                String firstName = split[split.length - 1];
                int order;
                try {
                    order = Integer.parseInt(split[0]);
                } catch (NumberFormatException e) {
                    order = 0;
                }
                List<FactTag> tags = factTagService.selectList(SqlQuery.from(FactTag.class).equal(FactTagInfo.LABELNAME1ST, firstName));
                FactTag factTag = new FactTag(firstName, "", "", order + "", "", "");
                if (tags.size() == 0) {
                    factTag.setLabelId1st(UUIDUtils.getUUID());
                } else {
                    factTag.setLabelId1st(tags.get(0).getLabelId1st());
                }
                factTagService.insert(factTag);

                //检索下级标签
                if (f.isDirectory()) {
                    int f_order = 0;
                    for (File listFile : Objects.requireNonNull(f.listFiles())) {
                        //保存二级标签
                        String secondName = listFile.getName();
                        String[] splitSecondName = secondName.split("\\.");
                        try {
                            f_order = Integer.parseInt(splitSecondName[0]);
                        } catch (NumberFormatException ignored) {
                        }
                        List<FactTag> ts = factTagService.selectList(SqlQuery.from(FactTag.class).equal(FactTagInfo.LABELNAME2ND, secondName));
                        FactTag factTag2nd = new FactTag(factTag.getLabelName1st(), secondName, "", factTag.getOrderInfo1st(), "", f_order + "");
                        if (ts.size() == 0) {
                            factTag2nd.setLabelId2nd(UUIDUtils.getUUID());
                        } else {

                            factTag2nd.setLabelId2nd(ts.get(0).getLabelId2nd());
                        }
                        factTagService.insert(factTag2nd);
                        f_order++;
                        //查找二级下的txt文本，保存为三级标签
                        if (listFile.isDirectory()) {
                            for (File f1 : Objects.requireNonNull(listFile.listFiles())) {
                                String threeName = f1.getName();
                                if (f1.isFile()) {
                                    String content = getContent(f1);
                                    String[] strings = threeName.split("\\.");
                                    FactTag factTag2 = new FactTag(factTag2nd.getLabelName1st(), secondName, strings[strings.length - 2], factTag.getOrderInfo1st(), content, "");
                                    factTag2.setLabelId1st(factTag2nd.getLabelId1st());
                                    factTag2.setLabelId2nd(factTag2nd.getLabelId2nd());
                                    factTag2.setLabelId3rd(UUIDUtils.getUUID());
                                    factTagService.insert(factTag2);
                                }
                            }
                        }
                    }
                }
            }
        } finally {
            String path = fileConfig.getFullDirPath("tagUpload", "tag", null);
            deleteZipExtract(path);
        }

    }

    private String getContent(File f1) {
        String content = null;
        try (InputStream fis = Files.newInputStream(f1.toPath(), StandardOpenOption.READ)) {
            content = StreamUtils.copyToString(fis, Charset.forName(charset));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    private String unZip(MultipartFile file) throws ZipException, IOException {
        String path = save(file);//这里是调用了写入磁盘的方法，并返回的zip文件位置
        String unZipPath = fileConfig.getFullDirPath("tagUpload", "tag", new Date());//解压路径
        ZipFile zip = new ZipFile(path);//将文件位置放入 zip4j包里面的ZipFile类
        zip.setFileNameCharset("GBK");//设置编码  这里根据具体文件的格式来设置
        zip.extractAll(unZipPath);//解压到参数的路径上地震学专业术语【官方推荐】.txt
        File f = new File(path);//然后把之前上传的zip给干掉
        f.delete();//然后把之前上传的zip给干掉
        return unZipPath;
    }

    /**
     * 保存文件
     */
    private String save(MultipartFile multipartFile) throws IOException {
        //创建文件
        File file = new File(fileConfig.getFullDirPath("tagUpload", "tag", new Date()) + File.separator + multipartFile.getOriginalFilename());
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(multipartFile.getBytes());//写入数据
        outputStream.close();
        return file.getPath();
    }
}
