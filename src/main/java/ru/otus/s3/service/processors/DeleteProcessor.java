package ru.otus.s3.service.processors;

import ru.otus.s3.service.FileStorage;
import ru.otus.s3.service.HttpRequest;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DeleteProcessor implements RequestProcessor {
    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        String bucket = request.getBucket();
        String key = request.getKey();
        if (key != null) {
            FileStorage.deleteFile(bucket, key);
        } else {
            FileStorage.deleteBucket(bucket);
        }

        String response = "" +
                "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                "<html><body><h1>File deleted.</h1><table><tr><td>1</td><td>2</td></tr></table></body></html>";
        output.write(response.getBytes(StandardCharsets.UTF_8));
        output.flush();
    }
}
