package xyz.ruhshan.sqsdemo.service;

import xyz.ruhshan.sqsdemo.dto.Task;

public interface QueueService {
    void publishTask(Task task);
}
