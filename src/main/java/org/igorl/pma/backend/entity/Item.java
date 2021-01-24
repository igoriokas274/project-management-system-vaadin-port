package org.igorl.pma.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.igorl.pma.backend.enums.ItemType;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.List;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "item")
@EntityListeners(AuditingEntityListener.class)
public class Item extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "itemId", nullable = false, unique = true)
    private Long itemId;

    @Column(name = "itemName", nullable = false)
    private String itemName;

    @Column(name = "itemDescription", columnDefinition="TEXT")
    private String itemDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "itemType", length = 20)
    private ItemType itemType;

    @Column(name = "unit")
    private String unit;

    @Column(name = "minStockLevel")
    private Integer minStockLevel;

    @Column(name = "salesPrice", precision = 10, scale = 2)
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer=10, fraction=2)
    private BigDecimal salesPrice;

    @Column(name = "purchasePrice", precision = 10, scale = 2)
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer=10, fraction=2)
    private BigDecimal purchasePrice;

    @Column(name = "closed", columnDefinition = "int default 0")
    private boolean isClosed;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "stockId")
    private StockType stockType;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "supplierId")
    private Supplier supplier;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "vatId")
    private VATValue vatValue;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<ItemToQuotation> itemToQuotations;

}
