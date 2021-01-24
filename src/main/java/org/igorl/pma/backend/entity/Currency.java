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
@Table(name = "currency")
@EntityListeners(AuditingEntityListener.class)
public class Currency extends Auditable {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "currencyId", nullable = false)
    private Long currencyId;

    @Column(name = "currencyCode", nullable = false, unique = true, length =3) // ISO 4217 code format
    private String currencyCode;

    @Column(name = "currencyName", nullable = false)
    private String currencyName;

    @Column(name = "closed", columnDefinition = "int default 0")
    private boolean isClosed;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "currency", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Customer> customers;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "currency", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Supplier> suppliers;

}
