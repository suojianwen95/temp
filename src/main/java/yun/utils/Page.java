package yun.utils;

import ninja.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Page<T> {

    public Page(int current,int limit){
        if(current>0){
            this.current = current;
        }else{
            this.current = 1;
        }

        if(limit>0){
            this.limit = limit;
        }else{
            this.limit = 10;
        }
    }

    public Page(int current){
        this.limit = 10;
        if(current>0){
            this.current = current;
        }else{
            this.current = 1;
        }
    }

    public void setAllrecords(long allCount){
        this.allrecords=(int)allCount;
        if(this.allrecords%limit==0){
            this.total = (int)(this.allrecords/limit);
        }else{
            this.total = (int)(this.allrecords/limit)+1;
        }
    }
    public void setItemCount(long itemCount){
        if(itemCount%limit==0){
            this.total = (int)(itemCount/limit);
        }else{
            this.total = (int)(itemCount/limit)+1;
        }
    }
    private List<T> items;
    /**
     * 总页码数
     */
    private Integer total;
    private Integer current;
    private Integer limit;
    private String params = "";
    /**
     * 总条数
     */
    private Integer allrecords;

    /**
     * 设置item的元素
     * @param its
     */
    public void setItems(List<T> its){
        this.items = its;
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return 第1页页码
     */
    public Integer getFirst(){
        return 1;
    }

    /**
     *
     * @return 最后一页页码
     */
    public Integer getLast(){
        return total;
    }

    /**
     * @return 当前页码
     */
    public Integer getCurrent(){return current;}

    /**
     *
     * @return 获取上一页的页码
     */
    public Integer getPre(){
        if(current == 1){
            return null;
        }else{
            return this.current-1;
        }
    }

    /**
     *
     * @return 获取下一页的页码
     */
    public Integer getNext(){
        if(current == total){
            return null;
        }else{
            return this.current+1;
        }
    }


    /**
     *
     * @return 获取当前页面数据列表
     */
    public List<T> getItems(){
        return this.items;
    }

    /**
     * 获取当前页数据的第一条
     * @return
     */
    public Integer getFirstResult(){
        if(this.current>1){
            return (this.current-1)*this.limit;
        }else{
            return 0;
        }
    }

    /**
     * 获取当前页面展示多少条结果
     * @return
     */
    public Integer getMaxResults(){
        return this.limit;
    }
    /**
     * 获取页面所有记录结果
     * @return
     */
    public Integer getAllResults(){
        return this.allrecords;
    }

    public String getParams(){
        if(params.endsWith("&")){
            params = params.substring(0,params.length()-1);
        }
        return this.params;
    }
    public void setParams(Context context){
        Map<String,String[]> paramsMap = context.getParameters();
        StringBuilder sb = new StringBuilder("&");
        paramsMap.forEach((k,v)->{
            if(!k.equals("p")){
                if(v.length==1){
                    sb.append(k)
                            .append("=")
                            .append(v[0])
                            .append("&");
                }
                if(v.length>1){
                    for(String vi: v){
                        sb.append(k)
                                .append("[]=")
                                .append(vi)
                                .append("&");
                    }
                }
            }
        });
        this.params = sb.toString();
    }

    public List<Integer> getPageNumbers(){
        List<Integer> list = new ArrayList<>();
        for(int i = 1;i<=getLast();i++){
            list.add(i);
        }
        return list;
    }

}
