package xyz.ruhshan.sqsdemo.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.ruhshan.sqsdemo.dto.Task;

@RestController
@Slf4j
public class TaskController {
    @PostMapping("/task")
    public void createTask(@RequestBody Task task){
        log.info("Received task {}", task.toString());
    }
}
