package xyz.ruhshan.sqsdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private String id;
    private String name;
    private String description;
    private Instant arrivedAt;
}
