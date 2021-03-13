package org.igorl.pma.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ToString
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "project")
@EntityListeners(AuditingEntityListener.class)
public class Project extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "projectId", nullable = false, unique = true)
    private Long projectId;

    @NotNull
    @NotEmpty
    @Column(name = "projectName", nullable = false)
    private String projectName;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "projectStartDate", nullable = false)
    private LocalDate projectStartDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "projectEndDate", nullable = false)
    private LocalDate projectEndDate;

    @Column(name = "projectMemo1", columnDefinition="TEXT")
    private String projectMemo1;

    @Column(name = "projectMemo2", columnDefinition="TEXT")
    private String projectMemo2;

    @Column(name = "closed", columnDefinition = "int default 0")
    private boolean isClosed;

    @ManyToOne
    @JoinColumn(name = "projectStatusId")
    private ProjectStatus projectStatus;

    @ManyToOne
    @JoinColumn(name = "projectTypeId")
    private ProjectType projectType;

    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customer customer;

    @ManyToMany(mappedBy = "projects")
    private Set<Employee> employees;

    @ManyToOne
    @JoinColumn(name = "term")
    private PayTerm payTerm;

    @JsonIgnore
    @OneToMany(mappedBy = "project")
    private List<Quotation> quotations;

}
