package com.github.lit.service.impl;

import com.github.lit.model.LoginUser;
import com.github.lit.model.UserQo;
import com.github.lit.model.UserVo;
import com.github.lit.repository.UserRepository;
import com.github.lit.repository.entity.User;
import com.github.lit.service.UserService;
import com.github.lit.util.UserUtils;
import com.lit.support.data.domain.Page;
import com.lit.support.data.domain.PageUtils;
import com.lit.support.exception.BizException;
import com.lit.support.util.bean.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * User : liulu
 * Date : 2017/8/12 11:20
 * version $Id: UserServiceImpl.java, v 0.1 Exp $
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {


    @Resource
    private UserRepository userRepository;

    @Override
    public Long register(UserVo.Register register) {
        if (!Objects.equals(register.getPassword(), register.getConfirmPassword())) {
            throw new BizException("密码和确认密码不一致");
        }
        if (!Objects.equals(register.getSmsCaptcha(), "1234")) {
            throw new BizException("手机验证码不正确");
        }

        return insert(BeanUtils.convert(register, new UserVo.Add()));
    }

    @Override
    public User findById(Long id) {
        return userRepository.selectById(id);
    }

    @Override
    public User findByName(String username) {
        return userRepository.selectByProperty(User::getUsername, username);
    }

    @Override
    public Page<UserVo.List> findPageList(UserQo qo) {
        LoginUser loginUser = UserUtils.getLoginUser();

//        if (loginUser != null && loginUser.hasOrg()) {
//            qo.setSerialNum(loginUser.getLevelIndex());
//            if (StringUtils.isEmpty(qo.getOrgCode())) {
//                qo.setOrgCode(loginUser.getOrgCode());
//            }
//        }
        Page<User> pageList = userRepository.selectPageList(qo);
        return PageUtils.convert(pageList, UserVo.List.class);
    }

    @Override
    public Long insert(UserVo.Add user) {
        checkUsername(user.getUsername());
        checkEmail(user.getEmail());
        checkMobileNum(user.getMobileNum());

        User addUser = BeanUtils.convert(user, new User());

        // 设置默认值
        addUser.setLock(false);
        addUser.setCreator(UserUtils.getLoginUser().getUsername());
        String password = StringUtils.hasText(user.getPassword()) ? user.getPassword() : "123456";
        addUser.setPassword(UserUtils.encode(password));
        userRepository.insert(addUser);
        return addUser.getId();
    }

    @Override
    public void update(UserVo.Update user) {

        User oldUser = findById(user.getId());

        if (!Objects.equals(oldUser.getUsername(), user.getUsername())) {
            checkUsername(user.getUsername());
        }
        if (!Objects.equals(oldUser.getEmail(), user.getEmail())) {
            checkEmail(user.getEmail());
        }
        if (!Objects.equals(oldUser.getMobileNum(), user.getMobileNum())) {
            checkMobileNum(user.getMobileNum());
        }
        User upUser = BeanUtils.convert(user, new User());
        userRepository.updateSelective(upUser);
    }

    private void checkUsername(String username) {

        if (StringUtils.isEmpty(username)) {
            return;
        }
        int count = userRepository.countByProperty(User::getUsername, username);
        if (count >= 1) {
            throw new BizException("该用户名被使用");
        }
    }

    private void checkEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            return;
        }
        int count = userRepository.countByProperty(User::getEmail, email);
        if (count >= 1) {
            throw new BizException("该邮箱已被使用");
        }
    }

    private void checkMobileNum(String mobileNum) {
        if (StringUtils.isEmpty(mobileNum)) {
            return;
        }
        int count = userRepository.countByProperty(User::getMobileNum, mobileNum);
        if (count >= 1) {
            throw new BizException("该手机号已被使用");
        }
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }


}