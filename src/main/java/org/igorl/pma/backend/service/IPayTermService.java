package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.PayTerm;

import java.util.List;

public interface IPayTermService {
    List<PayTerm> findAll();
    List<PayTerm> findAll(String stringFilter);
    void save(PayTerm thePayTerm);
    void deleteById(Long theId);
}
