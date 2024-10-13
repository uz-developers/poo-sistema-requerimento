package com.exemplo.services;

import java.util.List;
import java.util.HashMap;;

public abstract class Service {
    abstract public void create(HashMap<String, String> data);
    abstract public Boolean update(HashMap<String, String> data);
    abstract public Boolean delete(HashMap<String, String> data);
    abstract public List<?> getAll();
}
