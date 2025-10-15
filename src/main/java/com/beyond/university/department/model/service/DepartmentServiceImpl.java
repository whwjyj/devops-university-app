package com.beyond.university.department.model.service;

import com.beyond.university.department.model.mapper.DepartmentMapper;
import com.beyond.university.department.model.vo.Department;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentMapper departmentMapper;

    @Override
    public int getTotalCount(String openYn) {

        return departmentMapper.selectDepartmentsCount(openYn);
    }

    @Override
    public List<Department> getDepartments(int page, int numOfRows, String openYn) {
        /*
        Mybatis에서 페이징 처리

        Mybatis에서는 페이징 처리를 위해 RowBounds라는 클래스를 제공해 준다.
        RowBounds의 객체를 생성할 때 offset과 limit 값을 전달해서 페이징 처리를 쉽게 구현한다.
        (offset 만큼 건너뛰고 limit 만큼 가져온다.)

        offset = 0, limit = 10
          - 0개를 건너뛰고 10개를 가져온다. (1 ~ 10)

        offset = 10, limit = 10
          - 10개를 건너뛰고 10개를 가져온다. (11 ~ 20)

        offset = 20, limit = 10
          - 20개를 건너뛰고 10개를 가져온다. (21 ~ 30)
        */
        int offset = (page - 1) * numOfRows;

        RowBounds rowBounds = new RowBounds(offset, numOfRows);

//        if (true) {
//            throw new RuntimeException("서비스 실행 중 에러 발생");
//        }

        return departmentMapper.selectAll(openYn, rowBounds);
    }

    @Override
    public Optional<Department> getDepartmentByNo(String deptNo) {

        return departmentMapper.selectDepartmentByNo(deptNo);
    }

    @Override
    @Transactional
    public void save(Department department) {
        if (department.getNo() != null) {
            // update
            departmentMapper.updateDepartment(department);
        } else {
            // insert
            departmentMapper.insertDepartment(department);
        }
    }

    @Override
    @Transactional
    public void delete(String deptNo) {

        departmentMapper.deleteDepartment(deptNo);

    }
}