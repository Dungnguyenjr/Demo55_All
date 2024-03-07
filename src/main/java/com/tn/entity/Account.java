package com.tn.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "tbl_account")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Account extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(nullable = false)
    private String username;

    @NonNull
    @Column(nullable = false)
    private String password;

    @NonNull
    @Column(length = 50)
    private String fullName;

    @NonNull
    // EnumType.STRING: ADMIN, MANAGER, USER
    // EnumType.ORDINAL: 0, 1, 2
    @Enumerated(EnumType.STRING)
    private AccountRole role;

    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

}
