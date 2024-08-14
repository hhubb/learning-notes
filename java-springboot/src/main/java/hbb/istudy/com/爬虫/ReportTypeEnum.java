package hbb.istudy.com.爬虫;

/**
 * @Author binbin
 * @Date 2024 08 12 18 09
 **/

public enum ReportTypeEnum {
    Financial(1, "财务报告"),
    ESG(2, "ESG报告"),
    Un_Financial(3, "非财务报告"),
    patent(4, "专利报告"),
    study(5, "研究报告");
    private int code;
    private String desc;

    ReportTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
