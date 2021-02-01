package org.igorl.pma.backend.entity;

import lombok.*;
import org.igorl.pma.backend.enums.UserRoles;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ToString
@Setter
@Getter
@Entity
@Table(name = "user_accounts")
@EntityListeners(AuditingEntityListener.class)
public class UserAccount extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId", nullable = false)
    private long userId;

    @NotNull
    @NotEmpty
    @Column(name = "username", unique=true)
    private String userName;

    @NotNull
    @NotEmpty
    @Column(name = "password")
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRoles role;

    @Column(name = "enabled", columnDefinition = "int default 1")
    private boolean enabled = true;

    @OneToOne
    @JoinColumn(name = "employeeId")
    private Employee employee;

}
