package com.demo.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.demo.bean.User;
import com.demo.dao.DAO;

/**
 * 注册的Servlet
 */
public class RegisterServlet extends HttpServlet {
	private DAO dao = new DAO();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (username != null && password != null) {// 用户名密码不为空
			User user = dao.getOne("select id,username,password from allgo_db where username = ?", User.class,
					username);// 根据用户名查询数据库中是否已经存在这个对象
			if (user != null) {// 如果存在
				response.setContentType("text/json;charset=utf-8");// 防止乱码
				response.getWriter().write("服务器返回数据:用户已存在");

			} else {// 如果不存在
				// 把用户名密码存入数据库中
				dao.update("insert into allgo_db(username,password)values(?,?)", username, password);
				response.setContentType("text/json;charset=utf-8");// 防止乱码
				response.getWriter().write("服务器返回数据:注册成功");
			}

		}

	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		doGet(req, resp);
	}
}
