package com.sesac.aibackend0604.dto;

import com.sesac.aibackend0604.domain.Employee;

import java.time.LocalDateTime;

public record EmployeeResponse(
        Long id,
        String name,
        String position,
        Long departmentId,
        String departmentName,
        LocalDateTime createdAt
) {
    public static EmployeeResponse from(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getPosition(),
                employee.getDepartment().getId(),
                employee.getDepartment().getName(),
                employee.getCreatedAt()
        );
    }
}
