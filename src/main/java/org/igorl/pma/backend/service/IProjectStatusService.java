package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.ProjectStatus;

import java.util.List;

public interface IProjectStatusService {

        List<ProjectStatus> findAll();
        ProjectStatus findById(long theId);
        void save(ProjectStatus theProjectStatus);
        void deleteById(long theId);
    }
