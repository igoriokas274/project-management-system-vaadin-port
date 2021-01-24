package org.igorl.pma.backend.enums;

import lombok.Getter;

public enum Gender {
    M ("Masculine"),
    F ("Feminine");

    @Getter
    private String displayGender;

    Gender(String displayGender) {
        this.displayGender = displayGender;
    }
}
