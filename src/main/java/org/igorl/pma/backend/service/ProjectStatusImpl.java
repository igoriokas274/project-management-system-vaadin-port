package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.ProjectStatus;
import org.igorl.pma.backend.repository.ProjectStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectStatusImpl implements IProjectStatusService {

    private final ProjectStatusRepository projectStatusRepository;

    @Autowired
    public ProjectStatusImpl (ProjectStatusRepository theProjectStatusRepository){projectStatusRepository = theProjectStatusRepository;}

    @Override
    public List<ProjectStatus> findAll() {return projectStatusRepository.findAll();}

    @Override
    public List<ProjectStatus> findAll(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return projectStatusRepository.findAll();
        } else {
            return projectStatusRepository.search(searchTerm);
        }
    }

    @Override
    @Transactional
    public void save(ProjectStatus theProjectStatus){
        projectStatusRepository.save(theProjectStatus);
    }

    @Override
    @Transactional
    public void deleteById(long theId) {
        projectStatusRepository.deleteById(theId);
    }
}
