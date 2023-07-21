package com.dr.archive.fuzhou.im.bo;

import java.util.List;
import java.util.Map;

/**
 * 发送消息(formview)
 */
public class WordProBoSend {

    private List<Recver> recver;

    private String ctype;

    private String content;

    private Refer refer;

    private String flag;

    private ExtData extdata;

    public ExtData getExtdata() {
        return extdata;
    }

    public void setExtdata(ExtData extdata) {
        this.extdata = extdata;
    }

    public static class ExtData{

        private List<Map<String , String>> header;
        //private List<Map<String , String>> body;
        private Map<String , String> footer;
        private Map<String , String> target;

        public List<Map<String, String>> getHeader() {
            return header;
        }

        public void setHeader(List<Map<String, String>> header) {
            this.header = header;
        }

//        public List<Map<String, String>> getBody() {
//            return body;
//        }

//        public void setBody(List<Map<String, String>> body) {
//            this.body = body;
//        }

        public Map<String, String> getFooter() {
            return footer;
        }

        public void setFooter(Map<String, String> footer) {
            this.footer = footer;
        }

        public Map<String, String> getTarget() {
            return target;
        }

        public void setTarget(Map<String, String> target) {
            this.target = target;
        }

    }

    public static class Refer{
        private String id;

        private String name;

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

    public static class Recver{
        private String id;
        private String name;

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

    public Refer getRefer() {
        return refer;
    }

    public void setRefer(Refer refer) {
        this.refer = refer;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }



    public List<Recver> getRecver() {
        return recver;
    }

    public void setRecver(List<Recver> recver) {
        this.recver = recver;
    }

    public String getCtype() {
        return ctype;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
