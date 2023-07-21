package com.dr.archive.fuzhou.im.entity;

import java.util.List;

/**
 * 返回参数很多，而且没有文档，不过绝大多数目前用不上
 */
public class WordProBoGroupGetEntity {

    private int status;
    private Data data;
    private String errcode;
    private String errmsg;

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getErrmsg() {
        return errmsg;
    }
    public static class Data {
        private String  gid;
        private String  org_id;
        private String  name;
        private String  description;
        private String  avatar;
        private String  py_first;
        private int  user_limit;
        private int  user_count;
        private int  blocks_count;
        private int  type;
        private String  target_type;
        private String  target_id;
        private String  target_info;
        private int  is_public;
        private int  is_request;
        private int  invite_type;
        private int  is_need_confirm;
        private int  status;
        private String  owner;
        private long  create_time;
        private long  update_time;
        private String  user_ver;
        private String  group_ver;
        private int  is_mute;
        private int  flag;
        private List<PData> members;

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getOrg_id() {
            return org_id;
        }

        public void setOrg_id(String org_id) {
            this.org_id = org_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getPy_first() {
            return py_first;
        }

        public void setPy_first(String py_first) {
            this.py_first = py_first;
        }

        public int getUser_limit() {
            return user_limit;
        }

        public void setUser_limit(int user_limit) {
            this.user_limit = user_limit;
        }

        public int getUser_count() {
            return user_count;
        }

        public void setUser_count(int user_count) {
            this.user_count = user_count;
        }

        public int getBlocks_count() {
            return blocks_count;
        }

        public void setBlocks_count(int blocks_count) {
            this.blocks_count = blocks_count;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTarget_type() {
            return target_type;
        }

        public void setTarget_type(String target_type) {
            this.target_type = target_type;
        }

        public String getTarget_id() {
            return target_id;
        }

        public void setTarget_id(String target_id) {
            this.target_id = target_id;
        }

        public String getTarget_info() {
            return target_info;
        }

        public void setTarget_info(String target_info) {
            this.target_info = target_info;
        }

        public int getIs_public() {
            return is_public;
        }

        public void setIs_public(int is_public) {
            this.is_public = is_public;
        }

        public int getIs_request() {
            return is_request;
        }

        public void setIs_request(int is_request) {
            this.is_request = is_request;
        }

        public int getInvite_type() {
            return invite_type;
        }

        public void setInvite_type(int invite_type) {
            this.invite_type = invite_type;
        }

        public int getIs_need_confirm() {
            return is_need_confirm;
        }

        public void setIs_need_confirm(int is_need_confirm) {
            this.is_need_confirm = is_need_confirm;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

        public long getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(long update_time) {
            this.update_time = update_time;
        }

        public String getUser_ver() {
            return user_ver;
        }

        public void setUser_ver(String user_ver) {
            this.user_ver = user_ver;
        }

        public String getGroup_ver() {
            return group_ver;
        }

        public void setGroup_ver(String group_ver) {
            this.group_ver = group_ver;
        }

        public int getIs_mute() {
            return is_mute;
        }

        public void setIs_mute(int is_mute) {
            this.is_mute = is_mute;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public List<PData> getMembers() {
            return members;
        }

        public void setMembers(List<PData> members) {
            this.members = members;
        }

        private static class PData{
            private String  gid;
            private String  account;
            private String  name;
            private String  nickname;
            private String  py_first;
            private int  mute_expire;
            private String  avatar;
            private int  role;

            public String getGid() {
                return gid;
            }

            public void setGid(String gid) {
                this.gid = gid;
            }

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getPy_first() {
                return py_first;
            }

            public void setPy_first(String py_first) {
                this.py_first = py_first;
            }

            public int getMute_expire() {
                return mute_expire;
            }

            public void setMute_expire(int mute_expire) {
                this.mute_expire = mute_expire;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public int getRole() {
                return role;
            }

            public void setRole(int role) {
                this.role = role;
            }
        }
    }
}
