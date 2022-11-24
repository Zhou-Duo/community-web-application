package com.nowcoder.community.controller;

import com.nowcoder.community.service.AlphaService;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/alpha")
public class AlphaController {
    @Autowired
    private AlphaService alphaService;

    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello() {
        return "Hello I'm the first Maven project @Xinrui.";
    }

    // 声明浏览器请求路径
    @RequestMapping("/data")
    @ResponseBody
    public String getData() {
        return alphaService.find();
    }

    // 如何获得请求对象和响应对象： 1. 在服务端利用对象获取
    @RequestMapping("/http")
    // 在参数中声明 HttpServletRequest 和 HttpServletResponse， 那么 DispatcherServlet
    // 在调用该方法是会自动将这两个对象传进来
    public void http(HttpServletRequest request, HttpServletResponse response) {
        // 通过 response 对象向浏览器输出数据，因此不依赖此处返回值
        // 获取请求数据
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath()); // 请求路径
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name); // head 是 key-value 结构
            System.out.println(name + ": " + value);
        }
        System.out.println(request.getParameter("code")); // 获取参数

        // 返回响应数据
        response.setContentType("text/html;charset=utf-8"); // 设定响应数据形式
        // 利用响应流返回数据
        try (PrintWriter writer = response.getWriter()) {
            writer.write("<h1>牛客网</h1>"); // 在此传入 html 完整网页，此处为示例
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 如何获得请求对象和响应对象：2. 更加简便的封装方式
    // 基于 request 接受请求数据，基于 response 返回响应数据

    // GET 请求
    // 2种传参方式 1. 问号拼 2. 把参数拼到路径当中

    // 1. 问号拼
    // /students?current=1&limit=20 //查询多个学生分页
    @RequestMapping(path = "/students", method = RequestMethod.GET)
    @ResponseBody
    // DispatcherServlet 检测到参数后，会将 request 中与之参数名匹配的参数直接赋给他们
    public String getStudents(
            @RequestParam(name = "current", required = false, defaultValue = "1") int current,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) {
        // System.out.println(current);
        // System.out.println(limit);
        return "Student Zhou Xinrui";
    }

    // 2. 把参数拼到路径当中
    // /students/123 // 通过 ID 查询单个学生, 直接将参数编入路径中
    @RequestMapping(path = "/student/{id}", method = RequestMethod.GET)
    @ResponseBody
    // 通过 @PathVariable 注解从路径中得到参数
    public String getStudent(@PathVariable("id") int id) {
        System.out.println(id);
        return "a student";
    }

    // POST 请求
    @RequestMapping(path = "student", method = RequestMethod.POST)
    @ResponseBody
    // 如何获取 POST 请求中的数据呢？直接声明参数，DispatcherServlet 检测到参数后，会将 post 中与之参数名匹配的参数直接赋给他们
    public String saveStudent(String name, String age) {
        System.out.println(name);
        System.out.println(age);
        return "Success!";
    }

    // 响应 HTML 数据
    @RequestMapping(path = "teacher", method = RequestMethod.GET)
    public ModelAndView getTeacher() {
        ModelAndView mav = new ModelAndView(); // 手动实例化后返回
        // 传入数据
        mav.addObject("name", "周歆蕊");
        mav.addObject("age", "22");
        // 传入模版
        mav.setViewName("/demo/view"); // 省略.html
        return mav;
    }

    @RequestMapping(path = "/school", method = RequestMethod.GET)
    // model 不是自己创建的， DispatcherServlet 调用函数是检测到参数，会自动实例化 Model 后传入. model 存储在容器中
    public String getSchool(Model model) {
        model.addAttribute("name", "香港科技大学");
        model.addAttribute("age", "30");
        return "/demo/view"; // 返回 view 路径
    }

    // 响应 JSON 数据 （一般在异步请求中）
    // Java 对象 --> JSON 字符串--> JS 对象
    @RequestMapping(path = "/employee", method = RequestMethod.GET)
    // DispatcherServlet 调用函数时，检测到 @ResponseBody 注解 且函数返回的是 Java 对象，会自动将返回的 Map 转化为
    // JSON 字符串
    @ResponseBody
    public Map<String, Object> getEmp() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Minyue");
        map.put("age", "23");
        map.put("salary", "35000");
        return map;
    }

    // 返回集合
    @RequestMapping(path = "/employees", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getEmps() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> emp1 = new HashMap<>();
        emp1.put("name", "Minyue");
        emp1.put("age", "23");
        emp1.put("salary", "35000");
        list.add(emp1);

        Map<String, Object> emp2 = new HashMap<>();
        emp2.put("name", "Xinrui");
        emp2.put("age", "22");
        emp2.put("salary", "30000");
        list.add(emp2);
        return list;
    }

    // cookie示例

    @RequestMapping(path = "/cookie/set", method = RequestMethod.GET)
    @ResponseBody
    public String setCookie(HttpServletResponse response) {
        // 创建cookie
        Cookie cookie = new Cookie("code", CommunityUtil.generateUUID());
        // 设置cookie生效的范围
        cookie.setPath("/community/alpha");
        // 设置cookie的生存时间
        cookie.setMaxAge(60 * 10);
        // 发送cookie
        response.addCookie(cookie);

        return "set cookie";
    }

    @RequestMapping(path = "/cookie/get", method = RequestMethod.GET)
    @ResponseBody
    public String getCookie(@CookieValue("code") String code) {
        System.out.println(code);
        return "get cookie";
    }

    // session示例

    @RequestMapping(path = "/session/set", method = RequestMethod.GET)
    @ResponseBody
    public String setSession(HttpSession session) {
        session.setAttribute("id", 1);
        session.setAttribute("name", "Test");
        return "set session";
    }

    @RequestMapping(path = "/session/get", method = RequestMethod.GET)
    @ResponseBody
    public String getSession(HttpSession session) {
        System.out.println(session.getAttribute("id"));
        System.out.println(session.getAttribute("name"));
        return "get session";
    }

}
