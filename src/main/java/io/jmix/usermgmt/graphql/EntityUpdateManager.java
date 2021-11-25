package io.jmix.usermgmt.graphql;

import io.jmix.core.EntityImportExport;
import io.jmix.core.EntityImportPlan;
import io.jmix.core.Metadata;
import io.jmix.core.metamodel.model.MetaClass;
import io.jmix.core.security.AccessDeniedException;
import io.jmix.core.validation.EntityValidationException;
import io.jmix.graphql.datafetcher.GqlEntityValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.persistence.PersistenceException;
import java.util.Collection;
import java.util.Collections;

@Component
public class EntityUpdateManager {
    @Autowired
    private EntityImportExport entityImportExport;
    @Autowired
    private Metadata metadata;

    @Nullable
    public Object updateEntity(Object entity, EntityImportPlan entityImportPlan) {
        MetaClass metaClass = metadata.getClass(entity);
        try {
            Collection<Object> objects = entityImportExport.importEntities(Collections.singletonList(entity), entityImportPlan, true);
            return objects.stream()
                    .filter(entity::equals)
                    .findFirst()
                    .orElse(null);
        } catch (EntityValidationException ex) {
            throw new GqlEntityValidationException(ex, entity, metaClass);
        } catch (PersistenceException ex) {
            throw new GqlEntityValidationException(ex, "Can't save entity to database");
        } catch (AccessDeniedException ex) {
            throw new GqlEntityValidationException(ex, "Can't save entity to database. Access denied");
        }
    }
}
