package com.base.model;

public class UserVO {
    private int userSeqno;
    private String userName;
    private String phoneNo;
    private String email;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserVO{");
        sb.append("userSeqno=").append(userSeqno);
        sb.append(", userName='").append(userName).append('\'');
        sb.append(", phoneNo='").append(phoneNo).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public int getUserSeqno() {
        return userSeqno;
    }

    public void setUserSeqno(int userSeqno) {
        this.userSeqno = userSeqno;
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
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
