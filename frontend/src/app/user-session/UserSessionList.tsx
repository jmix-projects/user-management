import React, {useCallback} from "react";
import {observer} from "mobx-react";
import {LeftOutlined} from "@ant-design/icons";
import {Button, Tooltip} from "antd";
import {EntityPermAccessControl} from "@haulmont/jmix-react-core";
import {EntityListProps, registerEntityList, useEntityList} from "@haulmont/jmix-react-web";
import {
  DataTable,
  RetryDialog,
  saveHistory,
  useEntityDeleteCallback,
  useOpenScreenErrorCallback
} from "@haulmont/jmix-react-antd";
import {UserSession} from "../../jmix/entities/umgmt_UserSession";
import {FormattedMessage} from "react-intl";
import {gql, useMutation} from "@apollo/client";

const ENTITY_NAME = "umgmt_UserSession";
const ROUTING_PATH = "/userSessionList";

const UMGMT_USERSESSION_LIST = gql`
  query umgmt_UserSessionList(
    $limit: Int
    $offset: Int
    $orderBy: inp_umgmt_UserSessionOrderBy
    $filter: [inp_umgmt_UserSessionFilterCondition]
  ) {
    umgmt_UserSessionCount(filter: $filter)
    umgmt_UserSessionList(
      limit: $limit
      offset: $offset
      orderBy: $orderBy
      filter: $filter
    ) {
      id
      lastRequest
      username
    }
  }
`;


const INVALIDATE_SESSION = gql`
  mutation invalidateSession($id: String!) {
    invalidateSession(sessionId: $id)
  }
`;

const UserSessionList = observer((props: EntityListProps<UserSession>) => {
  const {entityList, onEntityListChange} = props;
  const onOpenScreenError = useOpenScreenErrorCallback();
  const onEntityDelete = useEntityDeleteCallback();
  const {
    items,
    count,
    relationOptions,
    executeListQuery,
    listQueryResult: {loading, error},
    handleSelectionChange,
    handleFilterChange,
    handleSortOrderChange,
    handlePaginationChange,
    goToParentScreen,
    entityListState
  } = useEntityList<UserSession>({
    listQuery: UMGMT_USERSESSION_LIST,
    entityName: ENTITY_NAME,
    routingPath: ROUTING_PATH,
    entityList,
    onEntityListChange,
    onPagination: saveHistory,
    onEntityDelete,
    onOpenScreenError
  });

  const [executeInvalidateSession] = useMutation(INVALIDATE_SESSION);

  const handleInvalidate = useCallback(() => {
    executeInvalidateSession({
      variables: {
        id: entityListState.selectedEntityId
      },
    }).then(() => executeListQuery())
  }, [executeListQuery, executeInvalidateSession]);

  const handleRefresh = useCallback(() => {
    executeListQuery()
  }, [executeListQuery]);

  if (error != null) {
    console.error(error);
    return <RetryDialog onRetry={executeListQuery}/>;
  }

  const buttons = [
    <EntityPermAccessControl
      entityName={ENTITY_NAME}
      operation="create"
      key="create"
    >
      <Button
        htmlType="button"
        style={{margin: "0 12px 12px 0"}}
        type="default"
        onClick={handleRefresh}
      >
        <span>
          <FormattedMessage id="jmix.usermgmt.userSessions.refresh"/>
        </span>
      </Button>
    </EntityPermAccessControl>,
    <EntityPermAccessControl
      entityName={ENTITY_NAME}
      operation="update"
      key="update"
    >
      <Button
        htmlType="button"
        style={{margin: "0 12px 12px 0"}}
        disabled={entityListState.selectedEntityId == null}
        type="default"
        onClick={handleInvalidate}
      >
        <FormattedMessage id="jmix.usermgmt.userSessions.invalidate"/>
      </Button>
    </EntityPermAccessControl>
  ];

  if (entityList != null) {
    buttons.unshift(
      <Tooltip title={<FormattedMessage id="common.back"/>} key="back">
        <Button
          htmlType="button"
          style={{margin: "0 12px 12px 0"}}
          icon={<LeftOutlined/>}
          onClick={goToParentScreen}
          key="back"
          type="default"
          shape="circle"
        />
      </Tooltip>
    );
  }

  return (
    <DataTable
      items={items}
      count={count}
      relationOptions={relationOptions}
      current={entityListState.pagination?.current}
      pageSize={entityListState.pagination?.pageSize}
      entityName={ENTITY_NAME}
      loading={loading}
      error={error}
      enableFiltersOnColumns={[]}
      enableSortingOnColumns={entityList != null ? [] : undefined}
      columnDefinitions={["id", "username", "lastRequest"]}
      onRowSelectionChange={handleSelectionChange}
      onFilterChange={handleFilterChange}
      onSortOrderChange={handleSortOrderChange}
      onPaginationChange={handlePaginationChange}
      hideSelectionColumn={true}
      buttons={buttons}
      tableProps={{
        pagination: false
      }}
    />
  );
});

registerEntityList({
  component: UserSessionList,
  caption: "screen.UserSessionList",
  screenId: "UserSessionList",
  entityName: ENTITY_NAME,
  menuOptions: {
    pathPattern: `${ROUTING_PATH}/:entityId?`,
    menuLink: ROUTING_PATH
  }
});

export default UserSessionList;
