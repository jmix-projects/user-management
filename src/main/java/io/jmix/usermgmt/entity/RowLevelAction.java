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

import io.jmix.core.metamodel.datatype.impl.EnumClass;

import javax.annotation.Nullable;
import java.util.Objects;

public enum RowLevelAction implements EnumClass<String> {

    CREATE("create"),
    READ("read"),
    UPDATE("update"),
    DELETE("delete");

    private String id;

    RowLevelAction(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static RowLevelAction fromId(String id) {
        for (RowLevelAction value : RowLevelAction.values()) {
            if (Objects.equals(id, value.getId())) return value;
        }
        return null;
    }
}
