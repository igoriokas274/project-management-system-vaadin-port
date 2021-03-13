package org.igorl.pma.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
@Table(name = "country")
@EntityListeners(AuditingEntityListener.class)
public class Country extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "countryId", nullable = false)
    private Long countryId;

    @NotNull
    @NotEmpty
    @Column(name = "countryCode", unique = true, length = 2) // ISO 3166 ALPHA-2 code format
    private String countryCode;

    @NotNull
    @NotEmpty
    @Column(name = "countryName")
    private String countryName;

    @Column(name = "closed", columnDefinition = "int default 0")
    private boolean isClosed;

    @JsonIgnore
    @OneToMany(mappedBy = "country")
    private List<Customer> customers;

    @JsonIgnore
    @OneToMany(mappedBy = "country")
    private List<Supplier> suppliers;

    @JsonIgnore
    @OneToMany(mappedBy = "country")
    private List<StockType> stockTypes;

    @JsonIgnore
    @OneToMany(mappedBy = "country")
    private List<Employee> employees;
}
