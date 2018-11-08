package com.tea.framework.metadata.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2015-2016
 * NameValuePair
 * Author: 龚健
 * Date: 2016/6/7
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NameValuePair<T> {
    private String name;
    private T value;
}
