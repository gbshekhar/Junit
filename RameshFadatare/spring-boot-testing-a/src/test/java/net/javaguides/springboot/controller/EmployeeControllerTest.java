package net.javaguides.springboot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.service.EmployeeService;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    //Junit Test for
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() throws Exception {
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Bheem")
                .lastName("Shekhar")
                .email("bheem@gmail.com")
                .build();
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocationOnMock -> invocationOnMock.getArgument(0)));

        //when  - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then - verify output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    //Junit Test for
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeeList() throws Exception {
        //given - precondition or setup
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(Employee.builder().firstName("bheem").lastName("shekhar").email("bheem@gmail.com").build());
        given(employeeService.getAllEmployees()).willReturn(employeeList);

        //when  - action or behaviour that we are going to test
        ResultActions response =  mockMvc.perform(get("/api/employees"));

        //then - verify output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(employeeList.size())));

    }

    //Positive scenario - valid Employee Id
    //Junit Test for Get Employee By Id REST API
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        //given - precondition or setup
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Bheem")
                .lastName("Shekhar")
                .email("bheem@gmail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        //when  - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //then - verify output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    //Negative scenario - Invalid Employee Id
    //Junit Test for Get Employee By Id REST API
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {
        //given - precondition or setup
        long employeeId = 1L;
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        //when  - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //then - verify output
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    //Junit Test for Update Employee REST API - Positive Scenario
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {
        //given - precondition or setup
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Bheem")
                .lastName("Shekhar")
                .email("bheem@gmail.com")
                .build();

        Employee updateEmployee = Employee.builder()
                .firstName("Shekhar")
                .lastName("Bheem")
                .email("golla@gmail.com")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //when  - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployee)));

        //then - verify output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updateEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updateEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updateEmployee.getEmail())));
    }

    //Junit Test for Update Employee REST API - Negative Scenario
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnNotFound() throws Exception {
        //given - precondition or setup
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Bheem")
                .lastName("Shekhar")
                .email("bheem@gmail.com")
                .build();

        Employee updateEmployee = Employee.builder()
                .firstName("Shekhar")
                .lastName("Bheem")
                .email("golla@gmail.com")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        //when  - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployee)));

        //then - verify output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    //Junit Test for Delete Employee
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnStatusCodeSuccess() throws Exception {
        //given - precondition or setup
        long employeeId = 1L;
        willDoNothing().given(employeeService).deleteEmployee(employeeId);

        //when  - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));

        //then - verify output
        response.andExpect(status().isOk())
                .andDo(print());

    }
}
