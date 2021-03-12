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
@Table(name = "stock_type")
@EntityListeners(AuditingEntityListener.class)
public class StockType extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stockId", nullable = false, unique = true)
    private Long stockId;

    @NotNull
    @NotEmpty
    @Column(name = "stockName")
    private String stockName;

    @Column(name = "addressLine1")
    private String addressLine1;

    @Column(name = "addressLine2")
    private String addressLine2;

    @Column(name = "city")
    private String city;

    @Column(name = "zipCode")
    private String zipCode;

    @Column(name = "closed", columnDefinition = "int default 0")
    private boolean isClosed;

    @ManyToOne
    @JoinColumn(name = "countryId")
    private Country country;

    @JsonIgnore
    @OneToMany(mappedBy = "stockType")
    private List<Item> items;

    public String getFullAddress() {
        return addressLine1 + " " + addressLine2 + " " + zipCode + " " + city;
    }
}
