package org.igorl.pma.backend.entity;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
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

    @Column(name = "itemName", nullable = false)
    private String itemName;

    @Column(name = "quantity", nullable = false)
    private double quantity;

    @Column(name = "salesPrice", precision = 10, scale = 2)
    private BigDecimal salesPrice;

    @Column(name = "purchasePrice", precision = 10, scale = 2)
    private BigDecimal purchasePrice;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "quotationId")
    private Quotation quotation;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "itemId")
    private Item item;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "vatId")
    private VATValue vatValue;

}
