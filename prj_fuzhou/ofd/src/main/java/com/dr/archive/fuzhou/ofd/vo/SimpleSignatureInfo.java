package com.dr.archive.fuzhou.ofd.vo;


public class SimpleSignatureInfo   {
  private static final long serialVersionUID = 3053065489126483976L;
  private byte[] signImg;
  private String signer;
  private String dn;
  private String contactInfo;
  private String imgData;

  public byte[] getSignImg() {
    return signImg;
  }

  public void setSignImg(byte[] signImg) {
    this.signImg = signImg;
  }

  public String getSigner() {
    return signer;
  }

  public void setSigner(String signer) {
    this.signer = signer;
  }

  public String getDn() {
    return dn;
  }

  public void setDn(String dn) {
    this.dn = dn;
  }

  public String getContactInfo() {
    return contactInfo;
  }

  public void setContactInfo(String contactInfo) {
    this.contactInfo = contactInfo;
  }

public String getImgData() {
	return imgData;
}

public void setImgData(String imgData) {
	this.imgData = imgData;
}

}
