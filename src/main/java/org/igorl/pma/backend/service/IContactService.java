package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.Contact;

import java.util.List;

public interface IContactService {

    List<Contact> findAll();
    Contact findById(long theId);
    void save(Contact theContact);
    void deleteById(long theId);

}
