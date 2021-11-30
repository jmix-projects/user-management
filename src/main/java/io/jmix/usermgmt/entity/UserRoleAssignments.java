package io.jmix.usermgmt.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import java.util.List;

@JmixEntity
@Table(name = "UMGMT_USER_ROLE_ASSIGNMENTS")
@Entity(name = "umgmt_UserRoleAssignments")
public class UserRoleAssignments {
    @Column(name = "ID", nullable = false)
    @Id
    private String id;

    @JoinTable(name = "UMGMT_ASSGN_RES_ROLE_LINK",
            joinColumns = @JoinColumn(name = "USER_ROLE_ASSGN_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "RESOURCE_ROLE_ID", referencedColumnName = "ID"))
    @ManyToMany
    private List<ResourceRole> resourceRoles;

    @JoinTable(name = "UMGMT_ASSGN_RL_ROLE_LINK",
            joinColumns = @JoinColumn(name = "USER_ROLE_ASSGN_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "ROW_LEVEL_ROLE_ID", referencedColumnName = "ID"))
    @ManyToMany
    private List<RowLevelRole> rowLevelRoles;

    public List<RowLevelRole> getRowLevelRoles() {
        return rowLevelRoles;
    }

    public void setRowLevelRoles(List<RowLevelRole> rowLevelRoles) {
        this.rowLevelRoles = rowLevelRoles;
    }

    public List<ResourceRole> getResourceRoles() {
        return resourceRoles;
    }

    public void setResourceRoles(List<ResourceRole> resourceRoles) {
        this.resourceRoles = resourceRoles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}