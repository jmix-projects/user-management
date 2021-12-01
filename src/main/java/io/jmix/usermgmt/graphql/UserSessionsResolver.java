package io.jmix.usermgmt.graphql;

import io.jmix.audit.UserSessions;
import io.jmix.audit.entity.UserSession;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@GraphQLApi
@Component
public class UserSessionsResolver {
    @Autowired
    protected UserSessions userSessions;

    @GraphQLMutation(name = "invalidateSession")
    public String invalidateSession(@GraphQLArgument(name = "sessionId") String sessionId) {
        UserSession userSession = userSessions.get(sessionId);
        //noinspection ConstantConditions
        if (userSession != null && userSession.getSessionInformation() != null) {
            userSessions.invalidate(userSession);
        }
        return "OK";
    }
}

