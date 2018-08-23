package com.duobi.manager.sys.config;
import com.duobi.manager.sys.entity.User;
import com.duobi.manager.sys.utils.ResponseJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Order(1)
@WebFilter(filterName = "checkLoginStatusFilter", urlPatterns = "/*")
public class CheckLoginStatusFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(CheckLoginStatusFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if(canIgnore(request)){
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }

        //校验用户是否已经登陆
        User principal = (User) SecurityUtils.getSubject().getPrincipal();
        if (principal != null) filterChain.doFilter(servletRequest,servletResponse);
        else {
            servletResponse.setCharacterEncoding("UTF-8");
            servletResponse.setContentType("application/json; charset=utf-8");
            try{
                ObjectMapper objectMapper = new ObjectMapper();
                ResponseJson responseJson = new ResponseJson();
                responseJson.setSuccess(false);
                responseJson.setMsg("用户未登录或登录超时,请退出后重新登录");
//                response.sendError(407,"未登录或登录失效");
                PrintWriter out = servletResponse.getWriter();
                out.append(objectMapper.writeValueAsString(responseJson));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void destroy() {

    }

    /**
     * 设置可以不登陆即能通过的地址，如静态资源、登录地址、登出地址、根地址
     * @param request
     * @return
     */
    private boolean canIgnore(HttpServletRequest request) {
        List<String> prefixIignores = new ArrayList<>();
        String url = request.getRequestURI();

        if (url.equals("/")) return true;//放行第一次加载页面时请求的根地址
        if (url.equals("/sys/login")) return true; //放行登录请求
        if (url.equals("/sys/logout")) return true; //放行退出登录请求

        prefixIignores.add("/static/"); //静态资源
        prefixIignores.add("/font-awesome"); //静态资源
        prefixIignores.add("/favicon.ico"); //静态资源
        for (String ignore : prefixIignores) {
            if (url.startsWith(ignore)) {
                return true;
            }
        }
        return false;
    }

}
