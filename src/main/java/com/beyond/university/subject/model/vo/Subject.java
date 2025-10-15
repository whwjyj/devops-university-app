package com.beyond.university.subject.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    private String no;

    private String deptNo;

    private String preattendingNo;

    private String name;

    private String type;
}