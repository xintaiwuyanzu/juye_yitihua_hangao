package com.dr.archive.fuzhou.im.entity;

public class WordProGroupResultEntity {
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

    public static class Data {
        private String gid;
        private String name;
        private String type;
        private String py_first;
        private String avatar;
        private String description;
        private String user_limit;
        private String user_count;
        private String owner;
        private String create_time;
        private String group_ver;
        private String user_ver;
        private String base_url;

        public String getBase_url() {
            return base_url;
        }

        public void setBase_url(String base_url) {
            this.base_url = base_url;
        }

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPy_first() {
            return py_first;
        }

        public void setPy_first(String py_first) {
            this.py_first = py_first;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUser_limit() {
            return user_limit;
        }

        public void setUser_limit(String user_limit) {
            this.user_limit = user_limit;
        }

        public String getUser_count() {
            return user_count;
        }

        public void setUser_count(String user_count) {
            this.user_count = user_count;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getGroup_ver() {
            return group_ver;
        }

        public void setGroup_ver(String group_ver) {
            this.group_ver = group_ver;
        }

        public String getUser_ver() {
            return user_ver;
        }

        public void setUser_ver(String user_ver) {
            this.user_ver = user_ver;
        }
    }
}
