package com.dr.archive.fuzhou.approve.bo;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: qiuyf
 * 归档 文件集元数据
 */
@JacksonXmlRootElement(localName = "base_info")
public class BaseInfoFiles {
    @JacksonXmlProperty(localName = "folder")
    @JacksonXmlElementWrapper(localName = "fileset")
    private List<BaseInfoFolder> baseInfoFolders = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        XmlMapper mapper = new XmlMapper();
        BaseInfoFiles baseInfoFiles = new BaseInfoFiles();
        BaseInfoFiles.BaseInfoFolder baseInfoFolder = new BaseInfoFiles.BaseInfoFolder();
        BaseInfoFiles.BaseInfoFolder.BaseFile baseFile = new BaseInfoFiles.BaseInfoFolder.BaseFile();
        baseInfoFiles.getBaseInfoFolders().add(baseInfoFolder);
        baseInfoFiles.getBaseInfoFolders().get(0).getBaseFiles().add(baseFile);
        String xml = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(baseInfoFiles);
        System.out.println(xml);
    }

    @JacksonXmlRootElement(localName = "folder")
    public static class BaseInfoFolder{

        @JacksonXmlProperty(localName = "file")
        private List<BaseFile> baseFiles = new ArrayList<>();
        @JacksonXmlProperty(isAttribute = true, localName = "id")
        private String id;
        @JacksonXmlProperty(isAttribute = true, localName = "title")
        private String name;

        @JacksonXmlRootElement(localName = "file")
        public static class BaseFile {
            @JacksonXmlProperty(localName = "file_code")
            private String fileCode;
            @JacksonXmlProperty(localName = "file_standard_name")
            private String fileStandardName;
            @JacksonXmlProperty(localName = "file_actual_name")
            private String fileActualName;
            @JacksonXmlProperty(localName = "format_information")
            private String formatInformation;
            @JacksonXmlProperty(localName = "computer_file_name")
            private String computerFileName;
            @JacksonXmlProperty(localName = "computer_file_size")
            private String computerFileSize;
            @JacksonXmlProperty(localName = "computer_file_creation_time")
            private String computerFileCreationTime;
            @JacksonXmlProperty(localName = "auxiliary")
            private String auxiliary;
            @JacksonXmlProperty(localName = "file_share")
            private String fileShare;
            @JacksonXmlProperty(localName = "file_share_conditions")
            private String fileShareConditions;

            public String getFileCode() {
                return fileCode;
            }

            public void setFileCode(String fileCode) {
                this.fileCode = fileCode;
            }

            public String getFileStandardName() {
                return fileStandardName;
            }

            public void setFileStandardName(String fileStandardName) {
                this.fileStandardName = fileStandardName;
            }

            public String getFileActualName() {
                return fileActualName;
            }

            public void setFileActualName(String fileActualName) {
                this.fileActualName = fileActualName;
            }

            public String getFormatInformation() {
                return formatInformation;
            }

            public void setFormatInformation(String formatInformation) {
                this.formatInformation = formatInformation;
            }

            public String getComputerFileName() {
                return computerFileName;
            }

            public void setComputerFileName(String computerFileName) {
                this.computerFileName = computerFileName;
            }

            public String getComputerFileSize() {
                return computerFileSize;
            }

            public void setComputerFileSize(String computerFileSize) {
                this.computerFileSize = computerFileSize;
            }

            public String getComputerFileCreationTime() {
                return computerFileCreationTime;
            }

            public void setComputerFileCreationTime(String computerFileCreationTime) {
                this.computerFileCreationTime = computerFileCreationTime;
            }

            public String getAuxiliary() {
                return auxiliary;
            }

            public void setAuxiliary(String auxiliary) {
                this.auxiliary = auxiliary;
            }

            public String getFileShare() {
                return fileShare;
            }

            public void setFileShare(String fileShare) {
                this.fileShare = fileShare;
            }

            public String getFileShareConditions() {
                return fileShareConditions;
            }

            public void setFileShareConditions(String fileShareConditions) {
                this.fileShareConditions = fileShareConditions;
            }
        }

        public List<BaseFile> getBaseFiles() {
            return baseFiles;
        }

        public void setBaseFiles(List<BaseFile> baseFiles) {
            this.baseFiles = baseFiles;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public List<BaseInfoFolder> getBaseInfoFolders() {
        return baseInfoFolders;
    }

    public void setBaseInfoFolders(List<BaseInfoFolder> baseInfoFolders) {
        this.baseInfoFolders = baseInfoFolders;
    }
}
