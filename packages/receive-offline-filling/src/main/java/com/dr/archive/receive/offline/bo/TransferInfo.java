
package com.dr.archive.receive.offline.bo;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 归档（移交）目录元数据
 * 这个是xml
 * <p>
 * <p>
 * 移交目录	必选	不可重复	容器型	——	由行政区划编码、全宗号和批次号组成
 *
 * @author dr
 */
@JacksonXmlRootElement(localName = "transferinfo")
public class TransferInfo implements Serializable {
    //统一社会信用代码
    private String socialcode;
    //保管期限
    private String retentionperiod;
    //业务流水号
    private String projectid;
    //题名
    private String title;
    //档号
    private String archiveCode;
    //全宗编码
    private String fondCode;
     //目录号
    private String catalogueCode;
    //分类编码列
    private String categoryCode;
    //机构或问题编码
    private String orgCode;
    //数据来源类型
    private String sourceType;
    //数据来源Id
    private String sourceId;
    //主题词
    private String keyWords;
    //备注
    private String note;
    //年度
    private String vintages;
    //文件时间
    private String fileTime;
    //保管期限
    private String saveTerm;
    //密级
    private String securityLevel;
    //责任者
    private String dutyPerson;
    //当前状态
    private String status;
    //子状态，
    private String subStatus;
    //开放范围 1：开放 2：不开放
    private String openScope;
    //案卷号
    private String ajh;
    // 案卷档号
    private String ajdh;
    //页号
    private String yh;
    //页数
    private String ys;
    //文件编号(文号)
    private String fileCode;
    //件号
    private String jh;
    //盒号
    private String hh;
    //人名
    private String personName;
    //位置
    private String position;
    //是否有原文信息
    private String ywHave;
    //文件类型
    private String wjlx;
    //文件份数
    private String wjfs;
    //保管期限编号
    private String bgqxbh;
    //门类代码
    private String mldm;
    private String wjhxz;
    private List<Folder> fileset = new ArrayList<>();

    public TransferInfo() {
        Folder folder = new Folder();
        fileset.add(folder);
    }

    public static void main(String[] args) throws IOException {
        XmlMapper mapper = new XmlMapper();
        mapper.writeValue(System.out, new TransferInfo());
    }

    public String getFondCode() {
        return fondCode;
    }

    public void setFondCode(String fondCode) {
        this.fondCode = fondCode;
    }

    public String getCatalogueCode() {
        return catalogueCode;
    }

    public void setCatalogueCode(String catalogueCode) {
        this.catalogueCode = catalogueCode;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getVintages() {
        return vintages;
    }

    public void setVintages(String vintages) {
        this.vintages = vintages;
    }

    public String getFileTime() {
        return fileTime;
    }

    public void setFileTime(String fileTime) {
        this.fileTime = fileTime;
    }

    public String getSaveTerm() {
        return saveTerm;
    }

    public void setSaveTerm(String saveTerm) {
        this.saveTerm = saveTerm;
    }

    public String getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(String securityLevel) {
        this.securityLevel = securityLevel;
    }

    public String getDutyPerson() {
        return dutyPerson;
    }

    public void setDutyPerson(String dutyPerson) {
        this.dutyPerson = dutyPerson;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(String subStatus) {
        this.subStatus = subStatus;
    }

    public String getOpenScope() {
        return openScope;
    }

    public void setOpenScope(String openScope) {
        this.openScope = openScope;
    }

    public String getAjh() {
        return ajh;
    }

    public void setAjh(String ajh) {
        this.ajh = ajh;
    }

    public String getAjdh() {
        return ajdh;
    }

    public void setAjdh(String ajdh) {
        this.ajdh = ajdh;
    }

    public String getYh() {
        return yh;
    }

    public void setYh(String yh) {
        this.yh = yh;
    }

    public String getYs() {
        return ys;
    }

    public void setYs(String ys) {
        this.ys = ys;
    }

    public String getFileCode() {
        return fileCode;
    }

    public void setFileCode(String fileCode) {
        this.fileCode = fileCode;
    }

    public String getJh() {
        return jh;
    }

    public void setJh(String jh) {
        this.jh = jh;
    }

    public String getHh() {
        return hh;
    }

    public void setHh(String hh) {
        this.hh = hh;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getYwHave() {
        return ywHave;
    }

    public void setYwHave(String ywHave) {
        this.ywHave = ywHave;
    }

    public String getWjlx() {
        return wjlx;
    }

    public void setWjlx(String wjlx) {
        this.wjlx = wjlx;
    }

    public String getWjfs() {
        return wjfs;
    }

    public void setWjfs(String wjfs) {
        this.wjfs = wjfs;
    }

    public String getBgqxbh() {
        return bgqxbh;
    }

    public void setBgqxbh(String bgqxbh) {
        this.bgqxbh = bgqxbh;
    }

    public String getMldm() {
        return mldm;
    }

    public void setMldm(String mldm) {
        this.mldm = mldm;
    }

    public String getWjhxz() {
        return wjhxz;
    }

    public void setWjhxz(String wjhxz) {
        this.wjhxz = wjhxz;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArchiveCode() {
        return archiveCode;
    }

    public void setArchiveCode(String archiveCode) {
        this.archiveCode = archiveCode;
    }

    public String getSocialcode() {
        return socialcode;
    }

    public void setSocialcode(String socialcode) {
        this.socialcode = socialcode;
    }

    public String getRetentionperiod() {
        return retentionperiod;
    }

    public void setRetentionperiod(String retentionperiod) {
        this.retentionperiod = retentionperiod;
    }

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    public List<Folder> getFileset() {
        return fileset;
    }

    public void setFileset(List<Folder> fileset) {
        this.fileset = fileset;
    }

    @JacksonXmlRootElement(localName = "folder")
    public static class Folder {
        @JacksonXmlProperty(isAttribute = true)
        private String id;

        private List<FileForFolder> filelist = new ArrayList<>();

        public Folder() {
            FileForFolder fileForFolder = new FileForFolder();
            filelist.add(fileForFolder);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<FileForFolder> getFilelist() {
            return filelist;
        }

        public void setFilelist(List<FileForFolder> file) {
            this.filelist = file;
        }

        @JacksonXmlRootElement(localName = "file")
        public static class FileForFolder {
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

            private String filehash;

            public String getFilehash() {
                return filehash;
            }

            public void setFilehash(String filehash) {
                this.filehash = filehash;
            }

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
        }
    }


}
