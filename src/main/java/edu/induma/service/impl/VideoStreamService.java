package edu.induma.service.impl;

import edu.induma.handler.AppWebSocketHandler;
import edu.induma.handler.BinWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class VideoStreamService {


    private final AppWebSocketHandler webSocketHandler;
    private final BinWebSocketHandler binWebSocketHandler;

    public void retunImage(String images){
       binWebSocketHandler.sendMessageToAll(images);
    }


}
