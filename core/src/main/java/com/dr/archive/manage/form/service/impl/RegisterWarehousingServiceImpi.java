package com.dr.archive.manage.form.service.impl;

import com.dr.archive.manage.form.entity.RegisterWarehousing;
import com.dr.archive.manage.form.entity.RegisterWarehousingDetails;
import com.dr.archive.manage.form.service.RegisterWarehousingService;
import com.dr.archive.util.DateTimeUtils;
import com.dr.archive.util.RegisterPDFUtil;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.security.SecurityHolder;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RegisterWarehousingServiceImpi extends DefaultBaseService<RegisterWarehousing> implements RegisterWarehousingService  {


    @Autowired
    CommonMapper commonMapper;

    @Autowired
    CommonService commonService;

    /*根据输入的list 作为表头*/

    /**
     * Excel文件输出路径
     */
    private String filePath = "core\\src\\main\\resources\\PDF\\";

    private String PDF_Template = "core\\src\\main\\resources\\PDF_Template\\模板.pdf";


    @Override
    public RegisterWarehousing insertReg(String status) {
        if (status.toUpperCase().equals("MANAGE")||status.toUpperCase().equals("RECEIVE")){
            Person currentPerson = SecurityHolder.get().currentPerson();
            //移交一次的批次
            //生成id给详情信息。
            RegisterWarehousing registerWarehousing = new RegisterWarehousing(UUIDUtils.getUUID(),currentPerson.getId(),System.currentTimeMillis(),System.currentTimeMillis(),currentPerson.getUserName());
            String name = "";
            if (status.equals("MANAGE")){
                name = "正式库";
                registerWarehousing.setReason("移动至正式库");
                registerWarehousing.setStatus("1");
                registerWarehousing.setRecord_name(currentPerson.getUserName()+"提交到"+name);
            }
            else if (status.equals("RECEIVE")){
                name = "临时库";
                registerWarehousing.setReason("退回至临时库");
                registerWarehousing.setStatus("0");
                registerWarehousing.setRecord_name(currentPerson.getUserName()+"退回到"+name);
            }
            commonMapper.insert(registerWarehousing);
            return registerWarehousing;
        }
        return null;

    }

    @Override
    public String processInformation(RegisterWarehousing registerWarehousing, List<RegisterWarehousingDetails> registerWarehousingDetails) throws IOException {

        String url = filePath+DateTimeUtils.getMillis()+".pdf";
        deleteFile(new File(filePath));
        List<String[]> strings = new ArrayList<>();
        for (RegisterWarehousingDetails detail:registerWarehousingDetails){
            String[] str = new String[]{detail.getTIMING(),detail.getArchiveCode(), detail.getND(), detail.getWJXCRQ(), detail.getBGQX()};
            strings.add(str);
        }
        try {
            RegisterPDFUtil.pdfZDY(registerWarehousing,strings,url);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return url;
    }

    public  Boolean deleteFile(File file) {
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()) {
            return false;
        }
        //获取目录下子文件
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f : files) {
            //判断子目录是否存在子目录,如果是文件则删除
            if (f.isDirectory()) {
                //递归删除目录下的文件
                deleteFile(f);
            } else {
                //文件删除
                f.delete();
                //打印文件名
                System.out.println("文件名：" + f.getName());
            }
        }
        return true;
    }

}


