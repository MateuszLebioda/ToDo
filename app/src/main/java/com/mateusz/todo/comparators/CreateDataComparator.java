package com.mateusz.todo.comparators;

import com.mateusz.todo.model.ToDo;

import java.util.Comparator;

public class CreateDataComparator implements Comparator<ToDo> {

    @Override
    public int compare(ToDo o1, ToDo o2) {
        if(o1 instanceof ToDo && o2 instanceof ToDo){
            return (o1.getCreated().compareTo(o2.getCreated()));
        }
        return 0;
    }
}
