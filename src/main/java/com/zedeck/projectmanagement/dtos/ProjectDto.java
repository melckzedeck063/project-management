package com.zedeck.projectmanagement.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class ProjectDto {
    private String name;
    private String description;
    private Long manager_id;
    private String start_date;
    private String end_date;
    private String uuid;
    private Long id;
}
