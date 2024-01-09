package com.utils;

import javafx.scene.control.TableColumn;
import javafx.util.StringConverter;

@SuppressWarnings("unchecked")
public class MyStringConverter<Col> extends StringConverter<Col> {
    private final TableColumn<?, Col> column;
    private final ReflectionUtils reflection = new ReflectionUtils();

    public MyStringConverter(TableColumn<?, Col> column) {
        this.column = column;
    }

    @Override
    public String toString(Col object) {
        return object != null ? object.toString() : "";
    }

    @Override
    public Col fromString(String string) {
        if (column != null) {
            Class<?> colType = column.getCellData(0).getClass();
            return (Col) reflection.convertToCorrectType(string, colType);
        }
        return null;
    }
}
