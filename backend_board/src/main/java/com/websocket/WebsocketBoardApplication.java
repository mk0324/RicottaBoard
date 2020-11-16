package com.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.aggregation.DateOperators;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
public class WebsocketBoardApplication {

    @PostConstruct
    public void setTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        System.out.println("현재 시각 : " + new Date());
    }

    public static void main(String[] args) {
        SpringApplication.run(WebsocketBoardApplication.class, args);
    } 

}
