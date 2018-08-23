package com.duobi.manager.sys.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CurrentSessionUtil {

    public static HttpServletRequest getCurrentRequest(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    public static HttpServletResponse getCurrentResponse(){
        HttpServletResponse resp = ((ServletWebRequest)RequestContextHolder.getRequestAttributes()).getResponse();
        return resp;
    }

    public static HttpSession getCurrentSession(){
        HttpServletRequest request = getCurrentRequest();
        if (request != null) return request.getSession();
        else return null;
        //通过shiro获取session。不管是通过 request.getSession或者subject.getSession获取到session，操作session，两者都是等价的。在使用默认session管理器的情况下，操作session都是等价于操作HttpSession。
//        Subject subject = SecurityUtils.getSubject();
//        Session session = subject.getSession(false);
//        if (session == null){
//            session = subject.getSession();
//        }
//        if (session != null){
//            return session;
//        }
    }

    public static void putToSession(String key,Object value) throws Exception{
        HttpSession session = getCurrentSession();
        if (session == null) throw new Exception("无法获取当前用户的session");
        session.setAttribute(key,value);
    }

    public static Object getFromSession(String key) throws Exception{
        HttpSession session = getCurrentSession();
        if (session == null) throw new Exception("无法获取到当前用户的session");
        Object o = session.getAttribute(key);
        return o;
    }

    public static void removeFromSession(String key) throws Exception{
        HttpSession session = getCurrentSession();
        if (session == null) throw new Exception("无法获取当前用户的session");
        session.removeAttribute(key);
    }


}
