package yun.dto;


import javax.validation.constraints.Size;

public class CategoryDto {

    @Size(min = 2,max = 255,message = "软件类别名称需要2-255个字符组成")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
