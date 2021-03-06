package com.duowei.kitchen_china.httputils;


import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;

import java.util.HashMap;
import java.util.Map;

public final class DownHTTP {

    /**
     * 用开源项目Volley从服务器获取数据的工具，解决的4.x版本串行的问题
     * */

    /**
     * get请求方式调用方法，url：服务器中的地址，请求结果响应的事件调用的时候实现
     */
    public static void getVolley(String url, VolleyResultListener listener) {
        // 得到请求队列
        RequestQueue queue = MyVolley.getRequestQueue();
        queue.getCache().clear();
        // 创建http请求
        MyStringRequest myReq = new MyStringRequest(Method.GET, url, listener, listener);
        // 将http请求加入队列，volley库会开始执行请求
        queue.add(myReq);
    }

    /**
     * post请求方式调用的方法，Map类型参数需要在调用的时候传入，key值是服务器获取的字段名，values是对应字段的参数
     */
    public static void postVolley(String url, final Map<String, String> params, VolleyResultListener listener) {
        // 得到请求队列
        RequestQueue queue = MyVolley.getRequestQueue();
        // 创建http请求
        MyStringRequest myReq = new MyStringRequest(Method.POST, url, listener, listener) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                return params;
            }
        };
        // 将http请求加入队列，volley库会开始执行请求
        queue.add(myReq);
    }

    /**
     * post请求方式调用的方法，Map类型参数需要在调用的时候传入，key值是服务器获取的字段名，values是对应字段的参数
     */
    public static void postVolley6(String url, String sql, VolleyResultListener listener) {
        final HashMap map = new HashMap<String, String>();
        String base64 = Base64.getBase64(sql).replaceAll("\n", "");
        map.put("State", "6");
        map.put("Ssql", base64);
        // 得到请求队列
        RequestQueue queue = MyVolley.getRequestQueue();
        // 创建http请求
        MyStringRequest myReq = new MyStringRequest(Method.POST, url, listener, listener) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                return map;
            }
        };
        // 将http请求加入队列，volley库会开始执行请求
        queue.add(myReq);
    }

	public static void postVolley7(String url, String sql,VolleyResultListener listener) {
		final HashMap map=new HashMap<String,String>();
		String base64 = Base64.getBase64(sql).replaceAll("\n", "");
		map.put("State","7");
		map.put("Ssql",base64);
		// 得到请求队列
		RequestQueue queue = MyVolley.getRequestQueue();
		// 创建http请求
		MyStringRequest myReq = new MyStringRequest(Method.POST, url, listener,listener)
		{
			protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
				return map;
			}
		};
		// 将http请求加入队列，volley库会开始执行请求
		queue.add(myReq);
	}
}
