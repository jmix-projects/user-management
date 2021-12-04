package io.jmix.usermgmt.graphql;

import io.jmix.core.AccessManager;
import io.jmix.core.Metadata;
import io.jmix.core.accesscontext.CrudEntityContext;
import io.jmix.core.metamodel.model.MetaClass;
import io.jmix.core.security.UserManager;
import io.jmix.core.security.UserRepository;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@GraphQLApi
@Component
public class ChangeUserPasswordResolver {
    @Autowired
    private UserManager userManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Metadata metadata;
    @Autowired
    private AccessManager accessManager;

    private static final Logger log = LoggerFactory.getLogger(ChangeUserPasswordResolver.class);

    @GraphQLMutation(name = "changeUserPassword")
    public String changeUserPassword(@GraphQLArgument(name = "username") String username, @GraphQLArgument(name = "password") String password) {
        Class<? extends UserDetails> userClass = userRepository.getSystemUser().getClass();
        MetaClass metaClass = metadata.findClass(userClass);
        if (metaClass != null) {
            CrudEntityContext entityContext = new CrudEntityContext(metaClass);
            accessManager.applyRegisteredConstraints(entityContext);
            if (entityContext.isUpdatePermitted()) {
                try {
                    userManager.changePassword(username, null, password);
                    return "OK";
                } catch (Exception e) {
                    log.error("Unable to change password", e);
                }
            }
        }
        throw new RuntimeException("Unable to change password");
    }
}

