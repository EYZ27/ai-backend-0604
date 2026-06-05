package com.sesac.aibackend0604.dto;

import com.sesac.aibackend0604.domain.Department;

public record DepartmentResponse(
        Long departmentId,
        String departmentName
) {
    public static DepartmentResponse from(Department department) {
        return new DepartmentResponse(
                department.getId(),
                department.getName()
        );
    }
}
