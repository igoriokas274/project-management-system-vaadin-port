package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.UserAccount;

import java.util.List;

public interface IUserAccountService {

    List<UserAccount> findAll();
    void save(UserAccount theUserAccount);
    void deleteById(long theId);

}
