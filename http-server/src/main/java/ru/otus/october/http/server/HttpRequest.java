package ru.otus.october.http.server;

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
        this.parse();
        parseHeaders();
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
        if (method == HttpMethod.POST) {
            this.body = rawRequest.substring(rawRequest.indexOf("\r\n\r\n") + 4);
        }
    }

    private void parseHeaders() {
        String rawHeaders = rawRequest.substring(rawRequest.indexOf("\r\n") + 2); // Пропустим первую строку запроса
        String[] lines = rawHeaders.split("\r\n"); // Разделяем на строки по разделителю строк

        headers = new HashMap<>();
        for (String line : lines) {
            int index = line.indexOf(": ");
            if (index != -1) {
                String key = line.substring(0, index).trim(); // Заголовок (до ": ")
                String value = line.substring(index + 2).trim(); // Значение (после ": ")
                headers.put(key, value);
            }
        }

        LOGGER.debug("Accept-Encoding: " + headers.get("Accept-Encoding"));
        LOGGER.debug("Connection: " + headers.get("Connection"));
    }

    public void info() {
        LOGGER.debug(rawRequest);
        LOGGER.info("Method: " + method);
        LOGGER.info("URI: " + uri);
        LOGGER.info("Parameters: " + parameters);
        LOGGER.info("Body: "  + body);
    }
}
