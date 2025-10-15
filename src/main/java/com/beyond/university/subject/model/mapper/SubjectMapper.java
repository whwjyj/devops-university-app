package com.beyond.university.subject.model.mapper;

import com.beyond.university.subject.model.vo.Subject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface SubjectMapper {

    int selectCountByDeptNo(@Param("deptNo") String deptNo);

    List<Subject> selectAllByDeptNo(@Param("deptNo") String deptNo, RowBounds rowBounds);
}