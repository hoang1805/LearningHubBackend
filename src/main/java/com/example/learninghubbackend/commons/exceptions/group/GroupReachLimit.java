package com.example.learninghubbackend.commons.exceptions.group;

import com.example.learninghubbackend.commons.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class GroupReachLimit extends CustomException {
    public GroupReachLimit() {
        super("You can not join this group because it reaches the limit members.", HttpStatus.BAD_REQUEST.value());
    }
}
