import React from "react";
import {observer} from "mobx-react";
import {LeftOutlined, PlusOutlined} from "@ant-design/icons";
import {Button, Tooltip} from "antd";
import {EntityPermAccessControl} from "@haulmont/jmix-react-core";
import {
  EntityListProps,
  registerEntityList,
  useDefaultBrowserTableHotkeys,
  useEntityList
} from "@haulmont/jmix-react-web";
import {
  DataTable,
  RetryDialog,
  saveHistory,
  useEntityDeleteCallback,
  useOpenScreenErrorCallback
} from "@haulmont/jmix-react-antd";
import {RowLevelRole} from "../../jmix/entities/umgmt_RowLevelRole";
import {FormattedMessage} from "react-intl";
import {gql} from "@apollo/client";

const ENTITY_NAME = "umgmt_RowLevelRole";
const ROUTING_PATH = "/rowLevelRoleList";

const UMGMT_ROWLEVELROLE_LIST = gql`
  query umgmt_RowLevelRoleList(
    $limit: Int
    $offset: Int
    $orderBy: inp_umgmt_RowLevelRoleOrderBy
    $filter: [inp_umgmt_RowLevelRoleFilterCondition]
  ) {
    umgmt_RowLevelRoleCount(filter: $filter)
    umgmt_RowLevelRoleList(
      limit: $limit
      offset: $offset
      orderBy: $orderBy
      filter: $filter
    ) {
      id
      _instanceName
      code
      name
    }
  }
`;

const RowLevelRoleList = observer((props: EntityListProps<RowLevelRole>) => {
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
    handleDeleteBtnClick,
    handleCreateBtnClick,
    handleEditBtnClick,
    goToParentScreen,
    entityListState
  } = useEntityList<RowLevelRole>({
    listQuery: UMGMT_ROWLEVELROLE_LIST,
    entityName: ENTITY_NAME,
    routingPath: ROUTING_PATH,
    entityList,
    onEntityListChange,
    onPagination: saveHistory,
    onEntityDelete,
    onOpenScreenError
  });

  useDefaultBrowserTableHotkeys({
    selectedEntityId: entityListState.selectedEntityId,
    handleCreateBtnClick,
    handleEditBtnClick,
    handleDeleteBtnClick
  });

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
        type="primary"
        icon={<PlusOutlined/>}
        onClick={handleCreateBtnClick}
      >
        <span>
          <FormattedMessage id="common.create"/>
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
        onClick={handleEditBtnClick}
      >
        <FormattedMessage id="common.edit"/>
      </Button>
    </EntityPermAccessControl>,
    <EntityPermAccessControl
      entityName={ENTITY_NAME}
      operation="delete"
      key="delete"
    >
      <Button
        htmlType="button"
        style={{margin: "0 12px 12px 0"}}
        disabled={entityListState.selectedEntityId == null}
        onClick={handleDeleteBtnClick}
        key="remove"
        type="default"
      >
        <FormattedMessage id="common.remove"/>
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
      columnDefinitions={["code", "name"]}
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
  component: RowLevelRoleList,
  caption: "screen.RowLevelRoleList",
  screenId: "RowLevelRoleList",
  entityName: ENTITY_NAME,
  menuOptions: {
    pathPattern: `${ROUTING_PATH}/:entityId?`,
    menuLink: ROUTING_PATH
  }
});

export default RowLevelRoleList;
