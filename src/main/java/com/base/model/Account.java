package com.base.model;

public class Account {

    private int userSeqno;
    private String loginId;
    private String pwd;
    private String userName;
    private String phoneNo;
    private String Email;
    private String sex;
    private String birth;
    private int userStatus;
    private String rgstDtm;
    private String updtDtm;
    private boolean enabled;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Account{");
        sb.append("userSeqno=").append(userSeqno);
        sb.append(", loginId='").append(loginId).append('\'');
        sb.append(", pwd='").append(pwd).append('\'');
        sb.append(", userName='").append(userName).append('\'');
        sb.append(", phoneNo='").append(phoneNo).append('\'');
        sb.append(", Email='").append(Email).append('\'');
        sb.append(", sex='").append(sex).append('\'');
        sb.append(", birth='").append(birth).append('\'');
        sb.append(", userStatus=").append(userStatus);
        sb.append(", rgstDtm='").append(rgstDtm).append('\'');
        sb.append(", updtDtm='").append(updtDtm).append('\'');
        sb.append(", enabled=").append(enabled);
        sb.append('}');
        return sb.toString();
    }

    public int getUserSeqno() {
        return userSeqno;
    }

    public void setUserSeqno(int userSeqno) {
        this.userSeqno = userSeqno;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    public String getRgstDtm() {
        return rgstDtm;
    }

    public void setRgstDtm(String rgstDtm) {
        this.rgstDtm = rgstDtm;
    }

    public String getUpdtDtm() {
        return updtDtm;
    }

    public void setUpdtDtm(String updtDtm) {
        this.updtDtm = updtDtm;
    }

    public boolean isEnabled() {
        return this.userStatus == 1;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
