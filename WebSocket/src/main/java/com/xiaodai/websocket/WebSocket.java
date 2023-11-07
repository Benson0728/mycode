package com.xiaodai.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;


import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{username}")
public class WebSocket {
    //WebSocket的Session
    private Session session;
    //存放当前用户名
    private String username;
    //存放当前在线用户数量
    private static Integer userNumber=0;
    //保存在线的WebSocket对象

    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {
        System.out.println("连接上了");
        this.session=session;
        //将当前对象放入websocket
        webSocketSet.add(this);
        userNumber++;
        //保存当前用户名
        this.username=username;
        //获取所有用户
        Set<String> users=new HashSet<>();
        for (WebSocket webSocket : webSocketSet) {
            users.add(webSocket.username);
        }
        //将信息包装好传给客户端
        Map<String,Object> message1=new HashMap<>();
        //用户列表
        message1.put("onlineUsers",users);
        //messageType 1代表上线 2代表下线 3代表在线名单 4代表普通消息
        message1.put("messageType",1);
        //返回用户名
        message1.put("username",username);
        //返回在线人数
        message1.put("number",userNumber);
        //发送给所有用户谁上线了
        sendMessageAll(JSON.toJSONString(message1),this.username);

        //刷新在线列表
        Map<String,Object> message2=new HashMap<>();
        message2.put("messageType", 3);
        //把所有用户放入map2
        message2.put("onlineUsers", users);
        //返回在线人数
        message2.put("number", this.userNumber);
        sendMessageAll(JSON.toJSONString(message2),this.username);
    }

    @OnMessage
    public void OnMessage(String message) throws IOException {
        //前端传来的数据进行转型
        JSONObject object=JSON.parseObject(message);
        //提取数据
        String textMessage = object.getString("message");
        String username = object.getString("username");
        String type = object.getString("type");
        String to = object.getString("tousername");
        //群发
        if (type.equals("群发")){
            Map<String,Object> message3=new HashMap<>();
            message3.put("messageType",4);
            message3.put("username",username);
            message3.put("textMessage",textMessage);
            sendMessageAll(JSON.toJSONString(message3),this.username);
        }else {
            //私发
            Map<String,Object> message4=new HashMap<>();
            message4.put("messageType",4);
            message4.put("username",username);
            message4.put("testMessage",textMessage);
            sendToOne(JSON.toJSONString(message4),to);
        }
    }

    @OnClose
    public void OnClose() throws IOException {
        //通知所有人某用户下线
        webSocketSet.remove(this);
        userNumber--;
        Map<String,Object> message=new HashMap<>();
        message.put("messageType",2);
        message.put("username",this.username);
        sendMessageAll(JSON.toJSONString(message),this.username);

        //更改在线列表
        Set<String> users=new HashSet<>();
        for (WebSocket webSocket : webSocketSet) {
            users.add(webSocket.username);
        }
        Map<String,Object> message2=new HashMap<>();
        message2.put("onlineUsers",users);
        message2.put("messageType",3);
        message2.put("number",userNumber);
        sendMessageAll(JSON.toJSONString(message2),this.username);
    }


    /**
     * 发消息给所有人
     * @param message
     * @param from
     * @throws IOException
     */
    public void sendMessageAll(String message,String from) throws IOException {
        for (WebSocket webSocket : webSocketSet) {
            webSocket.session.getBasicRemote().sendText(message);
        }
    }

    /**
     * 发消息给指定人
     * @param message
     * @param to
     * @throws IOException
     */
    public void sendToOne(String message,String to) throws IOException {
        for (WebSocket webSocket : webSocketSet) {
            if (webSocket.username.equals(to)){
                webSocket.session.getBasicRemote().sendText(message);
                break;
            }
        }
    }



}
