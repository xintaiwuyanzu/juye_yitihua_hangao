package com.dr.archive.fuzhou.ofd.service.impl;

import com.dr.archive.fuzhou.ofd.bo.FileByteInfo;
import com.dr.archive.fuzhou.ofd.bo.FileStreamResult;
import com.dr.archive.fuzhou.ofd.bo.OfdTransformRecord;
import com.dr.archive.fuzhou.ofd.service.OfdClient;
import com.dr.archive.fuzhou.ofd.service.OfdTransformService;
import com.dr.framework.common.file.autoconfig.CommonFileConfig;
import com.dr.framework.common.service.DefaultBaseService;
import com.google.common.io.Files;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;

/**
 * @author: yang
 * @create: 2022-05-16 17:48
 **/
@Service
@Transactional
public class OfdTransformServiceImpl extends DefaultBaseService<OfdTransformRecord> implements OfdTransformService {

    @Autowired
    OfdClient client;
    @Autowired
    CommonFileConfig commonFileConfig;

    @Override
    public void upload(MultipartFile file) {
        String uid = System.currentTimeMillis() + "";
        String fullName = file.getOriginalFilename();
        String fileName = fullName.substring(0,fullName.lastIndexOf("."));//源文件名称
        String fromType = fullName.substring(fullName.lastIndexOf(".")+1);//源文件类型
        try {
            //保存源文件
            String dirPath = commonFileConfig.getFullDirPath("ToOfd", "from", new Date());
            String from_filePath = dirPath + File.separator + uid + "." + fromType;
            OfdTransformRecord record = new OfdTransformRecord(uid, fileName, file.getSize(), fromType);
            saveFile(from_filePath, file.getBytes());
            //转换方法
            transformFile(record, file.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存文件
     */
    private void saveFile(String savePath, byte[] bytes) throws IOException {
        //创建文件
        File file = new File(savePath);
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(bytes);//写入数据
        outputStream.close();
    }


    /**
     * 从服务器下载文件
     */
    @Override
    public void download(String id, String type, HttpServletResponse response) {
        //通过文件名称获取文件
        OfdTransformRecord record = selectById(id);
        File file = getFullPathByUid(commonFileConfig.getWebPath(), record.getFileUid() + "." + type);
        //获取文件完整路径
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            response.reset();
            response.setHeader("Content-Type", "application/ofd");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(record.getFileName()+"."+type, "UTF-8"));
            OutputStream out = response.getOutputStream();
            out.write(bytes);
            fis.close();
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 转换操作
     */
    public void transformFile(OfdTransformRecord record, byte[] bytes) throws Exception {
        File file = getFullPathByUid(commonFileConfig.getWebPath(), record.getFileUid() + "." + record.getFromType());
        FileByteInfo fileByteInfo = FileByteInfo.fromFile(file);
        //转换
        FileStreamResult fileStreamResult = client.convertStream(fileByteInfo);
        String status = fileStreamResult.getConvertStatus();
        if (status.equals("0")) {
            //保存转换后文件
            String to_filePath = commonFileConfig.getFullDirPath("ToOfd", "to", new Date()) + File.separator + record.getFileUid() + "." + record.getToType();
            saveFile(to_filePath, bytes);
        }
        record.setStatus(status.equals("0") ? "0" : "1");//设置状态
        if(record.getId()==null){
            insert(record);
        }else {
            updateById(record);
        }
    }


    /**
     * 根据记录 重新转换/开始转换文件
     *
     * @param record 记录
     */
    @Override
    public void transform(OfdTransformRecord record) {
        record.setUpdateDate(System.currentTimeMillis());
        File file = getFullPathByUid(commonFileConfig.getWebPath(), record.getFileUid() + "." + record.getFromType());
        try {
            transformFile(record, Files.toByteArray(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过文件名获取绝对路径的文件
     *
     * @param root     files文件夹的绝对路径
     * @param fileName 文件名
     * @return
     */
    public File getFullPathByUid(String root, String fileName) {
        File baseDir = new File(root);// 创建一个File对象
        // 判断目录是否存在
        if (!baseDir.exists() || !baseDir.isDirectory()) {
            return null;
        }
        //判断目录是否存在
        File[] files = baseDir.listFiles();
        if (files != null) {
            for (File tempFile : files) {
                if (tempFile.isDirectory()) {
                    File file = getFullPathByUid(tempFile.getAbsolutePath(), fileName);
                    if (file != null) return file;
                } else if (tempFile.isFile()) {
                    String tempName = tempFile.getName();
                    if (tempName.equals(fileName)) {
                        return tempFile.getAbsoluteFile();
                    }
                }
            }
        }
        return null;
    }
}
