package com.example.learninghubbackend.services.group;

import com.example.learninghubbackend.commons.exceptions.InvalidField;
import com.example.learninghubbackend.dtos.requests.group.CreateGroupRequest;
import com.example.learninghubbackend.models.group.Group;
import com.example.learninghubbackend.utils.ReaderUtil;
import org.springframework.stereotype.Component;

@Component
public class GroupReader {
    public void read(Group group, CreateGroupRequest request) {
        readName(group, request.getName());
        readDescription(group, request.getDescription());
        readScope(group, request.getScope());
        readRegistrationPolicy(group, request.getRegistrationPolicy());
    }

    private void readName(Group group, String name) {
        if (!ReaderUtil.isValidString(name)) {
            throw new InvalidField("name");
        }

        group.setName(name);
    }

    private void readDescription(Group group, String description) {
        group.setDescription(description);
    }

    private void readScope(Group group, Scope scope) {
        group.setScope(scope);
    }

    private void readRegistrationPolicy(Group group, RegistrationPolicy registrationPolicy) {
        if (registrationPolicy == null) {
            throw new InvalidField("registration_policy");
        }

        if (group.getScope() == Scope.PUBLIC) {
            if (registrationPolicy != RegistrationPolicy.OPEN && registrationPolicy != RegistrationPolicy.REQUEST_APPROVAL) {
                throw new InvalidField("registration_policy");
            }

            group.setRegistrationPolicy(registrationPolicy);
        } else {
            if (registrationPolicy != RegistrationPolicy.INVITE_ONLY && registrationPolicy != RegistrationPolicy.TOKEN_BASED) {
                throw new InvalidField("registration_policy");
            }

            group.setRegistrationPolicy(RegistrationPolicy.REQUEST_APPROVAL);
        }
    }
}
