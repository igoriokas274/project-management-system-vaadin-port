package org.igorl.pma.backend.repository;

import org.igorl.pma.backend.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
}
