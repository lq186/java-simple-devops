package com.lq186.devops.ws;

import org.apache.maven.cli.MavenCli;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lq
 * @date 2020/1/21
 */
@Component
@ServerEndpoint("/compile/progress/{compileId}")
public class CompileProgressWebSocketServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompileProgressWebSocketServer.class);

    private static final ConcurrentHashMap<String, Session> SESSION_MAP = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("compileId") String compileId) {
        LOGGER.info("[onOpen] compileId: {}", compileId);
        SESSION_MAP.put(compileId, session);
    }

    @OnClose
    public void onClose(@PathParam("compileId") String compileId) {
        LOGGER.info("[onClose] compileId: {}", compileId);
        SESSION_MAP.remove(compileId);
    }

    @OnMessage
    public void onMessage(Session session, @PathParam("compileId") String compileId, String message) throws IOException {
        LOGGER.info("[onMessage] compileId: {}, message: {}", compileId, message);
    }

    @OnError
    public void onError(Session session, @PathParam("compileId") String compileId, Throwable throwable) {
        LOGGER.error("[onError] compileId: {}", compileId, throwable);
    }

    /**
     * 向客户端发送信息
     *
     * @param compileId 编译任务ID
     * @param message   编译输出信息
     * @throws IOException
     */
    public static final void sendMessage(String compileId, String message) throws IOException {
        if (SESSION_MAP.contains(compileId)) {
            SESSION_MAP.get(compileId).getBasicRemote().sendText(message);
        } else {
            LOGGER.error("[sendMessage] compileId not exists. compileId: {}", compileId);
        }
    }

    /**
     * 获取编译任务的客户端输出流
     *
     * @param compileId 编译任务ID
     * @return
     * @throws IOException
     */
    public static final OutputStream getOutputStream(String compileId) throws IOException {
        if (SESSION_MAP.contains(compileId)) {
            return SESSION_MAP.get(compileId).getBasicRemote().getSendStream();
        }

        LOGGER.error("[getOutputStream] compileId not exists. compileId: {}", compileId);
        throw new RuntimeException("compileId not exists");
    }

}
