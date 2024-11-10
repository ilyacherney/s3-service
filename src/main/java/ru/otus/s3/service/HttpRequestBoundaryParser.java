package ru.otus.s3.service;

public class HttpRequestBoundaryParser {
    public static String parse(String rawRequest) {
        int startIndex = rawRequest.indexOf("boundary") + 9;
        int endIndex = findEndIndex(rawRequest, startIndex);
        String boundary = rawRequest.substring(startIndex, endIndex);
        System.out.println("BOUNDARY: " + boundary);
        return "--" + boundary;
    }

    private static int findEndIndex(String rawRequest, int startIndex) {
        int endIndex = startIndex;
        char[] rawRequestAsArray = rawRequest.toCharArray();
        for (int i = startIndex; i <= rawRequest.length(); i++) {
            if (rawRequestAsArray[i] != '\r') {
                endIndex++;
            } else {
                break;
            }
        }
        return endIndex;
    }
}
