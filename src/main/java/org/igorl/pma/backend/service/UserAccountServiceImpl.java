package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.UserAccount;
import org.igorl.pma.backend.repository.EmployeeRepository;
import org.igorl.pma.backend.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserAccountServiceImpl implements IUserAccountService {

    private UserAccountRepository userAccountRepository;
    private EmployeeRepository employeeRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserAccountServiceImpl(UserAccountRepository theUserAccountRepository, EmployeeRepository theEmployeeRepository) {
        userAccountRepository = theUserAccountRepository;
        employeeRepository = theEmployeeRepository;
    }

    @Override
    public List<UserAccount> findAll () { return userAccountRepository.findAll();
    }

    @Override
    public List<UserAccount> findAll (String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return userAccountRepository.findAll();
        } else {
            return userAccountRepository.search(stringFilter);
        }
    }

    @Override
    public void delete(UserAccount userAccount) {
        userAccountRepository.delete(userAccount);
    }

    @Override
    public long countUsers() {
        return userAccountRepository.count();
    }

    @Override
    @Transactional
    public void save(UserAccount theUserAccount) {
        theUserAccount.setPassword(bCryptPasswordEncoder.encode(theUserAccount.getPassword()));
        userAccountRepository.save(theUserAccount);
    }
}
