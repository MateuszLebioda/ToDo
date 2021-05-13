package com.mateusz.todo.model;

import android.graphics.Bitmap;

import org.threeten.bp.LocalDateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ToDo implements Comparable, Serializable {

    private static int idSequence = 0;

    private int id;
    private String name;
    private LocalDateTime created;
    private LocalDateTime term;
    private boolean done;
    private boolean priority;
    @Builder.Default
    private List<Bitmap> attachment = new ArrayList<>();

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
                return -1;
            }else if(toDo.priority){
                return 1;
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
            id(++idSequence);
            return super.build();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToDo toDo = (ToDo) o;
        return done == toDo.done &&
                priority == toDo.priority &&
                Objects.equals(name, toDo.name) &&
                Objects.equals(created, toDo.created) &&
                Objects.equals(term, toDo.term);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, created, term, done, priority, attachment);
    }
}
