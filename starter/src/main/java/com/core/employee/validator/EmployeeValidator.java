package com.core.employee.validator;

import com.core.employee.entity.Employee;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
@Component
public class EmployeeValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Employee.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Employee employee=(Employee) target;

        ValidationUtils.rejectIfEmpty(errors,"name","name.empty");
        ValidationUtils.rejectIfEmpty(errors,"department","department.empty");
        if (employee.getSalary() < 0) {
            errors.rejectValue(
                    "salary",
                    "salary.negative",
                    "Salary cannot be negative"
            );
        }
    }
}
