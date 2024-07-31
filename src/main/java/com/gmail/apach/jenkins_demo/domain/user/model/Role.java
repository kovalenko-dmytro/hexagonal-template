package com.gmail.apach.jenkins_demo.domain.user.model;

import com.gmail.apach.jenkins_demo.domain.common.constant.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Role {

    private String roleId;
    private RoleType role;
}
