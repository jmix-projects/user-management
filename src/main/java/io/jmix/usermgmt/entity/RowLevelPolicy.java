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

import io.jmix.core.Messages;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Table(name = "UMGMT_ROW_LEVEL_POLICY")
@Entity(name = "umgmt_RowLevelPolicy")
@JmixEntity
public class RowLevelPolicy implements Serializable {
    private static final long serialVersionUID = -8009316149061437606L;

    @Id
    @Column(name = "ID")
    @JmixGeneratedValue
    private UUID id;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Integer version;

    @NotNull
    @Column(name = "TYPE_", nullable = false)
    private String type;

    @NotNull
    @Column(name = "ENTITY_NAME", nullable = false)
    private String entityName;

    @NotNull
    @Column(name = "ACTION_", nullable = false)
    private String action;

    @Lob
    @Column(name = "WHERE_CLAUSE", length = 5000)
    private String whereClause;

    @Lob
    @Column(name = "JOiN_CLAUSE", length = 5000)
    private String joinClause;

    @Lob
    @Column(name = "SCRIPT_", length = 5000)
    private String script;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ROLE_ID")
    private RowLevelRole role;

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

    public RowLevelAction getAction() {
        return RowLevelAction.fromId(action);
    }

    public void setAction(RowLevelAction action) {
        this.action = action != null ? action.getId() : null;
    }

    public RowLevelRole getRole() {
        return role;
    }

    public void setRole(RowLevelRole role) {
        this.role = role;
    }

    public String getJoinClause() {
        return joinClause;
    }

    public void setJoinClause(String joinClause) {
        this.joinClause = joinClause;
    }

    public String getWhereClause() {
        return whereClause;
    }

    public void setWhereClause(String whereClause) {
        this.whereClause = whereClause;
    }

    public RowLevelType getType() {
        return RowLevelType.fromId(type);
    }

    public void setType(RowLevelType type) {
        this.type = type != null ? type.getId() : null;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    @InstanceName
    public String getInstanceName(Messages messages) {
        return entityName + ":" + messages.getMessage(getType());
    }
}