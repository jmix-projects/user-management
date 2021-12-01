package io.jmix.usermgmt.graphql.schema;

import graphql.Scalars;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLType;
import graphql.schema.GraphQLTypeReference;
import io.jmix.graphql.NamingUtils;
import io.jmix.graphql.schema.PermissionTypesGenerator;

import java.util.ArrayList;
import java.util.List;

import static io.jmix.graphql.schema.BaseTypesGenerator.listType;

public class ExtendedPermissionTypesGenerator extends PermissionTypesGenerator {

    public List<GraphQLType> generatePermissionTypes() {
        List<GraphQLType> permissionTypes = new ArrayList<>();

        permissionTypes.add(GraphQLObjectType.newObject()
                .name(NamingUtils.TYPE_SEC_PERMISSION)
                .field(GraphQLFieldDefinition.newFieldDefinition()
                        .name("target")
                        .type(new GraphQLTypeReference(Scalars.GraphQLString.getName()))
                        .build())
                .field(GraphQLFieldDefinition.newFieldDefinition()
                        .name("value")
                        .type(new GraphQLTypeReference(Scalars.GraphQLInt.getName()))
                        .build())
                .build());

        permissionTypes.add(GraphQLObjectType.newObject()
                .name(NamingUtils.TYPE_SEC_PERMISSION_CONFIG)
                .field(GraphQLFieldDefinition.newFieldDefinition()
                        .name(NamingUtils.ENTITIES)
                        .type(listType(NamingUtils.TYPE_SEC_PERMISSION))
                        .build())
                .field(GraphQLFieldDefinition.newFieldDefinition()
                        .name(NamingUtils.ENTITY_ATTRS)
                        .type(listType(NamingUtils.TYPE_SEC_PERMISSION))
                        .build())
                .field(GraphQLFieldDefinition.newFieldDefinition()
                        .name(NamingUtils.SPECIFICS)
                        .type(listType(NamingUtils.TYPE_SEC_PERMISSION))
                        .build())
                .field(GraphQLFieldDefinition.newFieldDefinition()
                        .name("screens")
                        .type(listType(NamingUtils.TYPE_SEC_PERMISSION))
                        .build())
                .field(GraphQLFieldDefinition.newFieldDefinition()
                        .name("menus")
                        .type(listType(NamingUtils.TYPE_SEC_PERMISSION))
                        .build())
                .build());
        return permissionTypes;
    }
}
