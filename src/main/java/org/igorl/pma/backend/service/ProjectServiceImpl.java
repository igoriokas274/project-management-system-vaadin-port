package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.Project;
import org.igorl.pma.backend.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectServiceImpl implements IProjectService {

    private ProjectRepository projectRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository theProjectRepository) {
        projectRepository = theProjectRepository;
    }

    @Override
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @Override
    @Transactional
    public void save(Project theProject) {
        projectRepository.save(theProject);
    }

    @Override
    @Transactional
    public void deleteById(long theId) {
        projectRepository.deleteById(theId);
    }
}
