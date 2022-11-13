package org.igorl.pma.backend.enums;

public enum Gender {

    M("Masculine"), F("Feminine");

    private String genderName;
    Gender(String genderName) {
        this.genderName = genderName;
    }

    public String getGenderName() {
        return genderName;
    }

    @Override
    public String toString() {
        return genderName;
    }
}
