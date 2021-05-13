package com.mateusz.todo.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mateusz.todo.model.ToDo;

import java.util.List;

public class Mapper {
    public static String mapToDosToText(List<ToDo> todos){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(todos);
    }

    public static ToDo[] mapTextToToDo(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, ToDo[].class);
    }

}
