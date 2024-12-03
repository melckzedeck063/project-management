package com.zedeck.projectmanagement.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "projects")
public class Project extends BaseEntity implements Serializable {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    private UserAccount manager_id;

    @Column(name = "start_date")
    private String start_date;

    @Column(name = "end_date")
    private String end_date;
}
