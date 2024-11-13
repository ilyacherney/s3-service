package ru.otus.s3.service;

import ru.otus.s3.service.processors.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Dispatcher {
    private Map<String, RequestProcessor> processors;
    private RequestProcessor defaultNotFoundProcessor;
    private RequestProcessor defaultInternalServerErrorProcessor;
    private RequestProcessor defaultBadRequestProcessor;


    public Dispatcher() {
        this.processors = new HashMap<>();
//        this.processors.put("GET /", new HelloWorldProcessor());
        this.processors.put("GET", new GetProcessor());
        this.processors.put("PUT", new PutProcessor());
        this.processors.put("POST", new PostProcessor());
        this.processors.put("DELETE", new DeleteProcessor());
        this.defaultNotFoundProcessor = new DefaultNotFoundProcessor();
        this.defaultInternalServerErrorProcessor = new DefaultInternalServerErrorProcessor();
        this.defaultBadRequestProcessor = new DefaultBadRequestProcessor();
    }

    public void execute(HttpRequest request, OutputStream out) throws IOException {
        try {
            processors.get(request.getMethod().toString()).execute(request, out);
        } catch (BadRequestException e) {
            request.setException(e);
            defaultBadRequestProcessor.execute(request, out);
        } catch (Exception e) {
            e.printStackTrace();
            defaultInternalServerErrorProcessor.execute(request, out);
        }
    }
}
