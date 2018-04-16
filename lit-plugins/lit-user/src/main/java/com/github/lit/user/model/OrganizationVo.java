package com.github.lit.user.model;

import lombok.Data;

/**
 * User : liulu
 * Date : 2018/4/11 20:34
 * version $Id: OrganizationVo.java, v 0.1 Exp $
 */
@Data
public abstract class OrganizationVo {

    private String code;

    private String fullName;

    private String shortName;

    private String type;

    private String address;

    private String remark;

    @Data
    public static class List extends OrganizationVo {
        private Long id;
    }

    public static class Add extends OrganizationVo {

    }

    @Data
    public static class Update extends OrganizationVo {
        private Long id;
    }


}