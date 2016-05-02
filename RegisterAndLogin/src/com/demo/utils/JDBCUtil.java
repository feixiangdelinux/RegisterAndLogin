package com.demo.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 与数据库操作相关的工具类
 */
public class JDBCUtil {

	// 关闭以连接的数据库操作
	public static void close(Connection conn, PreparedStatement ps, ResultSet rs) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	// 获取数据的连接
	public static Connection getConnection() throws Exception {
		// 1.从配置文件中读取数据库连接的4个基本信息：
		Properties info = new Properties();
		// 加载文件的方式一：配置文件保存在当前工程下
		// FileInputStream fis = new FileInputStream(new
		// File("jdbc.properties"));
		// 加载文件的方式二：配置文件保存在src下
		InputStream is = JDBCUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");

		info.load(is);

		String driverClass = info.getProperty("driverClass");
		String url = info.getProperty("url");
		String userName = info.getProperty("userName");
		String password = info.getProperty("password");

		// 2.加载驱动
		Class.forName(driverClass);

		// 3.通过DriverManager获取数据库的连接
		Connection conn = DriverManager.getConnection(url, userName, password);
		return conn;
	}
}
