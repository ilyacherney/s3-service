package ru.otus.s3.service;

public class HttpRequestBodyParser {
    final static int NEW_LINE_LENGTH = 2;

    public static String parse(String rawRequest, String boundary) {
        String boundariesContent = extractBoundariesContent(rawRequest, boundary);
        String body = removeHttpHeaders(boundariesContent);
        return body;
    }

    public static String extractBoundariesContent(String rawRequest, String boundary){
        int boundaryLength = boundary.length();
        int bodyFirstBoundaryStartIndex = rawRequest.indexOf(boundary);
        int bodyFirstBoundaryEndIndex = bodyFirstBoundaryStartIndex + boundaryLength;
        int contentStartIndex = bodyFirstBoundaryEndIndex + NEW_LINE_LENGTH;
        int bodyLastBoundaryStartIndex = rawRequest.indexOf(boundary, contentStartIndex);
        int contentEndIndex = bodyLastBoundaryStartIndex - NEW_LINE_LENGTH;
        String boundariesContent = rawRequest.substring(contentStartIndex, contentEndIndex);
        return boundariesContent;
    }

    public static String removeHttpHeaders(String boundariesContent) {
        int start = boundariesContent.indexOf("Content-Type");
        int end = boundariesContent.indexOf("\r\n", start) + 2 * NEW_LINE_LENGTH;
        String body = boundariesContent.substring(end);
        return body;
    }

}
