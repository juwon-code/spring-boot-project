package juwoncode.commonblogproject.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailType {
    REGISTER("REGISTER");

    private String value;
}
