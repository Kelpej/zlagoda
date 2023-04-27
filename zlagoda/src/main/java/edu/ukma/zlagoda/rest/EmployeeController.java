package edu.ukma.zlagoda.rest;

import edu.ukma.zlagoda.dto.CreateEmployeeRequest;
import edu.ukma.zlagoda.entities.Employee;
import edu.ukma.zlagoda.entities.EmployeeRole;
import edu.ukma.zlagoda.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    @GetMapping("/me")
    public Employee role() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new Exception("Unauthenticated");
        }

        if (authentication.getAuthorities().contains(EmployeeRole.ADMIN)) {
            return Employee.builder()
                    .name("ADMIN")
                    .role(EmployeeRole.ADMIN)
                    .build();
        }

        return employeeRepository.findByNumber(authentication.getName()).orElseThrow();
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public Employee createEmployee(@RequestBody CreateEmployeeRequest request) {
        Employee employee = Employee.builder()
                .name(request.personality().name())
                .surname(request.personality().surname())
                .patronymic(request.personality().patronymic())
                .phoneNumber(request.personality().phoneNumber())
                .password(passwordEncoder.encode(request.password()))
                .birthday(request.birthday())
                .employmentDay(request.employmentDay())
                .salary(request.salary())
                .role(request.role())
                .city(request.address().getCity())
                .zipCode(request.address().getZipCode())
                .street(request.address().getStreet())
                .build();

        employeeRepository.save(employee);
        return employee;
    }

    @PutMapping
    @PreAuthorize("hasRole('MANAGER')")
    public Employee editEmployee(@RequestBody Employee edited) {
        System.out.println(edited.getAddress());

        employeeRepository.update(edited);
        return edited;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public void deleteEmployee(@PathVariable Long id) {
        employeeRepository.delete(id);
    }

    @GetMapping("/count_best")
    public int countBest() {
        return employeeRepository.countBest();
    }
}
