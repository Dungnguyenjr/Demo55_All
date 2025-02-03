package com.tn.controller;

import com.tn.dto.AccountDTO;
import com.tn.dto.DepartmentDTO;
import com.tn.entity.Account;
import com.tn.entity.AccountRole;
import com.tn.entity.Department;
import com.tn.repository.AccountRepository;
import com.tn.service.AccountService;
import com.tn.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

    private JavaMailSender javaMailSender;
    private AccountRepository acccountrepo;

    @Value("${spring.mail.username}")
    private String sender;

    public AccountController(AccountService accountService,
                             DepartmentService departmentService,
                             JavaMailSender javaMailSender,
                             AccountRepository acccountrepo) {
        this.accountService = accountService;
        this.departmentService = departmentService;
        this.javaMailSender = javaMailSender;
        this.acccountrepo = acccountrepo;
    }

    private void sendEmail(String subject, String content) {
        try {
            // Creating a simple mail message
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo("kamehalv1@gmail.com");
            mailMessage.setSubject(subject);
            mailMessage.setText(content);

            // Sending the mail
            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            System.out.println("Send email fail");
        }
    }

    @GetMapping("")
    public String clientIndex() {
        return "client-index";
    }

    @GetMapping("reset-password")
    public String resetPassword() {
        return "reset-password";
    }

    @PostMapping("reset-password")
    public String resetPassword2(@RequestParam String username) {
        sendEmail("Reset Password", "http://localhost:8080/update-password/" + username);
        return "client-index";
    }

    @GetMapping("update-password/{username}")
    public String updatePassword(@PathVariable String username, Model model) {


        model.addAttribute("username",username);
        return "enter-password";
    }
    @PostMapping("enter-password")
    public String enterNewPassword(@RequestParam String username,
                                   @RequestParam String password){

        Account account = acccountrepo.findByUsername(username);
        account.setPassword(new BCryptPasswordEncoder().encode(password));

        acccountrepo.save(account);


        return "client-index";

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

    @GetMapping("account-edit/{id}")
    public String edit(@PathVariable int id, Model model) {
        log.info("Edit account with id: " + id);

        Account account = accountService.getById(id);
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(account.getId());
        accountDTO.setUsername(account.getUsername());
        accountDTO.setFullName(account.getFullName());
        accountDTO.setRole(account.getRole());
        accountDTO.setActive(account.isActive());
        if (account.getDepartment() != null) {
            accountDTO.setDepartmentName(account.getDepartment().getDepartmentName());
        }

        model.addAttribute("accountDTO", accountDTO);
        return "account-edit";
    }

    @PostMapping("/account/update")
    public String update(@RequestParam Integer id,
                         @RequestParam String username,
                         @RequestParam String fullName,
                         @RequestParam String role) {

        Account account = accountService.getById(id);
        account.setUsername(username);
        account.setFullName(fullName);

        if (role.equals("USER")) {
            account.setRole(AccountRole.USER);
        } else if (role.equals("ADMIN")) {
            account.setRole(AccountRole.ADMIN);
        }

        accountService.save(account);

        return "redirect:/account";
    }

    @GetMapping("account-search")
    public String search(@RequestParam String data, Model model) {
        List<Account> accounts = accountService.search(data);
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
        model.addAttribute("data", data);

        return "account-list";
    }

}
