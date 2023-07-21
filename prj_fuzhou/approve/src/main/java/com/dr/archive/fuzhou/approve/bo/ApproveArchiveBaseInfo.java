package com.dr.archive.fuzhou.approve.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author caor
 * @date 2021-08-28 13:11
 * 基本信息元数据
 */
@JacksonXmlRootElement(localName = "base_info")
public class ApproveArchiveBaseInfo {
    /**
     * 收件
     */
    public static final String PROCESS_CODE_SJ = "1";
    /**
     * 受理
     */
    public static final String PROCESS_CODE_SL = "2";
    /**
     * 审核
     */
    public static final String PROCESS_CODE_SH = "3";
    /**
     * 办结
     */
    public static final String PROCESS_CODE_BJ = "4";
    /**
     * 补齐补正
     */
    public static final String PROCESS_CODE_BQ = "11";
    /**
     * 挂起
     */
    public static final String PROCESS_CODE_GQ = "12";
    /**
     * 其他
     */
    public static final String PROCESS_CODE_QT = "99";

    //电子文件号
    @JacksonXmlProperty(localName = "electronic_record_code")
    private String electronicRecordCode;
    //行政区划编码
    @JacksonXmlProperty(localName = "region_code")
    private String regionCode;
    //事项编码
    @JacksonXmlProperty(localName = "task_code")
    private String taskCode;
    //信息系统描述
    @JacksonXmlProperty(localName = "information_system_dcscription")
    private String informationSystemDcscription;
    //档案馆代码
    @JacksonXmlProperty(localName = "archives_identifier")
    private String archivesIdentifier;
    //档案馆名称
    @JacksonXmlProperty(localName = "archives_name")
    private String archivesName;
    //部门名称（立档单位名称）
    @JacksonXmlProperty(localName = "fonds_constituting_unit_name")
    private String departmentName;
    //责任处（科）室
    @JacksonXmlProperty(localName = "lead_department")
    private String leadDepartment;
    //全宗号
    @JacksonXmlProperty(localName = "fonds_identifier")
    private String fondsIdentifier;
    //统一社会信用代码
    @JacksonXmlProperty(localName = "social_code")
    private String socialCode;
    //保管期限
    @JacksonXmlProperty(localName = "retention_period")
    private String retentionPeriod;
    //密级
    @JacksonXmlProperty(localName = "security_classification")
    private String securityClassification;
    //业务流水号
    @JacksonXmlProperty(localName = "project_id")
    private String projectId;
    //事项名称
    @JacksonXmlProperty(localName = "task_name")
    private String taskName;
    //事项版本号
    @JacksonXmlProperty(localName = "task_version")
    private String taskVersion;
    //事项类型
    @JacksonXmlProperty(localName = "task_type")
    private String taskType;
    //办件名称
    @JacksonXmlProperty(localName = "project_name")
    private String projectName;
    //办件类型
    @JacksonXmlProperty(localName = "service_type")
    private String serviceType;
    //归档时间
    @JacksonXmlProperty(localName = "archive_time")
    private String archiveTime;
    //行政相对人名称
    @JacksonXmlProperty(localName = "apply_name")
    private String applyName;
    //行政相对人类型
    @JacksonXmlProperty(localName = "apply_type")
    private String applyType;
    //行政相对人证件类型
    @JacksonXmlProperty(localName = "card_type")
    private String cardType;
    //行政相对人证件号码
    @JacksonXmlProperty(localName = "contacted_card")
    private String contactedCard;
    //行政相对人手机
    //private String mobile;
    //行政相对人电话
    //private String phone;
    //行政相对人地址
    @JacksonXmlProperty(localName = "address")
    private String address;
    //法定代表人
    @JacksonXmlProperty(localName = "legal_man")
    private String legalMan;
    //法定代表人证件类型
    @JacksonXmlProperty(localName = "legal_man_id_type")
    private String legalManIdType;
    //法定代表人证件号码
    @JacksonXmlProperty(localName = "legal_man_id_number")
    private String legalManIdNumber;
    //是否属于项目类业务
    @JacksonXmlProperty(localName = "is_project")
    private String isProject;
    //项目代码
    @JacksonXmlProperty(localName = "project_code")
    private String projectCode;
    //项目名称
    @JacksonXmlProperty(localName = "project_title")
    private String projectTitle;
    //项目地址
    @JacksonXmlProperty(localName = "project_address")
    private String projectAddress;

    //受理（立案）单位
    @JacksonXmlProperty(localName = "receive_department")
    private String receiveDepartment;
    //受理（立案）时间
    @JacksonXmlProperty(localName = "receive_time")
    private String receiveTime;
    //办结时间
    @JacksonXmlProperty(localName = "transact_time")
    private String transactTime;
    //办理结果
    @JacksonXmlProperty(localName = "result")
    private String result;
    //结果证照编号
    @JacksonXmlProperty(localName = "result_code")
    private String resultCode;
    //受理编号
    @JacksonXmlProperty(localName = "accept_num")
    private String acceptNum;
    //关联业务受理编号
    //private String relationnum;
    //归档信息包内总件数
    @JacksonXmlProperty(localName = "total_number")
    private String totalNumber;
    //备注
    @JacksonXmlProperty(localName = "remarks")
    private String remarks;
    //归档文件集
//    private List<Folder> fileset = new ArrayList<>();
    private List<List<File>> fileset = new ArrayList<>();

    public List<List<File>> getFileset() {
        return fileset;
    }

    public void setFileset(List<List<File>> fileset) {
        this.fileset = fileset;
    }

    /**
     * 根据元数据获取所有实际文件名称
     *
     * @return
     */
    public Set<String> getAllFileNames() {
        if (fileset == null) {
            return Collections.emptySet();
        } else {
            Set<String> fileNames = new HashSet<>();
            for (List<File> fileList : fileset) {

                for (File file : fileList) {
                    String folderName = "申报文件";
                    if (!StringUtils.isEmpty(file.getFilecode()) && file.getFilecode().contains("B")) {
                        folderName = "过程文件";
                    } else if (!StringUtils.isEmpty(file.getFilecode()) && file.getFilecode().contains("C")) {
                        folderName = "结果文件";
                    }
                    fileNames.add(folderName + "/" + file.fileactualname);
                }
            }
            return fileNames;
        }
    }

    /**
     * 获取材料清单标准文件名称
     *
     * @return
     */
    public Set<String> getStdFiles() {
        if (fileset == null) {
            return Collections.emptySet();
        } else {
            Set<String> fileNames = new HashSet<>();
            for (List<File> fileList : fileset) {
                for (File file : fileList) {
                    String folderName = "申报文件";
                    if (!StringUtils.isEmpty(file.getFilecode()) && file.getFilecode().contains("B")) {
                        folderName = "过程文件";
                    } else if (!StringUtils.isEmpty(file.getFilecode()) && file.getFilecode().contains("C")) {
                        folderName = "结果文件";
                    }
                    fileNames.add(folderName + "/" + file.filestandardname);
                }
            }
            return fileNames;
        }
    }

    /**
     * 获取文件类型计数
     * <p>
     * key是材料类型  value是数量
     *
     * @return
     */
    public Map<String, Integer> getMaterialCountMap() {
        Map<String, Integer> map = new HashMap<>();
        for (List<File> fileList : fileset) {
            for (File file : fileList) {
                Integer count = map.get(file.filecode);
                if (count == null) {
                    count = 0;
                }
                map.put(file.filecode, count++);
            }
        }
        return map;
    }

//    @JacksonXmlRootElement(localName = "folder")
//    public static class Folder {
//        @JacksonXmlProperty(localName = "file")
//        @JacksonXmlElementWrapper(useWrapping = false)
//        private List<File> fileList = new ArrayList<>();
//        @JacksonXmlProperty(isAttribute = true)
//        private String id;
//        @JacksonXmlProperty(isAttribute = true)
//        private String title;
//
//
//        public List<File> getFileList() {
//            return fileList;
//        }
//
//        public void setFileList(List<File> fileList) {
//            this.fileList = fileList;
//        }
//
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public void setTitle(String title) {
//            this.title = title;
//        }
//    }

    @JacksonXmlRootElement(localName = "file")
    public static class File {
        @JacksonXmlProperty(isAttribute = true)
        private String id;
        //归档文件标准名称
        private String filestandardname;
        //归档文件实际名称
        private String fileactualname;
        //归档文件类型
        private String filecode;
        //归档文件状态
        private String filestate;
        //归档文件共享类型
        private String fileshare;
        //归档文件共享条件
        @JsonIgnore
        private List<String> fileshareconditions;
        //不予共享具体依据
        private String filenosharebeacuse;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFilestandardname() {
            return filestandardname;
        }

        public void setFilestandardname(String filestandardname) {
            this.filestandardname = filestandardname;
        }

        public String getFileactualname() {
            return fileactualname;
        }

        public void setFileactualname(String fileactualname) {
            this.fileactualname = fileactualname;
        }

        public String getFilecode() {
            return filecode;
        }

        public void setFilecode(String filecode) {
            this.filecode = filecode;
        }

        public String getFilestate() {
            return filestate;
        }

        public void setFilestate(String filestate) {
            this.filestate = filestate;
        }

        public String getFileshare() {
            return fileshare;
        }

        public void setFileshare(String fileshare) {
            this.fileshare = fileshare;
        }

        public List<String> getFileshareconditions() {
            return fileshareconditions;
        }

        public void setFileshareconditions(List<String> fileshareconditions) {
            this.fileshareconditions = fileshareconditions;
        }

        public String getFilenosharebeacuse() {
            return filenosharebeacuse;
        }

        public void setFilenosharebeacuse(String filenosharebeacuse) {
            this.filenosharebeacuse = filenosharebeacuse;
        }

        String getFileTitle() {
            //TODO 参照接口文档
            if ("B0".equals(filecode)) {
                return "审批流程信息";
            } else if ("B1".equals(filecode)) {
                return "收件材料接收凭证";
            } else if ("B2".equals(filecode)) {
                return "即审即办信用承诺书";
            } else if ("B3".equals(filecode)) {
                return "备案未抽中结果告知书";
            } else if ("B4".equals(filecode)) {
                return "不予受理通知书";
            } else if ("B5".equals(filecode)) {
                return "特殊程序告知书";
            } else if ("B6".equals(filecode)) {
                return "容缺受理信用承诺书";
            } else {
                return "暂未对材料进行分类";
            }
        }
    }

    public String getElectronicRecordCode() {
        return electronicRecordCode;
    }

    public void setElectronicRecordCode(String electronicRecordCode) {
        this.electronicRecordCode = electronicRecordCode;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getInformationSystemDcscription() {
        return informationSystemDcscription;
    }

    public void setInformationSystemDcscription(String informationSystemDcscription) {
        this.informationSystemDcscription = informationSystemDcscription;
    }

    public String getArchivesIdentifier() {
        return archivesIdentifier;
    }

    public void setArchivesIdentifier(String archivesIdentifier) {
        this.archivesIdentifier = archivesIdentifier;
    }

    public String getArchivesName() {
        return archivesName;
    }

    public void setArchivesName(String archivesName) {
        this.archivesName = archivesName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getLeadDepartment() {
        return leadDepartment;
    }

    public void setLeadDepartment(String leadDepartment) {
        this.leadDepartment = leadDepartment;
    }

    public String getFondsIdentifier() {
        return fondsIdentifier;
    }

    public void setFondsIdentifier(String fondsIdentifier) {
        this.fondsIdentifier = fondsIdentifier;
    }

    public String getSocialCode() {
        return socialCode;
    }

    public void setSocialCode(String socialCode) {
        this.socialCode = socialCode;
    }

    public String getRetentionPeriod() {
        return retentionPeriod;
    }

    public void setRetentionPeriod(String retentionPeriod) {
        this.retentionPeriod = retentionPeriod;
    }

    public String getSecurityClassification() {
        return securityClassification;
    }

    public void setSecurityClassification(String securityClassification) {
        this.securityClassification = securityClassification;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskVersion() {
        return taskVersion;
    }

    public void setTaskVersion(String taskVersion) {
        this.taskVersion = taskVersion;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getArchiveTime() {
        return archiveTime;
    }

    public void setArchiveTime(String archiveTime) {
        this.archiveTime = archiveTime;
    }

    public String getApplyName() {
        return applyName;
    }

    public void setApplyName(String applyName) {
        this.applyName = applyName;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getContactedCard() {
        return contactedCard;
    }

    public void setContactedCard(String contactedCard) {
        this.contactedCard = contactedCard;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLegalMan() {
        return legalMan;
    }

    public void setLegalMan(String legalMan) {
        this.legalMan = legalMan;
    }

    public String getLegalManIdType() {
        return legalManIdType;
    }

    public void setLegalManIdType(String legalManIdType) {
        this.legalManIdType = legalManIdType;
    }

    public String getLegalManIdNumber() {
        return legalManIdNumber;
    }

    public void setLegalManIdNumber(String legalManIdNumber) {
        this.legalManIdNumber = legalManIdNumber;
    }

    public String getIsProject() {
        return isProject;
    }

    public void setIsProject(String isProject) {
        this.isProject = isProject;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getProjectAddress() {
        return projectAddress;
    }

    public void setProjectAddress(String projectAddress) {
        this.projectAddress = projectAddress;
    }

    public String getReceiveDepartment() {
        return receiveDepartment;
    }

    public void setReceiveDepartment(String receiveDepartment) {
        this.receiveDepartment = receiveDepartment;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getTransactTime() {
        return transactTime;
    }

    public void setTransactTime(String transactTime) {
        this.transactTime = transactTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getAcceptNum() {
        return acceptNum;
    }

    public void setAcceptNum(String acceptNum) {
        this.acceptNum = acceptNum;
    }

    public String getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(String totalNumber) {
        this.totalNumber = totalNumber;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
