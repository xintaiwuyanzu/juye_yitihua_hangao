package com.dr.archive.fuzhou.im.entity;

import java.util.List;

/**
 * 群漫游
 */
public class WordProBoGroupMsgEntity {

    private int status;
    private Data data;
    private String errcode;
    private String errmsg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }


    public  static class Data{

        private int page;

        private int page_size;

        private int count;

        private List<Datas> list;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getPage_size() {
            return page_size;
        }

        public void setPage_size(int page_size) {
            this.page_size = page_size;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<Datas> getList() {
            return list;
        }

        public void setList(List<Datas> list) {
            this.list = list;
        }
    }
    public static class Datas{
        private String  groupid;
        private int num;

        private String type;
        private String subject;
        private String flag;

        private String  gid;
        private String sendid;
        private String sendplatform;
        private String sendname;
        private String senddate;
        private String sendavatar;
        private String text;
        private Object content;
        private String extdata;
        private String status;

        private List file;

        private String id;

        public String getGroupid() {
            return groupid;
        }

        public void setGroupid(String groupid) {
            this.groupid = groupid;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getSendid() {
            return sendid;
        }

        public void setSendid(String sendid) {
            this.sendid = sendid;
        }

        public String getSendplatform() {
            return sendplatform;
        }

        public void setSendplatform(String sendplatform) {
            this.sendplatform = sendplatform;
        }

        public String getSendname() {
            return sendname;
        }

        public void setSendname(String sendname) {
            this.sendname = sendname;
        }

        public String getSenddate() {
            return senddate;
        }

        public void setSenddate(String senddate) {
            this.senddate = senddate;
        }

        public String getSendavatar() {
            return sendavatar;
        }

        public void setSendavatar(String sendavatar) {
            this.sendavatar = sendavatar;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Object getContent() {
            return content;
        }

        public void setContent(Object content) {
            this.content = content;
        }

        public String getExtdata() {
            return extdata;
        }

        public void setExtdata(String extdata) {
            this.extdata = extdata;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List getFile() {
            return file;
        }

        public void setFile(List file) {
            this.file = file;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
