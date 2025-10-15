package com.beyond.university.subject.model.service;

import com.beyond.university.subject.model.mapper.SubjectMapper;
import com.beyond.university.subject.model.vo.Subject;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    private final SubjectMapper subjectMapper;

    @Override
    public int getCountByDeptNo(String deptNo) {

        return subjectMapper.selectCountByDeptNo(deptNo);
    }

    @Override
    public List<Subject> getSubjectsByDeptNo(String deptNo, int page, int numOfRows) {
        int offset = (page - 1) * numOfRows;
        RowBounds rowBounds = new RowBounds(offset, numOfRows);

        return subjectMapper.selectAllByDeptNo(deptNo, rowBounds);
    }
}