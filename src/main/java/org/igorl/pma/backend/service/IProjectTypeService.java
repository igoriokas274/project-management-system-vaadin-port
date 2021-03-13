package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.ProjectType;

import java.util.List;

public interface IProjectTypeService {
    List<ProjectType> findAll();
    List<ProjectType> findAll(String searchTerm);
    void save (ProjectType theProjectType);
    void deleteById(long theId);
}
