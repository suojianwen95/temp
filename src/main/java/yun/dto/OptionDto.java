package yun.dto;


import javax.validation.constraints.Size;

public class OptionDto {

    @Size(min = 1,max = 255,message = "参数值不能为空")
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
