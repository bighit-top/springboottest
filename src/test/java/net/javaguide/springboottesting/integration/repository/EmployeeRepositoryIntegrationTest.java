package net.javaguide.springboottesting.integration.repository;

import net.javaguide.springboottesting.model.Employee;
import net.javaguide.springboottesting.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeRepositoryIntegrationTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .firstName("firstname")
                .lastName("lastname")
                .email("email@email.com").build();

        employeeRepository.deleteAll();
    }

    @DisplayName("JUnit test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        // given - precondition or setup
        // when - action or the behavior that we are going test
        Employee savedEmployee = employeeRepository.save(employee);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    @DisplayName("JUnit test for all employees operation")
    @Test
    public void givenEmployees_whenFindAll_thenEmployeeList() {
        // given - precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("firstname1")
                .lastName("lastname1")
                .email("email1@email.com").build();

        employeeRepository.save(employee);
        employeeRepository.save(employee1);

        // when - action or the behavior that we are going test
        List<Employee> employeeList = employeeRepository.findAll();

        // then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).size().isEqualTo(2);
    }

    @DisplayName("JUnit test for get employee by id operation")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployee() {
        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behavior that we are going test
        Employee employeeDB = employeeRepository.findById(employee.getId()).get();

        // then - verify the output
        assertThat(employeeDB).isNotNull();
    }

    @DisplayName("JUnit test get employee by email operation")
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployee() {
        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behavior that we are going test
        // findByFiled()
        Employee employeeDB = employeeRepository.findByEmail(employee.getEmail()).get();

        // then - verify the output
        assertThat(employeeDB).isNotNull();
    }

    @DisplayName("JUnit test for update employee operation")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behavior that we are going test
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setFirstName("updateFirstName");
        savedEmployee.setEmail("updated@email.com");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        // then - verify the output
        assertThat(updatedEmployee.getFirstName()).isEqualTo("updateFirstName");
        assertThat(updatedEmployee.getEmail()).isEqualTo("updated@email.com");
    }

    @DisplayName("JUnit test for delete employee operation")
    @Test
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee() {
        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behavior that we are going test
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

        // then - verify the output
        assertThat(employeeOptional).isEmpty();
    }

    @DisplayName("JUnit test for custom query using JPQL index")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployee() {
        // given - precondition or setup
        employeeRepository.save(employee);
        String firstName = "firstname";
        String lastName = "lastname";

        // when - action or the behavior that we are going test
        Employee savedEmployee = employeeRepository.findByJPQL(firstName, lastName);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("JUnit test for custom query using JPQL Named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployee() {
        // given - precondition or setup
        employeeRepository.save(employee);
        String firstName = "firstname";
        String lastName = "lastname";

        // when - action or the behavior that we are going test
        Employee savedEmployee = employeeRepository.findByJPQLNamedParams(firstName, lastName);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("JUnit test for custom query using native SQL with index params")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject() {
        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behavior that we are going test
        Employee savedEmployee = employeeRepository.findByNativeSQL(employee.getFirstName(), employee.getLastName());

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("JUnit test for custom query using native SQL with named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQLNamedParams_thenReturnEmployeeObject() {
        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behavior that we are going test
        Employee savedEmployee = employeeRepository.findByNativeSQLNamedParams(employee.getFirstName(), employee.getLastName());

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }
}