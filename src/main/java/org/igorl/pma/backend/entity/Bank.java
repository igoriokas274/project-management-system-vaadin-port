package org.igorl.pma.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Bank {

    @Column(name = "SWIFT")
    private String swift;

    @Column(name = "bankCode")
    private String bankCode;

    @Column(name = "bankName")
    private String bankName;

    @Column(name = "bankAccount")
    private String bankAccount;
}
