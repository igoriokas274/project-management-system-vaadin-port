package org.igorl.pma.backend.repository;

import org.igorl.pma.backend.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    @Query("select u from UserAccount u where lower(u.userName) like lower(concat('%', :searchTerm, '%'))")
    List<UserAccount> search(@Param("searchTerm") String searchTerm);
}
