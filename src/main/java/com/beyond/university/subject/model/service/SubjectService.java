package com.beyond.university.subject.model.service;

import com.beyond.university.subject.model.vo.Subject;

import java.util.List;

public interface SubjectService {
    int getCountByDeptNo(String deptNo);

    List<Subject> getSubjectsByDeptNo(String deptNo, int page, int numOfRows);
}