package xyz.ruhshan.sqsdemo.service;

import xyz.ruhshan.sqsdemo.dto.Task;

import java.util.Map;

public interface TaskProcessor {
    void process(Task task, Map<String, Object> headers);
}
