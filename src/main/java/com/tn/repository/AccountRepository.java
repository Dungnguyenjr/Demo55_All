package com.tn.repository;

import com.tn.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    Account findByUsername(String username);

    @Query(value = "FROM Account a JOIN a.department d WHERE a.username LIKE CONCAT('%', :data, '%') OR a.fullName LIKE CONCAT('%', :data, '%') OR d.departmentName LIKE CONCAT('%', :data, '%')")
    List<Account> searchData(String data);
}
