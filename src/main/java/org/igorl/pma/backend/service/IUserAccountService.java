package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.UserAccount;

import java.util.List;

public interface IUserAccountService {

    List<UserAccount> findAll();
    List<UserAccount> findAll (String stringFilter);
    long countUsers();
    void save(UserAccount theUserAccount);
    void delete(UserAccount userAccount);

}
