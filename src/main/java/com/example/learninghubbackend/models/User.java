package com.example.learninghubbackend.models;

import com.example.learninghubbackend.commons.models.ExportData;
import com.example.learninghubbackend.commons.models.Exportable;
import com.example.learninghubbackend.commons.models.ObjectType;
import com.example.learninghubbackend.commons.models.Releasable;
import com.example.learninghubbackend.dtos.responses.user.UserRelease;
import com.example.learninghubbackend.dtos.responses.user.UserReleaseCompact;
import com.example.learninghubbackend.services.user.Gender;
import com.example.learninghubbackend.services.user.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Entity()
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseModel implements Exportable, Releasable<UserRelease, UserReleaseCompact> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String name;

    private String email;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Override
    public String getModelType() {
        return ObjectType.USER.getValue();
    }

    public User(Long id, String username, String password, String name, String email, String phone, Role role, Gender gender, Long createdAt, Long updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.gender = gender;
    }

    public User(String username, String password, String name, String email, String phone, Role role, Gender gender) {
        super();
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.gender = gender;
    }

    public List<GrantedAuthority> getAuthorities() {
        return List.of(role.getAuthority());
    }

    @Override
    public String getLink() {
        return "users/" + getId();
    }

    @Override
    public ExportData export() {
        return new ExportData(getId(), getName(), getModelType(), getLink());
    }

    @Override
    public UserRelease release() {
        UserRelease data = new UserRelease();
        return data.setId(id).setUsername(username).setName(name)
                .setEmail(email).setPhone(phone).setRole(role).setGender(gender)
                .setCreatedAt(getCreatedAt()).setUpdatedAt(getUpdatedAt());
    }

    @Override
    public UserReleaseCompact releaseCompact() {
        UserReleaseCompact data = new UserReleaseCompact();
        return data.setId(id).setUsername(username).setName(name).setEmail(email).setRole(role);
    }
}
