package org.igorl.pma.backend.entity;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "item_to_quotation")
@EntityListeners(AuditingEntityListener.class)
public class ItemToQuotation extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @NotEmpty
    @Column(name = "itemName", nullable = false)
    private String itemName;

    @NotNull
    @NotEmpty
    @Column(name = "quantity", nullable = false)
    private double quantity;

    @Column(name = "salesPrice", precision = 10, scale = 2)
    private BigDecimal salesPrice;

    @Column(name = "purchasePrice", precision = 10, scale = 2)
    private BigDecimal purchasePrice;

    @ManyToOne
    @JoinColumn(name = "quotationId")
    private Quotation quotation;

    @ManyToOne
    @JoinColumn(name = "itemId")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "vatId")
    private VATValue vatValue;

}
