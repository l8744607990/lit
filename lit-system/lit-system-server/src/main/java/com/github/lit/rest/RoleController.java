package com.github.lit.rest;

import com.github.lit.constant.PluginConst;
import com.github.lit.model.AuthorityVo;
import com.github.lit.model.RoleQo;
import com.github.lit.model.RoleVo;
import com.github.lit.repository.entity.Role;
import com.github.lit.service.RoleService;
import com.lit.support.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * User : liulu
 * Date : 2017/11/19 16:43
 * version $Id: RoleController.java, v 0.1 Exp $
 */
@Controller
@RequestMapping(PluginConst.URL_PREFIX + "/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @GetMapping
    public Page<Role> list(RoleQo roleQo) {
        return roleService.findPageList(roleQo);
    }

    @GetMapping("/bind/authority")
    public List<AuthorityVo.TreeNode> bindAuthority(Long roleId) {
        return roleService.findAuthorityTree(roleId);
    }

    @PostMapping("/bind/user")
    public void bindUser(RoleVo.BindUser bindUser) {
        roleService.bindUser(bindUser.getUserId(), bindUser.getRoleIds());
    }

    @PostMapping("/bind/authority")
    public void doBindAuthority(RoleVo.BindAuthority bindAuthority) {
        roleService.bindAuthority(bindAuthority.getRoleId(), bindAuthority.getAuthorityIds());
    }

    @GetMapping("/{id}")
    public Role get(Long id) {
        return roleService.findById(id);
    }

    @PostMapping
    public Long add(Role role) {
        return roleService.insert(role);
    }

    @PutMapping
    public void update(Role role) {
        roleService.update(role);
    }

    @DeleteMapping("/delete")
    public void delete(Long[] ids) {
        roleService.delete(ids);
    }

}