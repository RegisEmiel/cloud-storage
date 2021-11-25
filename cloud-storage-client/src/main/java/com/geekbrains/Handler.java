package com.geekbrains;

import com.geekbrains.messages.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class Handler extends SimpleChannelInboundHandler<Message> {
    private MainController mainController;
    private SocketChannel sc;

    public Handler() {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        sc = (SocketChannel) ctx.channel();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        switch (msg.getType()) {

            case LIST_FILES:
                ListFileMessage lr = (ListFileMessage) msg;
                break;
            case ACCEPT_COMPLETE_FILE:
                saveFile(msg);
                break;
            case NEXT_PART_TO_CLIENT:
                sendNextPart(msg);
                break;
            case ACCEPT_FILE_PART:
                saveFilePart(msg);
                break;
        }
    }

    @SneakyThrows
    private void sendNextPart(Message message) {
        NextFilePartToClientMessages msg = (NextFilePartToClientMessages) message;

        Path path = Paths.get(mainController.getClientDir().toString(), msg.getFileName());
        sc.writeAndFlush(mainController.getNetwork().getFileUtils().sendNextPart(msg, path));
    }


    @SneakyThrows
    private void saveFilePart(Message message) {
        PartFileMessage msg = (PartFileMessage) message;

        Path savePath = Paths.get(mainController.getClientDir().toString(), msg.getFileName());
        int part = msg.getPart();
        int allParts = msg.getAllParts();

        long startByte = msg.getStartByte();
        int lastPart = msg.getLastPartSize();

        if (mainController.getNetwork().getFileUtils().savePart(savePath, msg.getData())) {
            if (part == allParts) {
                // TODO
                // Обновить список файлов
                return;
            }
            sc.writeAndFlush(new NextPartToServerMessages(msg.getFileName(), allParts, part, startByte, lastPart));
        }
    }

    @SneakyThrows
    private void saveFile(Message message) {
        AcceptCompleteFileMessage msg = (AcceptCompleteFileMessage) message;
        Path savePath = Paths.get(mainController.getClientDir().toString(), msg.getFileName());
        mainController.getNetwork().getFileUtils().saveFile(savePath, msg.getData());
    }
}
