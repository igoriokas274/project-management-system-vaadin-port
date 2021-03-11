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
@Table(name = "currency")
@EntityListeners(AuditingEntityListener.class)
public class Currency extends Auditable {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "currencyId", nullable = false)
    private Long currencyId;

    @NotNull
    @NotEmpty
    @Column(name = "currencyCode", unique = true, length =3) // ISO 4217 code format
    private String currencyCode;

    @NotNull
    @NotEmpty
    @Column(name = "currencyName")
    private String currencyName;

    @Column(name = "closed", columnDefinition = "int default 0")
    private boolean isClosed;

    @JsonIgnore
    @OneToMany(mappedBy = "currency")
    private List<Customer> customers;

    @JsonIgnore
    @OneToMany(mappedBy = "currency")
    private List<Supplier> suppliers;

}
