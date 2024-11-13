package ru.otus.s3.service.processors;

import ru.otus.s3.service.FileStorage;
import ru.otus.s3.service.HttpRequest;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GetProcessor implements RequestProcessor {
    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        File file = new File(FileStorage.BASE_DIR + "/" + request.getBucket() + "/" + request.getKey());
        byte[] fileBytes = Files.readAllBytes(file.toPath());

        String contentType = Files.probeContentType(Paths.get(file.getPath()));
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        String headers = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Content-Disposition: attachment; filename=\"" + file.getName() + "\"\r\n" +
                "Content-Length: " + fileBytes.length + "\r\n" +
                "\r\n";

        output.write(headers.getBytes());
        output.write(fileBytes);
        output.flush();
    }
}
