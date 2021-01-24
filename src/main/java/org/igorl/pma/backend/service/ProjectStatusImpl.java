package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.ProjectStatus;
import org.igorl.pma.backend.repository.ProjectStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectStatusImpl implements IProjectStatusService {

    private final ProjectStatusRepository projectStatusRepository;

    @Autowired
    public ProjectStatusImpl (ProjectStatusRepository theProjectStatusRepository){projectStatusRepository = theProjectStatusRepository;}

    @Override
    public List<ProjectStatus> findAll() {return projectStatusRepository.findAll();}

    @Override
    public ProjectStatus findById(long theId) {

        Optional<ProjectStatus> result = projectStatusRepository.findById(theId);
        ProjectStatus theProjectStatus;
        if (result.isPresent()){
            theProjectStatus = result.get();
        } else {
            throw new RuntimeException("Did not find project status id" + theId);
        }
        return theProjectStatus;
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
