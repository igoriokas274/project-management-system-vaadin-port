package org.igorl.pma.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer")
@EntityListeners(AuditingEntityListener.class)
public class Customer extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customerId", nullable = false, unique = true)
    private Long customerId;

    @Column(name = "customerName", nullable = false)
    private String customerName;

    @Column(name = "customerRegistrationNumber")
    private String customerRegistrationNumber;

    @Column(name = "customerVATNumber")
    private String customerVATNumber;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "contactPhone", column = @Column(name = "customerPhone")),
            @AttributeOverride(name = "contactEmail", column = @Column(name = "customerEmail"))
    })
    private Address address;

    @Embedded
    private Bank bank;

    @Column(name = "closed", columnDefinition = "int default 0")
    private boolean isClosed;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Project> projects;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Contact> contacts;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "countryId")
    private Country country;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "currencyId")
    private Currency currency;

}
