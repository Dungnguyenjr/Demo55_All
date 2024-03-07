package com.tn.controller;

import com.tn.dto.AccountDTO;
import com.tn.dto.DepartmentDTO;
import com.tn.entity.Account;
import com.tn.entity.AccountRole;
import com.tn.entity.Department;
import com.tn.service.AccountService;
import com.tn.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//@RestController: Viết restful Api
//@Controller: Dùng giao diện thymeleaf
@Controller
@Slf4j
public class AccountController {

    private AccountService accountService;

    private DepartmentService departmentService;

    public AccountController(AccountService accountService,
                             DepartmentService departmentService) {
        this.accountService = accountService;
        this.departmentService = departmentService;
    }

    @GetMapping("account")
    public String getAll(Model model) {
        log.info("Get all account");

        List<Account> accounts = accountService.getAll();
        List<AccountDTO> accountDTOS = new ArrayList<>();

        accounts.forEach(account -> {
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setId(account.getId());
            accountDTO.setUsername(account.getUsername());
            accountDTO.setFullName(account.getFullName());
            accountDTO.setRole(account.getRole());
            accountDTO.setActive(account.isActive());

            if (account.getDepartment() != null) {
                accountDTO.setDepartmentName(account.getDepartment().getDepartmentName());
            }

            accountDTOS.add(accountDTO);
        });

        model.addAttribute("accountDTOS", accountDTOS);

        return "account-list";
    }

    @GetMapping("account-delete/{id}")
    public String delete(@PathVariable int id) {
        log.info("Delete account with id: " + id);

        accountService.delete(id);
        return "redirect:/account";
    }

    @GetMapping("account/add")
    public String add(Model model) {
        log.info("Add new account");

        List<Department> departments = departmentService.getAll();
        List<DepartmentDTO> departmentDTOS = new ArrayList<>();

        departments.forEach(department -> {
            DepartmentDTO departmentDTO = new DepartmentDTO();
            departmentDTO.setId(department.getId());
            departmentDTO.setDepartmentName(department.getDepartmentName());

            departmentDTOS.add(departmentDTO);
        });
        model.addAttribute("departmentDTOS", departmentDTOS);

        return "account-add";
    }

    @PostMapping("account/save")
    public String save(@RequestParam String username,
                       @RequestParam String password,
                       @RequestParam String fullName,
                       @RequestParam String role,
                       @RequestParam Integer departmentId
                       ) {

        AccountRole accountRole = null;
        if (role.equals("USER") || role.equals("")) {
            accountRole = AccountRole.USER;
        }
        if (role.equals("ADMIN")) {
            accountRole = AccountRole.ADMIN;
        }

        password = new BCryptPasswordEncoder().encode(password);
        Account account = new Account(username, password, fullName, accountRole);
        if (departmentId != 0) {
            Department department = departmentService.getById(departmentId);
            account.setDepartment(department);
        }

        accountService.save(account);

        return "redirect:/account";
    }
    // abc

}
