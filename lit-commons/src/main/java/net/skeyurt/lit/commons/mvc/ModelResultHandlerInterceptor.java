package net.skeyurt.lit.commons.mvc;

import net.skeyurt.lit.commons.context.ResultConst;
import net.skeyurt.lit.commons.exception.RunResultHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User : liulu
 * Date : 2017/3/20 20:21
 * version $Id: ModelResultHandlerInterceptor.java, v 0.1 Exp $
 */
public class ModelResultHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        RunResultHolder.clear();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        ModelMap modelMap = modelAndView == null ? null : modelAndView.getModelMap();
        if (modelMap != null) {
            if (RunResultHolder.hasError()) {
                modelMap.put(ResultConst.SUCCESS, false);
                String code = RunResultHolder.getCode(false);
                if (StringUtils.isNotEmpty(code)) {
                    modelMap.put(ResultConst.CODE, code);
                }
                String messages = RunResultHolder.getStrMessages();
                if (StringUtils.isNotEmpty(messages)) {
                    modelMap.put(ResultConst.MASSAGE, messages);
                }
            } else {
                if (modelMap.get(ResultConst.SUCCESS) == null) {
                    modelMap.put(ResultConst.SUCCESS, true);
                }
            }
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RunResultHolder.clear();
    }
}