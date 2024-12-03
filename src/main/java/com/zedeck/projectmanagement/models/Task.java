package com.zedeck.projectmanagement.models;

import com.zedeck.projectmanagement.utils.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "tasks")
public class Task extends BaseEntity implements Serializable {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "projectId", referencedColumnName = "id")
    private Project project_id;

    @ManyToOne
    @JoinColumn(name = "assigneeId", referencedColumnName = "id")
    private UserAccount assignee_id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
}
