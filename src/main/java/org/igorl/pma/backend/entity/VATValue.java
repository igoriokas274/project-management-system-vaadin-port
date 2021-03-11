package org.igorl.pma.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@ToString
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "vat_value")
@EntityListeners(AuditingEntityListener.class)
public class VATValue extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vatId", nullable = false, unique = true)
    private Long vatId;

    @NotNull
    @NotEmpty
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "vatValue")
    @Digits(integer=3, fraction=2)
    private BigDecimal vatValue;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vatValue", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Item> items;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vatValue", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<ItemToQuotation> itemToQuotations;

}
