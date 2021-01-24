package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.Project;

import java.util.List;

public interface IProjectService {

    List<Project> findAll();
    void save(Project theProject);
    void deleteById(long theId);
}
