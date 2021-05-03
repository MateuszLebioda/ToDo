package com.mateusz.todo.model;

import org.threeten.bp.LocalDateTime;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class ToDo {
    private String name;
    private LocalDateTime created = LocalDateTime.now();
    private LocalDateTime term;
    private boolean done;
    private boolean priority;
    private Attachment attachment;

    public static ToDoBuilder builder() {
        return new CustomToDoBuilder();
    }

    private static class CustomToDoBuilder extends ToDoBuilder {
        @Override
        public ToDo build() {
            created(LocalDateTime.now());
            return super.build();
        }
    }
}
