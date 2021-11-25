package com.geekbrains.utilities;

import com.geekbrains.messages.FilePartMessage;
import com.geekbrains.messages.AcceptCompleteFileMessage;
import io.netty.channel.socket.SocketChannel;


import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtilities {
    private static FileUtilities fileUtilities;

    public final long SIZE_LIMIT = 8 * 1024 * 1024; // Передаем по 8 Мб

    static int partIndex = 0;

    public static FileUtilities getInstance() {
        if (fileUtilities == null) {
            fileUtilities = new FileUtilities();

            return fileUtilities;
        }

        return fileUtilities;
    }

    public void sendFile(Path path, SocketChannel sc) throws IOException {
        if (Files.size(path) > SIZE_LIMIT) {
            sendFileInParts(path, sc);
        } else {
            sc.writeAndFlush(new AcceptCompleteFileMessage(path.getFileName().toString(), Files.readAllBytes(path)));
        }
    }

    public void saveFile(Path path, byte[] data) throws IOException {
        Files.write(path, data, StandardOpenOption.CREATE);
    }

    @SneakyThrows
    public boolean savePart(Path path, byte[] data) throws IOException {
        Files.write(path, data, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        log.debug("save {}", partIndex++);

        return true;
    }

    private void sendFileInParts(Path path, SocketChannel sc) throws IOException {
        int iter = (int) ((Files.size(path) + SIZE_LIMIT) / SIZE_LIMIT);
        int lastPartSize = (int) (Files.size(path) % SIZE_LIMIT);
        String fileName = path.getFileName().toString();
        byte[] data = readBytes(path,0, (int) SIZE_LIMIT);
        FilePartMessage filePartMessage = new FilePartMessage(fileName,iter,1,0,lastPartSize,data);
        sc.writeAndFlush(filePartMessage);
    }

    private byte[] readBytes(Path path, long startByte, int buf_size) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(path.toString(),"r");
        byte[] buf = new byte[buf_size];
        raf.seek(startByte);
        raf.read(buf);
        raf.close();

        return buf;
    }

    public FilePartMessage sendNextPart(FilePartMessage msg, Path path) throws IOException {
        int currentPart = msg.getPart() + 1;
        long currentStartByte = msg.getStartByte() + SIZE_LIMIT;

        if (currentPart==msg.getTotal()){
            byte[] lastPart = readBytes(path, currentStartByte, msg.getLastPartSize());
            return new FilePartMessage(msg.getFileName(),msg.getTotal(), currentPart,currentStartByte,msg.getLastPartSize(), lastPart);
        }
        else {
            byte[] part = readBytes(path, currentStartByte, (int) SIZE_LIMIT);
            return new FilePartMessage(msg.getFileName(),msg.getTotal(), currentPart,currentStartByte,msg.getLastPartSize(), part);
        }
    }
}
