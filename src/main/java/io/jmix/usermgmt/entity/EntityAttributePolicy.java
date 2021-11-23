package io.jmix.usermgmt.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@JmixEntity
@Table(name = "UMGMT_ENTITY_ATTRIBUTE_POLICY")
@Entity(name = "umgmt_EntityAttributePolicy")
public class EntityAttributePolicy {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Integer version;

    @NotNull
    @Column(name = "ENTITY_NAME", nullable = false)
    private String entityName;

    @NotNull
    @Column(name = "ATTRIBUTE", nullable = false)
    private String attribute;

    @Column(name = "ACTION_")
    private String action;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ROLE_ID")
    private ResourceRole role;

    public AttributePolicyType getAction() {
        return action == null ? null : AttributePolicyType.fromId(action);
    }

    public void setAction(AttributePolicyType action) {
        this.action = action == null ? null : action.getId();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public ResourceRole getRole() {
        return role;
    }

    public void setRole(ResourceRole role) {
        this.role = role;
    }
}