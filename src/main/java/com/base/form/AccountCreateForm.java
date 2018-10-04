package com.base.form;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class AccountCreateForm implements Serializable {

    private static final long serialVersionUID = 1223908077393105940L;

    @NotNull
    @Size(min = 1, max = 5)
    private String name;

    @NotNull
    @Size(min = 10, max = 11)
    private String tel;

    @NotNull
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private Date dateOfBirth;

    @NotNull
    @Size(min = 5, max = 30)
    private String email;

    private List<String> roles;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
