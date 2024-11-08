package ru.otus.october.http.server;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileStorage {
    private static final String BASE_DIR = "storage";

    public byte[] getFile(String bucketName, String fileName) throws IOException {
        Path filePath = Paths.get(BASE_DIR, bucketName, fileName);
        return Files.readAllBytes(filePath);
    }

    public void saveFile(String bucketName, String fileName, byte[] fileData) throws IOException {
        Path bucketPath = Paths.get(BASE_DIR, bucketName);
        Files.createDirectories(bucketPath);
        Path filePath = Paths.get(BASE_DIR, bucketName, fileName);
        Files.write(filePath, fileData);
    }

    public void deleteFile(String bucketName, String fileName) throws IOException {
        Path filePath = Paths.get(BASE_DIR, bucketName, fileName);
        Files.deleteIfExists(filePath);
    }
}