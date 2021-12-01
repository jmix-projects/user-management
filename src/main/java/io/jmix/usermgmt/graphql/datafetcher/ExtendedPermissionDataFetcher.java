package io.jmix.usermgmt.graphql.datafetcher;

import graphql.schema.DataFetcher;
import io.jmix.core.AccessManager;
import io.jmix.core.Metadata;
import io.jmix.core.accesscontext.CrudEntityContext;
import io.jmix.core.accesscontext.EntityAttributeContext;
import io.jmix.core.metamodel.model.MetaClass;
import io.jmix.core.metamodel.model.MetaProperty;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.graphql.NamingUtils;
import io.jmix.graphql.accesscontext.GraphQLAccessContext;
import io.jmix.graphql.datafetcher.PermissionDataFetcher;
import io.jmix.graphql.schema.permission.ShortPermissionInfo;
import io.jmix.security.authentication.RoleGrantedAuthority;
import io.jmix.security.model.ResourcePolicy;
import io.jmix.usermgmt.graphql.schema.PermissionConfig;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static io.jmix.graphql.accesscontext.GraphQLAccessContext.GRAPHQL_FILE_DOWNLOAD_ENABLED;
import static io.jmix.graphql.accesscontext.GraphQLAccessContext.GRAPHQL_FILE_UPLOAD_ENABLED;

public class ExtendedPermissionDataFetcher extends PermissionDataFetcher {

    protected static final int VIEW_MENU_PERMISSION = 1;
    protected static final int VIEW_SCREEN_PERMISSION = 1;

    protected final CurrentAuthentication currentAuthentication;
    protected final Metadata metadata;
    protected final AccessManager accessManager;

    public ExtendedPermissionDataFetcher(Metadata metadata,
                                         AccessManager accessManager,
                                         CurrentAuthentication currentAuthentication) {
        this.metadata = metadata;
        this.accessManager = accessManager;
        this.currentAuthentication = currentAuthentication;
    }

    @Override
    public DataFetcher<?> loadPermissions() {
        return environment -> {
            Set<String> defs = environment.getSelectionSet().getFieldsGroupedByResultKey().keySet();
            boolean loadEntities = defs.contains(NamingUtils.ENTITIES);
            boolean loadEntityAttrs = defs.contains(NamingUtils.ENTITY_ATTRS);
            boolean loadSpecifics = defs.contains(NamingUtils.SPECIFICS);
            boolean loadScreens = defs.contains("screens");
            boolean loadMenus = defs.contains("menus");
            return getPermissions(loadEntities, loadEntityAttrs, loadSpecifics, loadScreens, loadMenus);
        };
    }

    protected PermissionConfig getPermissions(boolean entities,
                                              boolean entityAttrs,
                                              boolean specifics,
                                              boolean screens,
                                              boolean menus) {
        PermissionConfig permissionConfig = new PermissionConfig();

        List<ShortPermissionInfo> entityPermissions = new ArrayList<>();
        List<ShortPermissionInfo> entityAttributePermissions = new ArrayList<>();
        List<ShortPermissionInfo> specificPermissions = new ArrayList<>();
        List<ShortPermissionInfo> screenPermissions = new ArrayList<>();
        List<ShortPermissionInfo> menuPermissions = new ArrayList<>();
        permissionConfig.setEntities(entityPermissions);
        permissionConfig.setEntityAttributes(entityAttributePermissions);
        permissionConfig.setSpecifics(specificPermissions);
        permissionConfig.setScreens(screenPermissions);
        permissionConfig.setMenus(menuPermissions);

        if (entities || entityAttrs) {
            fillEntityRelatedPermissions(entities, entityAttrs, entityPermissions, entityAttributePermissions);
        }

        if (specifics) {
            fillSpecificPermissions(specificPermissions);
        }

        if (screens || menus) {
            fillScreenAndMenuPermissions(screens, menus, screenPermissions, menuPermissions);
        }

        return permissionConfig;
    }

    protected void fillEntityRelatedPermissions(boolean entities,
                                                boolean entityAttrs,
                                                List<ShortPermissionInfo> entityPermissions,
                                                List<ShortPermissionInfo> entityAttributePermissions) {
        for (MetaClass metaClass : metadata.getSession().getClasses()) {
            if (entities) {
                fillEntityPermissions(metaClass, entityPermissions);
            }

            if (entityAttrs) {
                fillEntityAttributePermissions(metaClass, entityAttributePermissions);
            }
        }
    }

    protected void fillEntityPermissions(MetaClass metaClass,
                                         List<ShortPermissionInfo> entityPermissions) {
        CrudEntityContext entityContext = new CrudEntityContext(metaClass);
        accessManager.applyRegisteredConstraints(entityContext);

        if (entityContext.isCreatePermitted()) {
            entityPermissions.add(new ShortPermissionInfo(getEntityTarget(metaClass, "create"),
                    ALLOWED_CRUD_PERMISSION));
        }
        if (entityContext.isReadPermitted()) {
            entityPermissions.add(new ShortPermissionInfo(getEntityTarget(metaClass, "read"),
                    ALLOWED_CRUD_PERMISSION));
        }
        if (entityContext.isUpdatePermitted()) {
            entityPermissions.add(new ShortPermissionInfo(getEntityTarget(metaClass, "update"),
                    ALLOWED_CRUD_PERMISSION));
        }
        if (entityContext.isDeletePermitted()) {
            entityPermissions.add(new ShortPermissionInfo(getEntityTarget(metaClass, "delete"),
                    ALLOWED_CRUD_PERMISSION));
        }
    }

    protected void fillEntityAttributePermissions(MetaClass metaClass,
                                                  List<ShortPermissionInfo> entityAttributePermissions) {
        for (MetaProperty metaProperty : metaClass.getProperties()) {
            EntityAttributeContext attributeContext = new EntityAttributeContext(metaClass, metaProperty.getName());
            accessManager.applyRegisteredConstraints(attributeContext);

            if (attributeContext.canModify()) {
                entityAttributePermissions.add(new ShortPermissionInfo(
                        getEntityAttributeTarget(metaClass, metaProperty),
                        MODIFY_ATTRIBUTE_PERMISSION));
            } else if (attributeContext.canView()) {
                entityAttributePermissions.add(new ShortPermissionInfo(
                        getEntityAttributeTarget(metaClass, metaProperty),
                        VIEW_ATTRIBUTE_PERMISSION));
            }
        }
    }

    protected void fillSpecificPermissions(List<ShortPermissionInfo> specificPermissions) {
        GraphQLAccessContext downloadContext = new GraphQLAccessContext(GRAPHQL_FILE_DOWNLOAD_ENABLED);
        accessManager.applyRegisteredConstraints(downloadContext);

        if (downloadContext.isPermitted()) {
            specificPermissions.add(new ShortPermissionInfo(downloadContext.getName(), 1));
        } else {
            specificPermissions.add(new ShortPermissionInfo(downloadContext.getName(), 0));
        }

        GraphQLAccessContext uploadContext = new GraphQLAccessContext(GRAPHQL_FILE_UPLOAD_ENABLED);
        accessManager.applyRegisteredConstraints(uploadContext);

        if (uploadContext.isPermitted()) {
            specificPermissions.add(new ShortPermissionInfo(uploadContext.getName(), 1));
        } else {
            specificPermissions.add(new ShortPermissionInfo(uploadContext.getName(), 0));
        }
    }

    protected void fillScreenAndMenuPermissions(boolean screens,
                                                boolean menuItems,
                                                List<ShortPermissionInfo> screenPermissions,
                                                List<ShortPermissionInfo> menuItemPermissions) {
        Collection<? extends GrantedAuthority> grantedAuthorities = currentAuthentication.getAuthentication().getAuthorities();
        for (GrantedAuthority grantedAuthority : grantedAuthorities) {
            if (grantedAuthority instanceof RoleGrantedAuthority) {
                RoleGrantedAuthority roleGrantedAuthority = (RoleGrantedAuthority) grantedAuthority;
                roleGrantedAuthority.getResourcePolicies().forEach(resourcePolicy -> {
                    String resource = resourcePolicy.getResource();
                    if (screens && isFrontendScreenAccessGranted(resourcePolicy)) {
                        screenPermissions.add(new ShortPermissionInfo(resource, VIEW_SCREEN_PERMISSION));
                        return;
                    }
                    if (menuItems && isFrontendMenuAccessGranted(resourcePolicy)) {
                        menuItemPermissions.add(new ShortPermissionInfo(resource, VIEW_MENU_PERMISSION));
                    }
                });
            }
        }
    }

    protected boolean isFrontendScreenAccessGranted(ResourcePolicy resourcePolicy) {
        return checkResourcePolicy(resourcePolicy, "frontend_screen", ResourcePolicy.DEFAULT_ACTION, ResourcePolicy.DEFAULT_EFFECT);
    }

    protected boolean isFrontendMenuAccessGranted(ResourcePolicy resourcePolicy) {
        return checkResourcePolicy(resourcePolicy, "frontend_menu", ResourcePolicy.DEFAULT_ACTION, ResourcePolicy.DEFAULT_EFFECT);
    }

    protected boolean checkResourcePolicy(ResourcePolicy resourcePolicy, String expectedPolicyType, String expectedAction, String expectedEffect) {
        return expectedPolicyType.equals(resourcePolicy.getType())
                && expectedAction.equals(resourcePolicy.getAction())
                && expectedEffect.equals(resourcePolicy.getEffect());
    }
}
