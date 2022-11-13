package org.igorl.pma.backend.enums;

public enum UserRoles {

    ADMIN("Administrator"),
    MANAGER("Manager"),
    ACCOUNTANT("Accountant"),
    HR("Human Resources"),
    SUPPLY("Supply");

    /**
     * Test JavaDocs
     */
    private String roleName;

    UserRoles(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    @Override
    public String toString() {
        return roleName;
    }
}
