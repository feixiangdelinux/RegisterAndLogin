package com.example.denglu.activity;

import com.example.denglu.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 登录页面
 */
public class LoginActivity extends Activity implements OnClickListener {
	private EditText userName;
	private EditText passWord;
	private Button loginButton;
	private Button registerButton;
	private String name;// 用户名
	private String pwd;// 密码
	/**
	 * url地址(根据你服务器的地址进行设置,主要是设置http://or9574ay.xicp.net:8888这里,or9574ay.xicp.
	 * net对应的是你本机的ip地址,比如192.168.1.1,8888是端口号,tomcat默认是8080)
	 */
	private String url = "http://192.168.1.101:8080/RegisterAndLogin/LoginServlet";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		userName = (EditText) findViewById(R.id.userName_loginActivity_et);
		passWord = (EditText) findViewById(R.id.passWord_loginActivity_et);
		loginButton = (Button) findViewById(R.id.login_loginActivity_btn);
		registerButton = (Button) findViewById(R.id.register_loginActivity_btn);
		loginButton.setOnClickListener(this);
		registerButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_loginActivity_btn:// 点击登录按钮
			name = userName.getText().toString();
			pwd = passWord.getText().toString();
			uploadUserData(url, name, pwd);

			break;
		case R.id.register_loginActivity_btn:// 点击注册按钮
			Intent intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
	}

	/**
	 * 上传用户资料(用户名和密码)
	 */
	private void uploadUserData(String url, String name, String pwd) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("username", name);
		params.addQueryStringParameter("password", pwd);
		httpUtils.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {
			/**
			 * 上传失败
			 */
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(getApplicationContext(), arg1, Toast.LENGTH_SHORT).show();
			}

			/**
			 * 上传成功
			 */
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				Toast.makeText(getApplicationContext(), arg0.result, Toast.LENGTH_SHORT).show();
				if("登录成功".equals(arg0.result)){
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					startActivity(intent);
					finish();	
				}
			
			}
		});
	}
}
