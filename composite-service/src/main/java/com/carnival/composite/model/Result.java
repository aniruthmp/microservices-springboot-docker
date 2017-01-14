package com.carnival.composite.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a.c.parthasarathy on 10/18/16.
 */
@Data
public class Result {
    int totalPages;
    long totalRecords;

    List<Object> result = new ArrayList<Object>();
}
