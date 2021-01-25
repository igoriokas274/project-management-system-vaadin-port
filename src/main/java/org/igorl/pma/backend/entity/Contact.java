package org.igorl.pma.backend.entity;

import lombok.*;
import org.igorl.pma.backend.enums.Gender;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contact")
@EntityListeners(AuditingEntityListener.class)
public class Contact extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contactId", nullable = false, unique = true)
    private Long contactId;

    @Embedded
    private Name name;

    @Column(name = "contactPhone")
    private String contactPhone;

    @Column(name = "contactEmail")
    private String contactEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 1)
    private Gender gender;

    @Column(name = "closed", columnDefinition = "int default 0")
    private boolean isClosed;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "customerId")
    private Customer customer;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "supplierId")
    private Supplier supplier;

}
