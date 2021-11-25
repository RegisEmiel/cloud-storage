package com.geekbrains;

import com.geekbrains.messages.Message;
import com.geekbrains.messages.SendFileMessage;
import com.geekbrains.utilities.FileUtilities;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Path;

@Slf4j
public class NetChanel {
    private final FileUtilities fileUtilities = FileUtilities.getInstance();
    private SocketChannel socketChannel;


    public NetChanel() {
    }

    public FileUtilities getFileUtilities() {
        return fileUtilities;
    }

    public void startConnectionAuth() {
        Thread connection = new Thread(() -> {
            EventLoopGroup worker = new NioEventLoopGroup();

            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(worker)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                socketChannel = ch;
                                ch.pipeline().addLast(
                                        new ObjectDecoder(1024 * 1024 * 2, ClassResolvers.cacheDisabled(null)),
                                        new ObjectEncoder(),
//                                        new ClientObjectHandler(authController)
                                        new Handler()
                                );
                            }
                        });
                ChannelFuture channelFuture = bootstrap.connect("localhost", 8189).sync();
                channelFuture.channel().closeFuture().sync();

            } catch (Exception e) {
                log.debug("E = ", e);
            } finally {

                worker.shutdownGracefully();
            }
        });
        connection.setDaemon(true);
        connection.start();

    }


    public void close() {
        socketChannel.close();
    }

    public void sendFile(Path path) throws IOException {
        fileUtilities.sendFile(path, socketChannel);
    }

    public void sendDownloadRequest(String fileName) {
        socketChannel.writeAndFlush(new SendFileMessage(fileName));

    }

    public void send(Message message) {
        socketChannel.writeAndFlush(message);
    }

    public FileUtilities getFileUtils() {
        return fileUtilities;
    }
}
