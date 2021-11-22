package com.geekbrains;

import com.geekbrains.filehandlers.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


import io.netty.channel.socket.SocketChannel;
import lombok.SneakyThrows;


import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Handler extends SimpleChannelInboundHandler<Message> {
    private SocketChannel sc;
    private Path serverDir;
    private Path currentDir;
    private FileUtilities fileUtilities;
    private String login;
    private ChannelHandlerContext ctx;
    private WatchService watchService;
    private WatchKey register;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        sc = (SocketChannel) ctx.channel();
        this.ctx = ctx;

        fileUtilities = FileUtilities.getInstance();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        currentDir = null;
        serverDir = null;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {

        switch (msg.getType()) {

            case LIST_FILES:
                listFiles(ctx, currentDir);
                break;
            case CREATE_DIRECTORY:
                createDir(msg);
                break;
            case SENDING_FILE:
                saveFullFile(msg);
                break;
            case SEND_FILE:
                sendFile(msg);
                break;
            case FILE_PART:
                saveFilePart(msg);
                break;
            case NEXT_PART_UPLOAD:
                sendNextPart(msg);
                break;
            case FOLDER:
                listFolder(msg);
                break;
            case DELETE_FILE:
                deleteFile(msg);
                break;
        }
    }

    @SneakyThrows
    private void createDir(Message message) {
        CreateDirectoryMessage msg = (CreateDirectoryMessage) message;
        Path createDirPath = Paths.get(currentDir.toAbsolutePath().toString(), msg.getDirName());
        Files.createDirectory(createDirPath);
    }

    @SneakyThrows
    private void deleteFile(Message message) {
        DeleteMessage msg = (DeleteMessage) message;
        Path deletePath = Paths.get(currentDir.toAbsolutePath().toString(),msg.getFileName());
        Files.delete(deletePath);
    }

    @SneakyThrows
    private void listFolder(Message msg) {
        register.cancel();
        FolderMessage fr = (FolderMessage) msg;
        if (fr.getFolderName().equals("/up")) {
            currentDir = currentDir.getParent();
        } else {
            currentDir = Paths.get(currentDir.toAbsolutePath().toString(), fr.getFolderName());
        }
        listFiles(ctx, currentDir);
        register = currentDir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
    }



    private void sendNextPart(Message message) throws IOException {
        NextPartUploadMessage msg = (NextPartUploadMessage) message;
        Path path = Paths.get(currentDir.toString(), msg.getFileName());
        sc.writeAndFlush(fileUtilities.sendNextPart(msg, path));
    }

    private void saveFilePart(Message message) {
        //FilePart msg = (FilePart) message;
        FilePartMessage msg = (FilePartMessage) message;
        Path savePath = Paths.get(currentDir.toString(), msg.getFileName());
        int part = msg.getPart();
        int allParts = msg.getTotal();
        long startByte = msg.getStartByte();
        int lastPart = msg.getLastPartSize();

        try {
            if (fileUtilities.savePart(savePath, msg.getData())) {
                if (part == allParts) {
                    return;
                }

                //sc.writeAndFlush(new NextFilePartToClient(msg.getFileName(), allParts, part, startByte, lastPart));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendFile(Message message) throws IOException {
        SendingFileMessage msg = (SendingFileMessage) message;
        Path savePath = Paths.get(currentDir.toString(), msg.getFileName());
        if (Files.exists(savePath)) {
            fileUtilities.sendFile(savePath, sc);
        }
    }

    private void saveFullFile(Message message) throws IOException {
        SendingFileMessage msg = (SendingFileMessage) message;
        Path savePath = Paths.get(currentDir.toString(), msg.getFileName());
        fileUtilities.saveFile(savePath, msg.getData());
    }

    private void listFiles(ChannelHandlerContext sc, Path dir) throws IOException {
        List<FileDescriptions> collect = Files
                .list(dir)
                .map(FileDescriptions::new)
                .collect(Collectors.toList());
        if (currentDir.toAbsolutePath().equals(serverDir.toAbsolutePath())) {
            sc.writeAndFlush(new ListFileMessage(collect, true));
        } else {
            sc.writeAndFlush(new ListFileMessage(collect));
        }
    }

    private void watcherDir() throws Exception {
        watchService = FileSystems.getDefault().newWatchService();
        Thread watchServe = new Thread(() -> {
            try {
                while (true) {
                    log.debug("Wait events...");
                    WatchKey watchKey = watchService.take();
                    List<WatchEvent<?>> watchEvents = watchKey.pollEvents();
                    for (WatchEvent<?> watchEvent : watchEvents) {
                        log.debug("1 - {}, 2- {}", watchEvent.context(), watchEvent.kind());
                    }
                    listFiles(ctx, currentDir);
                    watchKey.reset();
                }
            } catch (Exception e) {
                log.debug("E=", e);
            }
        });
        watchServe.setDaemon(true);
        watchServe.start();

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //  ctx.close();
        log.debug("E serv = ", cause);
    }
}
