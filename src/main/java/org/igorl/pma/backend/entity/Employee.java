package org.igorl.pma.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.igorl.pma.backend.enums.Gender;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@ToString
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "employee")
@EntityListeners(AuditingEntityListener.class)
public class Employee extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employeeId", nullable = false, unique = true)
    private Long employeeId;

    @NotNull
    @NotEmpty
    @Column(name = "firstName")
    private String firstName;

    @Column(name = "middleName")
    private String middleName;

    @NotNull
    @NotEmpty
    @Column(name = "lastName")
    private String lastName;

    @Column(name = "title")
    private String title;

    @Column(name = "addressLine1")
    private String addressLine1;

    @Column(name = "addressLine2")
    private String addressLine2;

    @Column(name = "city")
    private String city;

    @Column(name = "zipCode")
    private String zipCode;

    @Column(name = "officePhone")
    private String officePhone;

    @Column(name = "mobilePhone")
    private String mobilePhone;

    @Column(name = "homePhone")
    private String homePhone;

    @Column(name = "workEmail")
    private String workEmail;

    @Column(name = "personalEmail")
    private String personalEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 1)
    private Gender gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "dateOfBirth")
    private LocalDate dateOfBirth;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "dateOfEmployment")
    private LocalDate dateOfEmployment;

    @Column(name = "bankCode")
    private String bankCode;

    @Column(name = "bankName")
    private String bankName;

    @Column(name = "bankAccount")
    private String bankAccount;

    @Column(name = "closed", columnDefinition = "int default 0")
    private boolean isClosed;

    @JsonIgnore
    @OneToMany(mappedBy = "manager")
    private List<Project> projects;

    @ManyToOne
    @JoinColumn(name = "departmentId")
    private Department department;

    @JsonIgnore
    @OneToOne(mappedBy = "employee")
    private UserAccount userAccount;

    @ManyToOne
    @JoinColumn(name = "countryId")
    private Country country;

    public String getFullName() {
            return firstName + " " + middleName + " " + lastName;
    }
}
