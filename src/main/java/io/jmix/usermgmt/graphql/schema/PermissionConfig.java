package io.jmix.usermgmt.graphql.schema;

import io.jmix.graphql.schema.permission.ShortPermissionInfo;

import javax.annotation.Nullable;
import java.util.List;

public class PermissionConfig {

    private List<ShortPermissionInfo> entities;
    private List<ShortPermissionInfo> entityAttributes;
    private List<ShortPermissionInfo> specifics;
    private List<ShortPermissionInfo> menus;
    private List<ShortPermissionInfo> screens;

    public PermissionConfig() {
    }

    @Nullable
    public List<ShortPermissionInfo> getEntities() {
        return entities;
    }

    public void setEntities(List<ShortPermissionInfo> entities) {
        this.entities = entities;
    }

    @Nullable
    public List<ShortPermissionInfo> getEntityAttributes() {
        return entityAttributes;
    }

    public void setEntityAttributes(List<ShortPermissionInfo> entityAttributes) {
        this.entityAttributes = entityAttributes;
    }

    @Nullable
    public List<ShortPermissionInfo> getSpecifics() {
        return specifics;
    }

    public void setSpecifics(List<ShortPermissionInfo> specifics) {
        this.specifics = specifics;
    }

    @Nullable
    public List<ShortPermissionInfo> getMenus() {
        return menus;
    }

    public void setMenus(List<ShortPermissionInfo> menus) {
        this.menus = menus;
    }

    @Nullable
    public List<ShortPermissionInfo> getScreens() {
        return screens;
    }

    public void setScreens(List<ShortPermissionInfo> screens) {
        this.screens = screens;
    }
}
