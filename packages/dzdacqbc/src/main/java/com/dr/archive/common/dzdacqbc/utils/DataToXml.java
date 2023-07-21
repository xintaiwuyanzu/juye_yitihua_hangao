package com.dr.archive.common.dzdacqbc.utils;

import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.util.ZipUtil;
import com.dr.framework.common.form.core.entity.FormField;
import com.dr.framework.common.form.core.model.FormData;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.List;

public class DataToXml {
    /**
     *
     * @param formData
     * @param formFields
     * @param odfPath 原文所在路径
     * @param targetPath 存储路径
     */

    public static void packetXml(FormData formData, List<FormField> formFields, String odfPath, String targetPath, String finalPath) {
        try {
            // 1、生成一个根节点  base_info title="基本信息元数据"
            Element rss = new Element("base_info");
            // 2、为节点添加属性
            rss.setAttribute("title", "基本信息元数据");
            // 3、生成一个document对象
            Document document = new Document(rss);
            for (FormField formField : formFields) {
                if (!StringUtils.isEmpty(formField.getFieldAliasStr())) {
                    Element channel = new Element(formField.getFieldAliasStr());
                    channel.setAttribute("title", formField.getLabel());
                    channel.setText(formData.getString(formField.getFieldCode()));
                    rss.addContent(channel);
                } else {
                    Element channel = new Element(formField.getFieldCode());
                    channel.setAttribute("title", formField.getLabel());
                    channel.setText(formData.getString(formField.getFieldCode()));
                    rss.addContent(channel);
                }
            }
            //4、获取存储路径

            //5、获取原文

            File file = new File(odfPath);
            //置空fileLists用于获取文件夹下面的所有OFD文件
//            fileLists = new ArrayList<>();
            Integer count = 1;
            if (!StringUtils.isEmpty(file.listFiles())) {
                Element fileset = new Element("fileset");
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    File listFile = files[i];

                    if (!listFile.getName().contains(".DS_Store")) {
                        if (!listFile.getName().contains(".ofd")) {

                            File targetFile = new File(targetPath);
                            if (!targetFile.exists()) {
                                targetFile.mkdirs();
                            }
                            OutputStream os = new FileOutputStream(targetPath + File.separator + listFile.getName());
                            InputStream is = new FileInputStream(files[i].getPath());
                            FileCopyUtils.copy(is, os);

                            Element file1 = new Element("file");
                            file1.setAttribute("id", getFileNewName(i + 1));

                            Element file_code = new Element("file_code");
                            file_code.setAttribute("title", "归档文件类型");
                            file_code.setText("A");

                            Element file_standard_name = new Element("file_standard_name");
                            file_standard_name.setAttribute("title", "归档文件目录名称");
                            file_standard_name.setText(formData.getString(ArchiveEntity.COLUMN_TITLE));

                            Element file_actual_name = new Element("file_actual_name");
                            file_actual_name.setAttribute("title", "归档文件名称");
                            file_actual_name.setText(files[i].getName());

                            Element format_information = new Element("format_information");
                            format_information.setAttribute("title", "归档文件格式");
                            format_information.setText(files[i].getName().substring(files[i].getName().lastIndexOf(".") + 1));

                            Element computer_file_name = new Element("computer_file_name");
                            computer_file_name.setAttribute("title", "归档文件计算机文件名");
                            computer_file_name.setText(files[i].getName().substring(0, files[i].getName().lastIndexOf(".")));

                            Element computer_file_size = new Element("computer_file_size");
                            computer_file_size.setAttribute("title", "归档文件计算机文件大小");
                            computer_file_size.setText(files[i].length() + "");

                            Element computer_file_creation_time = new Element("computer_file_creation_time");
                            computer_file_creation_time.setAttribute("title", "归档文件计算机形成时间");
                            computer_file_creation_time.setText(formData.getString(ArchiveEntity.COLUMN_FILETIME));

                            file1.addContent(file_code);
                            file1.addContent(file_standard_name);
                            file1.addContent(file_actual_name);
                            file1.addContent(format_information);
                            file1.addContent(computer_file_name);
                            file1.addContent(computer_file_size);
                            file1.addContent(computer_file_creation_time);


//                                }
                            count++;
                            fileset.addContent(file1);

                        } else {
                            File targetFile = new File(targetPath);
                            if (!targetFile.exists()) {
                                targetFile.mkdirs();
                            }
                            OutputStream os = new FileOutputStream(targetPath + File.separator + listFile.getName());
                            InputStream is = new FileInputStream(listFile.getPath());
                            FileCopyUtils.copy(is, os);

                            Element file1 = new Element("file");
                            file1.setAttribute("id", getFileNewName(1));

                            Element file_code = new Element("file_code");
                            file_code.setAttribute("title", "归档文件类型");
                            file_code.setText("A");

                            Element file_standard_name = new Element("file_standard_name");
                            file_standard_name.setAttribute("title", "归档文件目录名称");
                            file_standard_name.setText(formData.getString(ArchiveEntity.COLUMN_TITLE));

                            Element file_actual_name = new Element("file_actual_name");
                            file_actual_name.setAttribute("title", "归档文件名称");
                            file_actual_name.setText(listFile.getName());

                            Element format_information = new Element("format_information");
                            format_information.setAttribute("title", "归档文件格式");
                            format_information.setText(listFile.getName().substring(listFile.getName().lastIndexOf(".") + 1));

                            Element computer_file_name = new Element("computer_file_name");
                            computer_file_name.setAttribute("title", "归档文件计算机文件名");
                            computer_file_name.setText(listFile.getName().substring(0, listFile.getName().lastIndexOf(".")));

                            Element computer_file_size = new Element("computer_file_size");
                            computer_file_size.setAttribute("title", "归档文件计算机文件大小");
                            computer_file_size.setText(listFile.length() + "");

                            Element computer_file_creation_time = new Element("computer_file_creation_time");
                            computer_file_creation_time.setAttribute("title", "归档文件计算机形成时间");
                            computer_file_creation_time.setText(formData.getString(ArchiveEntity.COLUMN_FILETIME));

                            file1.addContent(file_code);
                            file1.addContent(file_standard_name);
                            file1.addContent(file_actual_name);
                            file1.addContent(format_information);
                            file1.addContent(computer_file_name);
                            file1.addContent(computer_file_size);
                            file1.addContent(computer_file_creation_time);

                            fileset.addContent(file1);
                        }
                    }
                }
                rss.addContent(fileset);
            }

            Format format = Format.getCompactFormat();
            // 设置换行Tab或空格
            format.setIndent("	");
            format.setEncoding("UTF-8");

            // 6、创建XMLOutputter的对象
            XMLOutputter outputer = new XMLOutputter(format);
            String property = System.getProperty("user.dir");
            // 7、利用outputer将document转换成xml文档
            File xmlFile = new File(finalPath + File.separator + "基本信息元数据.xml");
            if (!xmlFile.exists()) {
                //文件可能不存在需要创建
                xmlFile.getParentFile().mkdir();
            }
            OutputStream outputStream = new FileOutputStream(xmlFile);
            outputer.output(document, outputStream);
            outputStream.close();
            System.out.println("打包成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("打包失败");
        }
    }

    public static String getFileNewName(Integer count) {
        String str = "";
        String title = String.valueOf(count);
        for (int i = title.length(); i < 4; i++) {
            str = "0" + title;
        }
        return str;
    }
}
