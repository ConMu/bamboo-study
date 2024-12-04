package com.conmu.handler;

import io.netty.handler.codec.http.websocketx.*;

public interface WebSocketFrameListener {

    void onTextMessage(TextWebSocketFrame msg);

    void onBinaryMessage(BinaryWebSocketFrame msg);

    void onPingMessage(PingWebSocketFrame msg);

    void onPongMessage(PongWebSocketFrame msg);

    void onCloseMessage(CloseWebSocketFrame msg);

}
