package org.igorl.pma.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@ToString
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "pay_term")
@EntityListeners(AuditingEntityListener.class)
public class PayTerm extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "termId", nullable = false, unique = true)
    private Long termId;

    // TODO: Check compatibility with BigDecimalFiled math actions [check PayTermForm.class]
    @NotNull
    @Column(name = "term", unique = true)
    private Integer term;

    @NotNull
    @NotEmpty
    @Column(name = "description")
    private String description;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "payTerm")
    private List<Project> projects;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "payTerm")
    private List<Supplier> suppliers;

}
