package com.demo.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import com.demo.utils.JDBCUtil;

/**
 * 操作数据库的工具类
 * 最下面有这个类的详细的使用说明
 */
public class DAO {

	/**
	 * 增删改的操作所对应的方法
	 * 
	 * @param sql
	 *            第一个参数传sql语句
	 * @param args
	 *            第二个填sql语句中的参数
	 */
	public void update(String sql, Object... args) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			// 1.获取数据库的连接
			conn = JDBCUtil.getConnection();
			// 2.预编译sql语句，返回一个PreparedStatement实现类的对象
			ps = conn.prepareStatement(sql);

			// 3.填充占位符
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}

			// 4.执行操作
			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			// 5.关闭操作
			JDBCUtil.close(conn, ps, null);
		}
	}

	/**
	 * 相对通用的查询操作,返回一个对象
	 * 
	 * @param sql
	 *            第一个参数填查询sql语句
	 * @param clazz
	 *            第二个填Class的实例
	 * @param args
	 *            第三个填sql参数
	 * @return 返回一个对象
	 * 
	 */
	public <T> T getOne(String sql, Class<T> clazz, Object... args) {
		Connection conn = null;
		PreparedStatement ps = null;

		ResultSet rs = null;
		try {
			// 1.获取连接
			conn = JDBCUtil.getConnection();
			// 2.预编译sql语句
			ps = conn.prepareStatement(sql);
			// 3.填充占位符
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}

			// 4.调用executeQuery()返回一个结果集：ResultSet
			rs = ps.executeQuery();
			// 结果集的元数据：ResultSetMetaData
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();// 获取结果集的列数
			T t = clazz.newInstance();

			// 5.对结果集的数据进行处理
			if (rs.next()) {
				// 通过循环将t对象的所有属性赋值
				for (int i = 0; i < columnCount; i++) {
					Object columnVal = rs.getObject(i + 1);// 获取列值

					String columnLabel = rsmd.getColumnLabel(i + 1);// 获取列的名字

					Field field = clazz.getDeclaredField(columnLabel);

					field.setAccessible(true);

					field.set(t, columnVal);
				}

				return t;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 6.关闭操作
			JDBCUtil.close(conn, ps, rs);

		}

		return null;
	}

	/**
	 * 相对通用的查询操作,返回一个对象的集合
	 * 
	 * @param sql
	 *            第一个参数填查询sql语句
	 * @param clazz
	 *            第二个填Class的实例
	 * @param args
	 *            第三个填sql参数
	 * @return 返回一个对象的集合
	 * 
	 */
	public <T> List<T> getForList(String sql, Class<T> clazz, Object... args) {
		Connection conn = null;
		PreparedStatement ps = null;

		ResultSet rs = null;
		try {
			// 1.获取连接
			conn = JDBCUtil.getConnection();
			// 2.预编译sql语句
			ps = conn.prepareStatement(sql);
			// 3.填充占位符
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}

			// 4.调用executeQuery()返回一个结果集：ResultSet
			rs = ps.executeQuery();
			// 结果集的元数据：ResultSetMetaData
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();// 获取结果集的列数
			List<T> list = new ArrayList<T>();
			// 5.对结果集的数据进行处理
			while (rs.next()) {
				T t = clazz.newInstance();
				// 通过循环将t对象的所有属性赋值
				for (int i = 0; i < columnCount; i++) {
					Object columnVal = rs.getObject(i + 1);// 获取列值

					String columnLabel = rsmd.getColumnLabel(i + 1);// 获取列的名字

					Field field = clazz.getDeclaredField(columnLabel);

					field.setAccessible(true);

					field.set(t, columnVal);
				}

				list.add(t);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 6.关闭操作
			JDBCUtil.close(conn, ps, rs);

		}

		return null;
	}
	/**
	 * 数据库每一行对应一个对象,而每一列对应对象中的一个属性.增上改查都是根据其中一个属性来修改对象,更新对象,查询对象和删除对象的 在sql语句中?
	 * 代表的是占位符,(和List<T>,中的T类似 ) 插入操作: sql语句格式:
	 * "insert into 表名(列名1,列名2,列名3)values(?,?,?)" 例子: DAO dao = new DAO(); dao
	 * .update( "insert into allgo_db(username,password)values(?,?)", "小名 ",
	 * "123"); 注释: 往allgo_db 表中插入一行username 为小名 ,password 为123的对象)
	 * 
	 * 删除操作: sql语句 格式: "delete from 表名 where  要删除的列名 = ?" 例子: DAO dao = new
	 * DAO(); dao .update ("delete from allgo_db where username = ?" , "789" );
	 * 注释: 把allgo_db 表中username 为789 的对象删除(这里删除就是删除一行))
	 * 
	 * 更新操作: sql语句 格式: "update 表名 set 列名2 = 列名值 where 列名1 = 列名值" 例子: DAO dao =
	 * new DAO(); dao .update(
	 * "update allgo_db set username= ?, password= ? where id = ?", "莫扎特 ",
	 * "11111" ,"16" ); 注释: 把allgo_db表中id为16的对象的username改为莫扎特,password的改为11111
	 * 
	 * 查询一个操作:(有别于上面的增删改操作需要填写三个参数.第一个参数填查询 sql语句,第二个填Class的实例,第三个填 sql参数) sql语句
	 * 格式: select 列名1,列名2 ,列名3 from 表名 where 列名4 = ? 例子: DAO dao = new DAO();
	 * User user =dao .getOne(
	 * "select id,username,password from allgo_db where username = ?",
	 * User.class , "小名"); // 根据用户名查询数据库中是否已经存在这个对象 注释: 从allgo_db 表中查询username
	 * =小名 的用户的id,username ,password 返回值是对象,如果表中没有则返回null)
	 * 
	 * 查询多个操作:(有别于上面的增删改操作需要填写三个参数.第一个参数填查询 sql语句,第二个填Class的实例,第三个填 sql参数) sql语句
	 * 格式: select 列名1,列名2 ,列名3 from 表名 where 列名4 < ? 例子: DAO dao = new DAO();
	 * List<User> list = dao .getForList(
	 * "select id,username,password from allgo_db where id < ?", User. class,
	 * "10" ); 注释: 从allgo_db 表中查询id <10 的用户的id,username ,password
	 * 返回值是一个对象的集合,如果表中没有则返回null)
	 * 
	 */
}
