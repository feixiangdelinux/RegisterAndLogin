package com.demo.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.demo.bean.User;
import com.demo.dao.DAO;

/**
 * 登录的Servlet
 */
public class LoginServlet extends HttpServlet {
	private DAO dao = new DAO();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		// 打印一个信息
		System.out.println("username   " + username + "   password   " + password);
		if (password.isEmpty() || username.isEmpty()) {// 用户名或密码为空
			response.setContentType("text/json;charset=utf-8");// 防止乱码
			response.getWriter().write("用户名或密码不能为空");
		} else {// 用户名密码不为空
			// 根据用户名从Mysql中遍历所有数据,找到对应的User信息,比对密码是否正确,密码正确进入,
			User user = dao.getOne("select id,username,password from allgo_db where username = ?", User.class,
					username);// 根据用户名查询数据库中是否已经存在这个对象
			if (user == null) {// 用户不存在
				response.setContentType("text/json;charset=utf-8");// 防止乱码
				response.getWriter().write("用户名不正确,请重新输入");
			} else {
				if (user.getPassword().equals(password)) {
					response.setContentType("text/json;charset=utf-8");// 防止乱码
					response.getWriter().write("登录成功");
				} else {
					response.setContentType("text/json;charset=utf-8");// 防止乱码
					response.getWriter().write("密码不正确,请重新输入");
				}
			}
		}

	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		doGet(req, resp);
	}
}