package com.services;

import java.util.List;

public interface DataService<T> {
    List<T> getAllByPage(int page, int count);
}
