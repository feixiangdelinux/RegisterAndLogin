# RegisterAndLogin
android登录和注册功能,包括客户端和服务端
服务端:
	用的是apache-tomcat-7.0.69服务器,把客户端传来的用户名和密码存入Mysql数据库中.在web工程中有一个jdbc.properties的文件,里面存放了Mysql数据库的信息
	说明
driverClass=com.mysql.jdbc.Driver(要连接的数据库类型,mysql数据库)
url=jdbc:mysql://localhost:3306/test(url对应的是数据库中的那张表)
userName=root(访问数据库的用户名)
password=root(访问数据库的密码)
在你导入此工程,想要运行成功就要安装apache-tomcat-7.0.69和Mysql数据库,数据库的配置信息就要填入jdbc.properties文件中,
客户端:
	非常简单的demo,只有登录,注册和主页面,联网上传信息用的是xutils框架.只要你服务端配置正确.修改demo中对应的url地址即可
	