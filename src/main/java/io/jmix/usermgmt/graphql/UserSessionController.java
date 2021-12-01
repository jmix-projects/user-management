package io.jmix.usermgmt.graphql;

import io.jmix.audit.UserSessions;
import io.jmix.core.Metadata;
import io.jmix.graphql.loader.GraphQLEntityDataFetcher;
import io.jmix.graphql.loader.GraphQLEntityDataFetcherContext;
import io.jmix.graphql.loader.GraphQLEntityListDataFetcher;
import io.jmix.graphql.loader.GraphQLEntityListDataFetcherContext;
import io.jmix.graphql.modifier.GraphQLUpsertEntityDataFetcher;
import io.jmix.graphql.modifier.GraphQLUpsertEntityDataFetcherContext;
import io.jmix.usermgmt.entity.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserSessionController implements GraphQLEntityListDataFetcher<UserSession>,
        GraphQLEntityDataFetcher<UserSession>,
        GraphQLUpsertEntityDataFetcher<UserSession> {

    @Autowired
    protected Metadata metadata;
    @Autowired
    protected UserSessions userSessions;

    @Override
    public UserSession loadEntity(GraphQLEntityDataFetcherContext<UserSession> context) {
        return null;
    }

    @Override
    public List<UserSession> loadEntityList(GraphQLEntityListDataFetcherContext<UserSession> context) {
        return userSessions.sessions()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserSession importEntities(GraphQLUpsertEntityDataFetcherContext<UserSession> context) {
        return null;
    }

    private UserSession mapToDto(io.jmix.audit.entity.UserSession src) {
        UserSession userSession = metadata.create(UserSession.class);
        userSession.setId(src.getSessionId());
        userSession.setUsername(src.getPrincipalName());
        userSession.setLastRequest(src.getLastRequest());

        return userSession;
    }
}
