package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.ProjectStatus;

import java.util.List;

public interface IProjectStatusService {
        List<ProjectStatus> findAll();
        List<ProjectStatus> findAll(String searchTerm);
        void save(ProjectStatus theProjectStatus);
        void deleteById(long theId);
    }
