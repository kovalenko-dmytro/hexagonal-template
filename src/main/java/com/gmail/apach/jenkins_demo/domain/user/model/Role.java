package com.gmail.apach.jenkins_demo.domain.user.model;

import com.gmail.apach.jenkins_demo.domain.common.constant.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Role implements Serializable {

    @Serial
    private static final long serialVersionUID = 1234567L;

    private String roleId;
    private RoleType role;
}
