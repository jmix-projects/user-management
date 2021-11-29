/*
 * Copyright 2020 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.jmix.usermgmt.entity;

import io.jmix.core.DeletePolicy;
import io.jmix.core.entity.annotation.OnDelete;
import io.jmix.core.metamodel.annotation.Composition;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.securitydata.entity.StringCollectionConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Table(name = "UMGMT_ROW_LEVEL_ROLE")
@Entity(name = "umgmt_RowLevelRole")
@JmixEntity
public class RowLevelRole implements Serializable {
    private static final long serialVersionUID = -1587602133446436634L;

    @Id
    @Column(name = "ID")
    private String id;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Integer version;

    @Column(name = "NAME", nullable = false)
    @InstanceName
    private @NotNull String name;

    @Column(name = "CODE", nullable = false)
    private @NotNull String code;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "READ_ONLY")
    private Boolean readOnly;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "role")
    private List<RowLevelPolicy> rowLevelPolicies;

    @Lob
    @Column(name = "CHILD_ROLES")
    @Convert(converter = StringCollectionConverter.class)
    private Set<String> childRoles;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<RowLevelPolicy> getRowLevelPolicies() {
        return rowLevelPolicies;
    }

    public void setRowLevelPolicies(List<RowLevelPolicy> rowLevelPolicies) {
        this.rowLevelPolicies = rowLevelPolicies;
    }

    public Set<String> getChildRoles() {
        return childRoles;
    }

    public void setChildRoles(Set<String> childRoles) {
        this.childRoles = childRoles;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }
}