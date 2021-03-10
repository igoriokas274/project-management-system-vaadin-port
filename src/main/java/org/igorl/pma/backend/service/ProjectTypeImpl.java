package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.ProjectType;
import org.igorl.pma.backend.repository.ProjectTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectTypeImpl implements IProjectTypeService {

    private final ProjectTypeRepository projectTypeRepository;

    @Autowired
    public ProjectTypeImpl (ProjectTypeRepository theProjectTypeRepository){projectTypeRepository = theProjectTypeRepository;}

    @Override
    public List<ProjectType> findAll() {return projectTypeRepository.findAll();}

    @Override
    public List<ProjectType> findAll(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return projectTypeRepository.findAll();
        } else {
            return projectTypeRepository.search(searchTerm);
        }
    }

    @Override
    @Transactional
    public void save(ProjectType theProjectType){
        projectTypeRepository.save(theProjectType);
    }

    @Override
    @Transactional
    public void deleteById(long theId) {
        projectTypeRepository.deleteById(theId);
    }
}
