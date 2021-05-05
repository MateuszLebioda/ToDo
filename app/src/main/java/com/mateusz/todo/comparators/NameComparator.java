package com.mateusz.todo.comparators;

import com.mateusz.todo.model.ToDo;

import java.util.Comparator;

public class NameComparator implements Comparator<ToDo> {

    @Override
    public int compare(ToDo o1, ToDo o2) {
        if(o1 instanceof ToDo && o2 instanceof ToDo){
            return (o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase()));
        }
        return 0;
    }
}
