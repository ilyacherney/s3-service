package com.flamexander.cloud.box.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ProtoFileSender {
    public static void sendFile(Path path, Channel channel, ChannelFutureListener finishListener) throws IOException {
        FileRegion region = new DefaultFileRegion(path.toFile(), 0, Files.size(path));
        byte[] filenameBytes = path.getFileName().toString().getBytes(StandardCharsets.UTF_8);
        // 1 + 4 + filenameBytes.length + 8 -> SIGNAL_BYTE FILENAME_LENGTH(int) + FILENAME + FILE_LENGTH(long)
        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer(1 + 4 + filenameBytes.length + 8);
        buf.writeByte(CloudBoxCommandsList.FILE_SIGNAL_BYTE);
        buf.writeInt(filenameBytes.length);
        buf.writeBytes(filenameBytes);
        buf.writeLong(Files.size(path));
        channel.writeAndFlush(buf);

        ChannelFuture transferOperationFuture = channel.writeAndFlush(region);
        if (finishListener != null) {
            transferOperationFuture.addListener(finishListener);
        }
    }

    public static void sendFileAsHttpPost(Path path, Channel channel, String bucket, String key) throws IOException {
        byte[] fileContent = Files.readAllBytes(path); // Чтение файла в массив байтов
        ByteBuf content = Unpooled.wrappedBuffer(fileContent); // Упаковка содержимого файла в ByteBuf

        // Создание HTTP запроса POST с URI "/upload"
        FullHttpRequest request = new DefaultFullHttpRequest(
//                HttpVersion.HTTP_1_1, HttpMethod.POST, "/" + bucket + "/" + key, content);
                HttpVersion.HTTP_1_1, HttpMethod.POST, "/" + bucket + "/" + key, content);

        // Установка заголовков
        request.headers().set(HttpHeaderNames.HOST, "localhost");
        request.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/octet-stream"); // MIME тип
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
        request.headers().set("Filename", path.getFileName().toString()); // Имя файла в заголовке

        // Отправка HTTP запроса через канал
        channel.writeAndFlush(request);
    }
}