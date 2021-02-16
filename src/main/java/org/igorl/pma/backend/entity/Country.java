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
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "country", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Customer> customers;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "country", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Supplier> suppliers;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "country", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<StockType> stockTypes;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "country", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Employee> employees;
}
