package ru.otus.s3.service;

public class Application {
    public static void main(String[] args) {
        new HttpServer(8190).start();
    }
}
