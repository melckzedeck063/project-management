package com.zedeck.projectmanagement.dtos;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class EmailDto {

    private String recipient;
    private String subject;
    private String body;
}

