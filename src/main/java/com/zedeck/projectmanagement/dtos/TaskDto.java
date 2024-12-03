package com.zedeck.projectmanagement.dtos;


import com.zedeck.projectmanagement.models.Project;
import com.zedeck.projectmanagement.models.UserAccount;
import com.zedeck.projectmanagement.utils.Status;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class TaskDto {

    private String name;

    private String description;

    private Long project_id;

    private Long assignee_id;

    private String status;
}
