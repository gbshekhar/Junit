package net.javaguides.springboot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
//@Testcontainers
public class EmployeeControllerIntgTestWithTestContainer extends  AbstractBaseTestContainer{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        employeeRepository.deleteAll();
    }

    @DisplayName("Integration Testing: SaveEmployee")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() throws Exception {
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Bheem")
                .lastName("Shekhar")
                .email("bheem@gmail.com")
                .build();

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

    @DisplayName("Integration Testing: GetAllEmployees")
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeeList() throws Exception {
        //given - precondition or setup
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(Employee.builder().firstName("bheem").lastName("shekhar").email("bheem@gmail.com").build());
        employeeRepository.saveAll(employeeList);

        //when  - action or behaviour that we are going to test
        ResultActions response =  mockMvc.perform(get("/api/employees"));

        //then - verify output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(employeeList.size())));

    }

    @DisplayName("Intg. Test for Get Employee By Id REST API")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Bheem")
                .lastName("Shekhar")
                .email("bheem@gmail.com")
                .build();
        employeeRepository.save(employee);

        //when  - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));

        //then - verify output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    //Negative scenario - Invalid Employee Id
    @DisplayName("Get Employee By Id REST API")
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {
        //given - precondition or setup
        long employeeId = 1L;

        //when  - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //then - verify output
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    @DisplayName("Junit Test for Update Employee REST API - Positive Scenario")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {
        //given - precondition or setup
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Bheem")
                .lastName("Shekhar")
                .email("bheem@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        Employee updateEmployee = Employee.builder()
                .firstName("Shekhar")
                .lastName("Bheem")
                .email("golla@gmail.com")
                .build();

        //when  - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployee)));

        //then - verify output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updateEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updateEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updateEmployee.getEmail())));
    }

    @DisplayName("Junit Test for Update Employee REST API - Negative Scenario")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnNotFound() throws Exception {
        //given - precondition or setup
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Bheem")
                .lastName("Shekhar")
                .email("bheem@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        Employee updateEmployee = Employee.builder()
                .firstName("Shekhar")
                .lastName("Bheem")
                .email("golla@gmail.com")
                .build();

        //when  - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployee)));

        //then - verify output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("Junit Test for Delete Employee")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnStatusCodeSuccess() throws Exception {
        //given - precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstName("Bheem")
                .lastName("Shekhar")
                .email("bheem@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        //when  - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", savedEmployee.getId()));

        //then - verify output
        response.andExpect(status().isOk())
                .andDo(print());

    }
}
