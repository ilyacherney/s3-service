package ru.otus.s3.service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileStorage {
    public static final String BASE_DIR = "storage";

    public static byte[] getFile(String bucketName, String fileName) throws IOException {
        Path filePath = Paths.get(BASE_DIR, bucketName, fileName);
        return Files.readAllBytes(filePath);
    }

    public static void saveFile(String bucketName, String fileName, byte[] fileData) throws IOException {
        createBucket(bucketName);
        Path filePath = Paths.get(BASE_DIR, bucketName, fileName);
        Files.write(filePath, fileData);
    }

    public static void deleteFile(String bucketName, String fileName) throws IOException {
        Path filePath = Paths.get(BASE_DIR, bucketName, fileName);
        Files.deleteIfExists(filePath);
    }

    public static void createBucket(String bucketName) throws IOException {
        Path bucketPath = Paths.get(BASE_DIR, bucketName);
        Files.createDirectories(bucketPath);
    }

    public static void deleteBucket(String bucketName) throws IOException {
        Path bucketPath = Paths.get(BASE_DIR, bucketName);
        Files.deleteIfExists(bucketPath);
    }
}