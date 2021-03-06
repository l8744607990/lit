package com.github.lit.security.model;

import com.github.lit.jdbc.annotation.GeneratedValue;
import com.github.lit.jdbc.annotation.Id;
import com.github.lit.jdbc.annotation.Table;
import com.github.lit.jdbc.enums.GenerationType;
import com.github.lit.plugin.context.PluginConst;
import lombok.*;

import java.io.Serializable;

/**
 * User : liulu
 * Date : 2017/11/19 16:28
 * version $Id: RoleAuth.java, v 0.1 Exp $
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = PluginConst.TABLE_PREFIX + "role_authority")
public class RoleAuthority implements Serializable {

    private static final long serialVersionUID = 854150550312268210L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleAuthorityId;

    /**
     * 角色Id
     */
    private Long roleId;

    /**
     * 权限Id
     */
    private Long authorityId;

    /**
     * 角色编号
     */
    private String roleCode;

    /**
     * 权限码
     */
    private String authorityCode;

}
