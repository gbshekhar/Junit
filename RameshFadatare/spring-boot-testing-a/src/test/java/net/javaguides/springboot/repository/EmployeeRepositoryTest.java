package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.Employee;
import static  org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    EmployeeRepository employeeRepository;

    private  Employee employee;

    @BeforeEach
    public  void setUp(){
        employee = Employee.builder()
                .firstName("Bheem")
                .lastName("shekhar")
                .email("shekhar@gmail.com")
                .build();
    }

    //Junit Test to save Employee
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSaveEmployee(){

        //given - precondition or setup

        //when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.save(employee);

        //then - verify output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    //Junit Test for get all employees
    @Test
    public void givenEmployeesList_whenFindAll_thenEmployeesList(){
        //given - precondition or setup

        Employee employee2 = Employee.builder()
                .firstName("Raj")
                .lastName("shekhar")
                .email("rajshekhar@gmail.com")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee2);

        //when  - action or behaviour that we are going to test
        List<Employee> employeeList = employeeRepository.findAll();

        //then - verify output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    //Junit Test for find by employee id
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject(){
        //given - precondition or setup
        employeeRepository.save(employee);

        //when  - action or behaviour that we are going to test
        Employee employeeDB = employeeRepository.findById(employee.getId()).get();

        //then - verify output
        assertThat(employeeDB).isNotNull();
    }

    //Junit Test for get employee by email
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject(){
        //given - precondition or setup
        employeeRepository.save(employee);

        //when  - action or behaviour that we are going to test
        Employee employeeDB = employeeRepository.findByEmail(employee.getEmail()).get();

        //then - verify output
        assertThat(employeeDB).isNotNull();
    }

    //Junit Test for Update Employee
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){
        //given - precondition or setup
        employeeRepository.save(employee);

        //when  - action or behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setEmail("bheem@gmail.com");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        //then - verify output
        assertThat(updatedEmployee.getEmail()).isEqualTo("bheem@gmail.com");
    }

    //Junit Test for
    @Test
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee(){
        //given - precondition or setup
        employeeRepository.save(employee);

        //when  - action or behaviour that we are going to test
        employeeRepository.delete(employee);
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

        //then - verify output
        assertThat(employeeOptional).isEmpty();
    }

    //Junit Test for custom query using JPQL with index params
    @Test
    public void givenFirstNameAndLastName_whenFindJPQL_thenReturnEmployeeObject(){
        //given - precondition or setup
        employeeRepository.save(employee);
        String firstName = "Bheem";
        String lastName = "shekhar";

        //when  - action or behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByJPQL(firstName, lastName);

        //then - verify output
        assertThat(savedEmployee).isNotNull();
    }

    //Junit Test for custom query using JPQL with Named Params
    @Test
    public void givenFirstNameAndLastName_whenFindJPQLNamedParams_thenReturnEmployeeObject(){
        //given - precondition or setup
        employeeRepository.save(employee);
        String firstName = "Bheem";
        String lastName = "shekhar";

        //when  - action or behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByJPQLNamedParams(firstName, lastName);

        //then - verify output
        assertThat(savedEmployee).isNotNull();
    }

    //Junit Test for custom query using Native SQL with index params
    @Test
    public void givenFirstNameAndLastName_whenFindNativeQuery_thenReturnEmployeeObject(){
        //given - precondition or setup
        employeeRepository.save(employee);
        String firstName = "Bheem";
        String lastName = "shekhar";

        //when  - action or behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByNativeQuery(firstName, lastName);

        //then - verify output
        assertThat(savedEmployee).isNotNull();
    }
}