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
@Table(name = "quotation_status")
@EntityListeners(AuditingEntityListener.class)
public class QuotationStatus extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quotationStatusId", nullable = false, unique = true)
    private Long quotationStatusId;

    @NotNull
    @NotEmpty
    @Column(name = "quotationStatusName")
    private String quotationStatusName;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quotationStatus")
    private List<Quotation> quotations;

}
