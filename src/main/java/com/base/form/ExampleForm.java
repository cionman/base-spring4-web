package com.base.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class ExampleForm implements Serializable {

    private static final long serialVersionUID = 5730579553333008912L;

    @NotEmpty
    @Size(max = 100)
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
