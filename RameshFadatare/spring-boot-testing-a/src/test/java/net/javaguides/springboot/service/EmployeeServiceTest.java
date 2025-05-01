package net.javaguides.springboot.service;

import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import net.javaguides.springboot.service.impl.EmployeeServiceImpl;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        //below is alternative to annotation approach
        //employeeRepository = Mockito.mock(EmployeeRepository.class);
        //employeeService = new EmployeeServiceImpl(employeeRepository);
         employee = Employee.builder()
                .id(1L)
                .firstName("Bheem")
                .lastName("Shekhar")
                .email("bheem@gmail.com")
                .build();
    }

   //Junit Test for save Employee
   @Test
   public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject(){
       //given - precondition or setup
       given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());

       given(employeeRepository.save(employee)).willReturn(employee);

       //when  - action or behaviour that we are going to test
       Employee savedEmployee= employeeService.saveEmployee(employee);

       //then - verify output
       assertThat(savedEmployee).isNotNull();

   }

    //Junit Test for save Employee with Exception.
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowException(){
        //given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        //when  - action or behaviour that we are going to test
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        //then
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    //Junit Test for Get All Employees
    @Test
    public void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeeList(){
        //given - precondition or setup
        given(employeeRepository.findAll()).willReturn(List.of(employee));

        //when  - action or behaviour that we are going to test
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then - verify output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(1);
    }

    //Junit Test for Get All Employees - Empty
    @Test
    public void givenEmptyEmployeeList_whenGetAllEmployees_thenReturnEmptyEmployeeList(){
        //given - precondition or setup
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        //when  - action or behaviour that we are going to test
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then - verify output
        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);
    }

    //Junit Test for GetEmployeeById method
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject(){
        //given - precondition or setup
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        //when  - action or behaviour that we are going to test
        Employee savedEmployee = employeeService.getEmployeeById(employee.getId()).get();

        //then - verify output
        assertThat(savedEmployee).isNotNull();
    }

    //Junit Test for updateEmployee method
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){
        //given - precondition or setup
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("shekhar@gmail.com");

        //when  - action or behaviour that we are going to test
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        //then - verify output
        assertThat(updatedEmployee.getEmail()).isEqualTo("shekhar@gmail.com");
    }

    //Junit Test for deleteEmployee
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenNothing(){
        //given - precondition or setup
        Long employeeId = 1L;
        willDoNothing().given(employeeRepository).deleteById(employeeId);

        //when  - action or behaviour that we are going to test
        employeeService.deleteEmployee(employeeId);

        //then - verify output
        verify(employeeRepository, times(1)).deleteById(employeeId);

    }
}