package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.UserAccount;
import org.igorl.pma.backend.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserAccountServiceImpl implements IUserAccountService {

    private UserAccountRepository userAccountRepository;

    @Autowired
    public UserAccountServiceImpl(UserAccountRepository theUserAccountRepository) {
        userAccountRepository = theUserAccountRepository;
    }

    @Override
    public List<UserAccount> findAll () { return userAccountRepository.findAll();
    }

    @Override
    @Transactional
    public void save ( UserAccount theUserAccount) { userAccountRepository.save(theUserAccount);
    }

    @Override
    @Transactional
    public void deleteById(long theId) {
        userAccountRepository.deleteById(theId);
    }
}
