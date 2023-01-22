package net.javaguide.springboottesting.unit.service.impl;

import net.javaguide.springboottesting.exception.ResourceNotFoundException;
import net.javaguide.springboottesting.model.Employee;
import net.javaguide.springboottesting.repository.EmployeeRepository;
import net.javaguide.springboottesting.service.impl.EmployeeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .id(1L)
                .firstName("firstname")
                .lastName("lastname")
                .email("email@email.com").build();
    }

    @DisplayName("JUnit test for saveEmployee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
        // given - precondition or setup
        // email 유효성 체크
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);

        System.out.println(employeeRepository);
        System.out.println(employeeService);

        // when - action or the behavior that we are going test
        Employee savedEmployee = employeeService.saveEmployee(employee);

        System.out.println(savedEmployee);

        // then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("JUnit test for saveEmployee method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException() {
        // given - precondition or setup
        // email 유효성 체크
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));
        // exception 이 발생하므로 다음 명령문인 employeeRepository.save() 로 이동하지 않아 필요하지 않음
//        given(employeeRepository.save(employee)).willReturn(employee);

        System.out.println(employeeRepository);
        System.out.println(employeeService);

        // when - action or the behavior that we are going test
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        // then - verify the output
        // exception 이 발생하므로 다음 명령문인 employeeRepository.save() 로 이동하지 않음
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @DisplayName("JUnit test getAllEmployees method - positive scenario")
    @Test
    public void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeeList() {
        // given - precondition or setup
        Employee employee2 = Employee.builder()
                .id(2L)
                .firstName("firstname2")
                .lastName("lastname2")
                .email("email2@email.com").build();

        given(employeeRepository.findAll()).willReturn(List.of(employee, employee2));

        // when - action or the behavior that we are going test
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then - verify the output
        Assertions.assertThat(employeeList).isNotNull();
        Assertions.assertThat(employeeList.size()).isEqualTo(2);
    }

    @DisplayName("JUnit test getAllEmployees method - negative scenario")
    @Test
    public void givenEmptyEmployeeList_whenGetAllEmployees_thenReturnEmptyEmployeeList() {
        // given - precondition or setup
        Employee employee2 = Employee.builder()
                .id(2L)
                .firstName("firstname2")
                .lastName("lastname2")
                .email("email2@email.com").build();

        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        // when - action or the behavior that we are going test
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then - verify the output
        Assertions.assertThat(employeeList).isEmpty();
        Assertions.assertThat(employeeList.size()).isEqualTo(0);
    }

    @DisplayName("JUnit test for getEmployeeById method")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {
        // given - precondition or setup
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        // when - action or the behavior that we are going test
        Employee savedEmployee = employeeService.getEmployeeById(employee.getId()).get();

        // then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("JUnit test for updateEmployee method")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // given - precondition or setup
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("updatedEmail@email.com");

        // when - action or the behavior that we are going test
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        // then - verify the output
        Assertions.assertThat(updatedEmployee).isNotNull();
        Assertions.assertThat(updatedEmployee.getEmail()).isEqualTo("updatedEmail@email.com");
    }

    @DisplayName("JUnit test for deleteEmployee method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenNothing() {
        // given - precondition or setup
        Long employeeId = 1L;
        willDoNothing().given(employeeRepository).deleteById(employeeId);

        // when - action or the behavior that we are going test
        employeeService.deleteEmployee(employeeId);

        // then - verify the output
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }
}