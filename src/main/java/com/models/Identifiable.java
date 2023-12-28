package com.models;

import java.io.Serializable;

public interface Identifiable<T, ID extends Serializable> extends Serializable {
    ID getId();
}
