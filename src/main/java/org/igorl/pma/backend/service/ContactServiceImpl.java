package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.Contact;
import org.igorl.pma.backend.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContactServiceImpl implements IContactService {

    private ContactRepository contactRepository;

    @Autowired
    public ContactServiceImpl(ContactRepository theContactRepository) {
        contactRepository = theContactRepository;
    }

    @Override
    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    @Override
    public List<Contact> findAll(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return contactRepository.findAll();
        } else {
            return contactRepository.search(searchTerm);
        }
    }

    @Override
    @Transactional
    public void save(Contact theContact) { contactRepository.save(theContact);
    }

    @Override
    @Transactional
    public void deleteById(long theId) { contactRepository.deleteById(theId);
    }
}
