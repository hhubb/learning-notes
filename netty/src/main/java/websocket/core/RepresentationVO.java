package websocket.core;


import lombok.Data;

import java.util.Date;

@Data
public class RepresentationVO {

    private Integer fromType;

    private String name;

    private Date createTime;
    private String resourceId;

    private String license;

    private String policyPatternValue;

    private String domain;

    private String title;

    private String description;

    private Boolean ifPolicyEffective;

    private Boolean downloadable;

    private Boolean ifCollect;

    private String targetConnectorName;

    private String targetConnectorId;

    private String targetBrokerUrl;

}
