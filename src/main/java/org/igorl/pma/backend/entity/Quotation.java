package org.igorl.pma.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@ToString
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "quotation")
@EntityListeners(AuditingEntityListener.class)
public class Quotation extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quotationId", nullable = false, unique = true)
    private Long quotationId;

    @NotNull
    @NotEmpty
    @Column(name = "quotationTitle", nullable = false)
    private String quotationTitle;

    @Column(name = "confirmed", columnDefinition = "int default 0")
    private boolean isConfirmed;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "quotationDate")
    private LocalDate quotationDate;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "quotationStatusId")
    private QuotationStatus quotationStatus;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quotation")
    private List<ItemToQuotation> itemToQuotations;

}
