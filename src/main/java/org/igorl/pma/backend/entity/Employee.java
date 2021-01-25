package org.igorl.pma.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.igorl.pma.backend.enums.Gender;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employee")
@EntityListeners(AuditingEntityListener.class)
public class Employee extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employeeId", nullable = false, unique = true)
    private Long employeeId;

    @Embedded
    private Name name;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "contactPhone", column = @Column(name = "homePhone")),
            @AttributeOverride(name = "contactEmail", column = @Column(name = "personalEmail"))
    })
    private Address address;

    @Embedded
    private Bank bank;

    @Column(name = "officePhone")
    private String officePhone;

    @Column(name = "mobilePhone")
    private String mobilePhone;

    @Column(name = "workEmail")
    private String workEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 1)
    private Gender gender;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "dateOfBirth")
    private Date dateOfBirth;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "dateOfEmployment", nullable = false)
    private Date dateOfEmployment;

    @Column(name = "closed", columnDefinition = "int default 0")
    private boolean isClosed;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "employee_project",
            joinColumns = {@JoinColumn(name = "employeeId")},
            inverseJoinColumns = {@JoinColumn(name = "projectId")}
    )
    private Set<Project> projects = new HashSet<>();

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "departmentId")
    private Department department;

    @JsonIgnore
    @OneToOne(mappedBy = "employee", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private UserAccount userAccount;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "countryId")
    private Country country;

}
