package yun.service;

public class CommonSdo<T> {

    public static final String NOTICE_SUCCESS="success";
    public static final String NOTICE_ERROR = "error";

    public CommonSdo() {

    }

    public CommonSdo(String notice, T item, String message) {
        this.notice = notice;
        this.message = message;
        this.item = item;
    }

    public static <D> CommonSdo<D> success(D d){
        CommonSdo<D> sdo = new CommonSdo<D>();
        sdo.setNotice(NOTICE_SUCCESS);
        sdo.setMessage("操作成功");
        sdo.setItem(d);
        return sdo;
    }

    public static <D> CommonSdo<D> success(D d,String message){
        CommonSdo<D> sdo = new CommonSdo<D>();
        sdo.setNotice(NOTICE_SUCCESS);
        sdo.setMessage(message);
        sdo.setItem(d);
        return sdo;
    }

    public static CommonSdo error(String message){
        CommonSdo sdo = new CommonSdo();
        sdo.setMessage(message);
        sdo.setItem(null);
        sdo.setNotice(NOTICE_ERROR);
        return sdo;
    }

    private String notice;
    private String message;
    private T item;

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    public Boolean isSuccess(){
        return NOTICE_SUCCESS.equals(getNotice());
    }
}
