package fun.yeshu.nosugar.deviceinfo;


public class CallInfo {

    public String number; // 号码
    public long date;     // 日期
    public int type;      // 类型：来电、去电、未接
    public long duration;

    public CallInfo(String number, long date, int type, long duration) {
        this.number = number;
        this.date = date;
        this.type = type;
        this.duration = duration;
    }
}
