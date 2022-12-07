package com.example.waimai.filter;

import com.alibaba.fastjson.JSON;
import com.example.waimai.common.BaseContext;
import com.example.waimai.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginCheckFilter", urlPatterns = {"/*"})
@Slf4j
public class LoginCheckFilter implements Filter {

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest; // 强转类型
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

//        1，请求的URI
        String requestURI = httpServletRequest.getRequestURI();
//        2，定义放行的请求路径
        String[] urls = new String[]{
                "/employees/login",
                "/employees/logout",
                "/frontend/**",
                "/backend/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login",
                "/user/logout"
        };
//        3，放行
        if(urlsCheck(urls, requestURI)){
            log.info("路径放行");
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        if (httpServletRequest.getSession().getAttribute("employee") != null) {
            log.info("网页登录过，放行");
            Long empId = (Long) httpServletRequest.getSession().getAttribute("employee"); // 在EmployeeController.login里放入
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        if (httpServletRequest.getSession().getAttribute("user") != null) {
            log.info("客户端登录过，放行");
            Long userId = (Long) httpServletRequest.getSession().getAttribute("user"); // 在UserController.login里放入
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }


//        4，不给放行
        httpServletResponse.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
        log.info("拦截请求: {}", requestURI);

    }

    public boolean urlsCheck(String[] urls, String requestURI) {
        for (String url : urls) {
            if (PATH_MATCHER.match(url, requestURI))
                return true;
        }
        return false;
    }
}
