package com.mateusz.todo.model;

import org.threeten.bp.LocalDateTime;

import java.util.Comparator;
import java.util.Objects;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class ToDo implements Comparable {
    private String name;
    private LocalDateTime created;
    private LocalDateTime term;
    private boolean done;
    private boolean priority;
    private Attachment attachment;

    public static ToDoBuilder builder() {
        return new CustomToDoBuilder();
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof ToDo){
            ToDo toDo = (ToDo)o;
            if(this.priority && toDo.priority){
                return this.name.toLowerCase().compareTo(toDo.name.toLowerCase());
            }else if(this.priority){
                return 1;
            }else if(toDo.priority){
                return 0;
            }else {
                return this.name.toLowerCase().compareTo(toDo.name.toLowerCase());
            }
        }
        return 0;
    }

    private static class CustomToDoBuilder extends ToDoBuilder {
        @Override
        public ToDo build() {
            created(LocalDateTime.now());
            return super.build();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToDo toDo = (ToDo) o;
        return Objects.equals(name, toDo.name) &&
                Objects.equals(created, toDo.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, created);
    }


}
