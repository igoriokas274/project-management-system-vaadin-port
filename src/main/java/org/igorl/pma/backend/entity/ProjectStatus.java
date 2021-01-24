package org.igorl.pma.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "project_status")
@EntityListeners(AuditingEntityListener.class)
public class ProjectStatus extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "projectStatusId", nullable = false, unique = true)
    private Long projectStatusId;

    @Column(name = "projectStatusName", nullable = false)
    private String projectStatusName;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "projectStatus", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Project> projects;

}
