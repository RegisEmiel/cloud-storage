package com.geekbrains;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Server {
    public static void main(String[] args) {
        EventLoopGroup hard = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(hard, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    new ObjectDecoder(8*1024*1024, ClassResolvers.cacheDisabled(null)),
                                    new ObjectEncoder()//,
                                    //new ServerObjectHandler()
                            );
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind(8189).sync();
//            AuthUsers.getInstance().disconnectAll();
            log.debug("Server started");
            channelFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            log.debug("E=", e);
        } finally {
            hard.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
