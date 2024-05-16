package websocket.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author binbin
 * @Date 2024 04 10 13 45
 **/
public enum WsFlagEnum {
    RESOURCE_REQUEST(1, "RESOURCE_REQUEST"),
    PRIVATE_KEY_REQUEST(2, "PRIVATE_KEY_REQUEST"),
    DOWNLOAD_REQUEST(3, "DOWNLOAD_REQUEST"),
    RESOURCE_RESPONSE(4, "RESOURCE_RESPONSE"),
    PRIVATE_KEY_RESPONSE(5, "PRIVATE_KEY_RESPONSE"),
    DOWNLOAD_RESPONSE(6, "DOWNLOAD_RESPONSE");
    private int code;
    private String description;

    WsFlagEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static WsFlagEnum getWsFlagEnum(String desc) {

        for (WsFlagEnum enumValue : WsFlagEnum.values()) {
            if (desc.equals(enumValue.description)) {
                return enumValue;
            }

        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static List<String> getDesc() {
        List<String> descriptionList = new ArrayList<>();

        for (WsFlagEnum enumValue : WsFlagEnum.values()) {
            descriptionList.add(enumValue.getDescription());
        }
        return descriptionList;
    }
}
