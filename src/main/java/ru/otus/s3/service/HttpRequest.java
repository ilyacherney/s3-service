package ru.otus.s3.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class HttpRequest {
    private String rawRequest;
    private HttpMethod method;
    private String uri;
    private Map<String, String> parameters;
    private String body;
    private Exception exception;
    private static final Logger LOGGER = LogManager.getLogger(HttpRequest.class.getName());
    private Map<String, String> headers;
    private String bucket;
    private String key;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public String getUri() {
        return uri;
    }

    public String getRoutingKey() {
        return method + " " + uri;
    }

    public String getBody() {
        return body;
    }

    public HttpRequest(String rawRequest) {
        this.rawRequest = rawRequest;
        parseHeaders();
        this.parse();
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public boolean containsParameter(String key) {
        return parameters.containsKey(key);
    }

    private void parse() {
        int startIndex = rawRequest.indexOf(' ');
        int endIndex = rawRequest.indexOf(' ', startIndex + 1);
        uri = rawRequest.substring(startIndex + 1, endIndex);

        if (uri.indexOf('/') != uri.lastIndexOf('/')) {
            bucket = uri.substring(1, uri.indexOf('/', 1));
            key = uri.substring(uri.lastIndexOf("/") + 1);
        } else {
            bucket = uri.substring(1);
        }
        method = HttpMethod.valueOf(rawRequest.substring(0, startIndex));
        parameters = new HashMap<>();
        if (uri.contains("?")) {
            String[] elements = uri.split("[?]");
            uri = elements[0];
            String[] keysValues = elements[1].split("[&]");
            for (String o : keysValues) {
                String[] keyValue = o.split("=");
                parameters.put(keyValue[0], keyValue[1]);
            }
        }
        if(method == HttpMethod.PUT) {
            String boundary = HttpRequestBoundaryParser.parse(rawRequest);
            this.body = HttpRequestBodyParser.parse(rawRequest, boundary);
        }
    }

    private void parseHeaders() {
        String rawHeaders = rawRequest.substring(rawRequest.indexOf("\r\n") + 2);
        String[] lines = rawHeaders.split("\r\n");

        headers = new HashMap<>();
        for (String line : lines) {
            int index = line.indexOf(": ");
            if (index != -1) {
                String key = line.substring(0, index).trim();
                String value = line.substring(index + 2).trim();
                headers.put(key, value);
            }
        }
    }

    public String getBucket() {
        return bucket;
    }

    public String getKey() {
        return key;
    }

    public HttpMethod getMethod() {
        return method;
    }
}
