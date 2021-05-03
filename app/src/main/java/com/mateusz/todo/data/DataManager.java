package com.mateusz.todo.data;

import com.mateusz.todo.model.ToDo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataManager {
    private static DataManager instance;
    private List<ToDo> toDos = new ArrayList<>();

    private DataManager() {
    }

    public static DataManager getInstance(){
        if (instance == null) {
            instance = new DataManager();
            for (int i = 0; i <= 4; i++) {
                instance.addToDo(ToDo.builder().name("Przykładowe zadanie numer " + i).build());
            }
        }
        return instance;
    }

    public List<ToDo> getToDos() {
        Collections.sort(this.toDos, ToDo::compareTo);
        return this.toDos;
    }

    public List<ToDo> addToDo(ToDo toDo){
        this.toDos.add(toDo);
        return getToDos();
    }

    public List<ToDo> removeToDo(ToDo removedToDo){
        List<ToDo> toDos = new ArrayList<>();
        for(ToDo toDo: this.toDos){
            if(!toDo.equals(removedToDo)){
                toDos.add(toDo);
            }
        }
        this.toDos = toDos;
        return getToDos();
    }

}
