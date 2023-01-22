package net.javaguide.springboottesting.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguide.springboottesting.model.Employee;
import net.javaguide.springboottesting.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("JUnit test for create(POST) employee REST API")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("email@email.com").build();
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then - verify the output
        response.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @DisplayName("JUnit test for Get All employees REST API")
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeeList() throws Exception {
        // given - precondition or setup
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder()
                .firstName("firstName1")
                .lastName("lastName1")
                .email("email1@email.com").build());
        listOfEmployees.add(Employee.builder()
                .firstName("firstName2")
                .lastName("lastName2")
                .email("email2@email.com").build());
        given(employeeService.getAllEmployees()).willReturn(listOfEmployees);

        // when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(listOfEmployees.size())));
    }

    @DisplayName("JUnit test for Get Employee by id REST API - positive scenario : valid employee id")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        // given - precondition or setup
        Long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("email@email.com").build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        // when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @DisplayName("JUnit test for Get Employee by id REST API - negative scenario : invalid employee id")
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {
        // given - precondition or setup
        Long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("email@email.com").build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        // when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("JUnit test for update(PUT) Employee REST API - positive scenario")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception {
        // given - precondition or setup
        Long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("email@email.com").build();
        Employee updatedEmployee = Employee.builder()
                .firstName("updateFirstName")
                .lastName("updateLastName")
                .email("updateEmail@email.com").build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        response.andExpect(status().isOk()) // 200
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    @DisplayName("JUnit test for update(PUT) Employee REST API - negative scenario")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception {
        // given - precondition or setup
        Long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("email@email.com").build();
        Employee updatedEmployee = Employee.builder()
                .firstName("updateFirstName")
                .lastName("updateLastName")
                .email("updateEmail@email.com").build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        response.andExpect(status().isNotFound()) // 404
                .andDo(print());
    }

    @DisplayName("JUnit test for delete Employee REST API")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
        // given - precondition or setup
        Long employeeId = 1L;
        willDoNothing().given(employeeService).deleteEmployee(employeeId);

        // when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }

}