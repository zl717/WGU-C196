package com.example.wguc196;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.Date;

public class Converters {

    @TypeConverter
    public Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

}

