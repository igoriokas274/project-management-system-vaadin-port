package org.igorl.pma.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer=2, fraction=2)
    private Double vatValue;

    @JsonIgnore
    @OneToMany(mappedBy = "vatValue")
    private List<Item> items;

    @JsonIgnore
    @OneToMany(mappedBy = "vatValue")
    private List<ItemToQuotation> itemToQuotations;

}
