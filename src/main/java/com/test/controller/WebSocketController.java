package com.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.common.utils.WebSocketUtils;

@Controller
@RequestMapping("/websocket")
public class WebSocketController {
	@Autowired
	private WebSocketUtils webSocketUtils;
	
	@RequestMapping("")
	public String toHome() {
		return "websocket";
	}
	
	@ResponseBody
	@GetMapping( "/sendTestMessage/{message}")
    public String sendTestMessage(@PathVariable String message) {
        try {
            webSocketUtils.sendAllMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }
}
