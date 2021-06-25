package com.example.demo;

import ch.qos.logback.core.net.server.Client;
import jdk.nashorn.internal.ir.debug.JSONWriter;
import net.bytebuddy.implementation.bind.annotation.Super;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.util.DigestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import sun.net.www.http.HttpClient;

import javax.xml.ws.spi.http.HttpHandler;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@SpringBootTest
class DemoApplicationTests {
    /**
     * 客户端id
     */
    private static final String CLIENT_ID ="";
    /**
     * 密钥 游戏方提供创建
     */
    private static final String CLIENT_SECRET = "";
    /**
     * 授权类型，固定值
     */
    private static final String GRANT_TYPE="client_credentials";
    /**
     * 授权范围
     */
    private static final String SCOPE ="ALL";
    /**
     * 授权地址
     */
    private static final String AUTH_URL ="https://open-api.3dtank.com/oauth/token";
    /**
     * 请求签名密钥，游戏方提供创建
     */
    private static final String SIGN_KEY="HSnbLl6RWM^W&fA*30HJ0lbBmvK9wW#p";

    private static RestTemplate restTemplate = new RestTemplate();

    /**
     * 登录
     * @throws IOException
     */
    @Test
    void Login() throws IOException {
        /**
         * 添加认证 header
         */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "bearer "+getHeader());

        long ts =System.currentTimeMillis();
        MultiValueMap<String, Object> map= new LinkedMultiValueMap();
        map.add("user_id", 123);
        map.add("server_id",1);
        map.add("ts",ts);
        map.add("fcm",1);

        Map signMap =new HashMap();
        signMap.put("user_id",123);
        signMap.put("server_id",1);
        signMap.put("ts",ts);
        signMap.put("fcm",1);
        map.add("sign",createSign(signMap));

        HttpEntity entity = new HttpEntity(map,headers);
        ResponseEntity<Map> exchange = restTemplate.exchange("https://open-api.3dtank.com/tank-ymxk-web/api/login", HttpMethod.POST, entity, Map.class);
        System.out.println(exchange.getBody());
        /**
         * 返回值：
         * 跳转 data中的url地址就可以登录游戏
         * {
         *     "errCode":0,
         *     "message":"ok",
         *     "data":"https://3dtank.com/play/?token=AAAAAAAAAAAAAAAAAAAAAHpEE0jUB2vi1ObgoFkNDGHza2BDkxNVm6uHHaYAhfyy&sub_partner_id=ymxk",
         *     "ticks":0
         * }
         */
    }

    /**
     * 获取用户信息
     * @throws IOException
     */
    @Test
    void getUserInfo() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "bearer "+getHeader());
        long ts =System.currentTimeMillis();
        MultiValueMap<String, Object> map= new LinkedMultiValueMap();
        map.add("user_id", 123);
        map.add("ts",ts);

        Map signMap =new HashMap();
        signMap.put("user_id",123);
        signMap.put("ts",ts);
        map.add("sign",createSign(signMap));

        HttpEntity entity = new HttpEntity(map,headers);
        ResponseEntity<Map> exchange = restTemplate.exchange("https://open-api.3dtank.com/tank-ymxk-web/api/get-user-info", HttpMethod.POST, entity, Map.class);
        System.out.println(exchange.getBody());
    }

    /**
     * 充值
     * @throws IOException
     */
    @Test
    void topUp() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "bearer "+getHeader());
        String orderId = UUID.randomUUID().toString();
        long ts =System.currentTimeMillis();
        MultiValueMap<String, Object> map= new LinkedMultiValueMap();
        map.add("user_id", 123);
        map.add("order_id",orderId);
        map.add("ts",ts);
        map.add("money",1);
        map.add("coin",16);

        Map signMap =new HashMap();
        signMap.put("user_id",123);
        signMap.put("order_id",orderId);
        signMap.put("ts",ts);
        signMap.put("money",1);
        signMap.put("coin",16);
        map.add("sign",createSign(signMap));

        HttpEntity entity = new HttpEntity(map,headers);
        ResponseEntity<Map> exchange = restTemplate.exchange("https://open-api.3dtank.com/tank-ymxk-web/api/notification-of-top-up", HttpMethod.POST, entity, Map.class);
        System.out.println(exchange.getBody());
    }



    private String createSign(Map map){
        /**
         * 1. 升序排序
         * 2. 拼接字符串 +SIGN_KEY
         * 3. 返回 MD5 签名
         */
        Map<String,Object> parameters= new TreeMap<>(map);
        List<String> paramsList = new ArrayList<>();
        for (String k : parameters.keySet()) {
            String v = parameters.get(k).toString();
            if (k.equals("sign") || StringUtils.isEmpty(v)) {
                continue;
            }
            paramsList.add(String.format("%s=%s", k, v));
        }
        String str = String.join("&", paramsList)+SIGN_KEY;
        System.err.println("签名字符串："+str);
        String sign = DigestUtils.md5DigestAsHex(str.getBytes());
        return sign;
    }



    private String getHeader() throws IOException {
        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("client_id", CLIENT_ID);
        map.add("client_secret",CLIENT_SECRET);
        map.add("scope",SCOPE);
        map.add("grant_type",GRANT_TYPE);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity entity = new HttpEntity(map,headers);
        ResponseEntity<Map> exchange = restTemplate.exchange(AUTH_URL, HttpMethod.POST, entity, Map.class);
        /**
         * 返回值：
         * access_token 认证的token，
         * {
         *     "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJBTEwiXSwiZXhwIjoxNjI0NDQ4OTE4LCJqdGkiOiIxMDVlYTU3MC00ZjI1LTQxNDctODdiNC01Njk1YWI4OGJjM2UiLCJjbGllbnRfaWQiOiJ5bXhrIn0.ePntP___d2zHKioMA-h03alv1F632qHa9g16rl3xcpE",
         *     "token_type": "bearer",
         *     "expires_in": 5999,
         *     "scope": "ALL",
         *     "jti": "105ea570-4f25-4147-87b4-5695ab88bc3e"
         * }
         */
        if(!exchange.getBody().containsKey("access_token")){
            throw new RuntimeException("auth error!");
        }
        return exchange.getBody().get("access_token").toString();
    }

}
