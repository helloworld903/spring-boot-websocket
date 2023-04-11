package com.test.common.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@Component
@ServerEndpoint("/websocket/{userId}")
public class WebSocketUtils {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private Session session;
	private String userId;
	private static CopyOnWriteArraySet<WebSocketUtils> webSockets = new CopyOnWriteArraySet<>();
	private static ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<String, Session>();
	
	@OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") String userId) {
        try {
            this.session = session;
            this.userId = userId;
            webSockets.add(this);
            sessionPool.put(userId, session);
            logger.info("【websocket消息】有新的连接，总数为:" + webSockets.size());
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
	
	@OnClose
    public void onClose() {
        try {
            webSockets.remove(this);
            sessionPool.remove(this.userId);
            logger.info("【websocket消息】连接断开，总数为:" + webSockets.size());
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
	
	@OnMessage
    public void onMessage(String message) {
        logger.info("【websocket消息】收到客户端消息:" + message);
    }
	
	 public void sendAllMessage(String message) {
	        logger.info("【websocket消息】广播消息:" + message);
	        for (WebSocketUtils webSocket : webSockets) {
	            try {
	                if (webSocket.session.isOpen()) {
	                    webSocket.session.getAsyncRemote().sendText(message);
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }
	 
	 public void sendOneMessage(String userId, String message) {
	        Session session = sessionPool.get(userId);
	        if (session != null && session.isOpen()) {
	            try {
	                logger.info("【websocket消息】 单点消息:" + message);
	                session.getAsyncRemote().sendText(message);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }
	 
	 public void sendMoreMessage(String[] userIds, String message) {
	        for (String userId : userIds) {
	            Session session = sessionPool.get(userId);
	            if (session != null && session.isOpen()) {
	                try {
	                    logger.info("【websocket消息】 单点消息:" + message);
	                    session.getAsyncRemote().sendText(message);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	 }
}
