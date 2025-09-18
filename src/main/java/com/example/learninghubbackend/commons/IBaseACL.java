package com.example.learninghubbackend.commons;

import com.example.learninghubbackend.models.User;

public interface IBaseACL<T> {
    boolean canCreate();

    boolean canDelete(T object);

    boolean canEdit(T object);

    boolean canView(T object);
}
