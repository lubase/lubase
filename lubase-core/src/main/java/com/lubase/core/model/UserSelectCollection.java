package com.lubase.core.model;

import lombok.Data;

import java.util.List;

@Data
public class UserSelectCollection {
    List<SelectUserModel> data;
    private int totalCount;
}
