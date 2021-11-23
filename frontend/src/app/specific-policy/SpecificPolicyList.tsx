import React from "react";
import { observer } from "mobx-react";
import { PlusOutlined, LeftOutlined } from "@ant-design/icons";
import { Button, Tooltip } from "antd";
import { EntityPermAccessControl } from "@haulmont/jmix-react-core";
import {
  useEntityList,
  EntityListProps,
  registerEntityList,
  useDefaultBrowserTableHotkeys
} from "@haulmont/jmix-react-web";
import {
  DataTable,
  RetryDialog,
  useOpenScreenErrorCallback,
  useEntityDeleteCallback,
  saveHistory
} from "@haulmont/jmix-react-antd";
import { SpecificPolicy } from "../../jmix/entities/umgmt_SpecificPolicy";
import { FormattedMessage } from "react-intl";
import { gql } from "@apollo/client";

const ENTITY_NAME = "umgmt_SpecificPolicy";
const ROUTING_PATH = "/specificPolicyList";

const UMGMT_SPECIFICPOLICY_LIST = gql`
  query umgmt_SpecificPolicyList(
    $limit: Int
    $offset: Int
    $orderBy: inp_umgmt_SpecificPolicyOrderBy
    $filter: [inp_umgmt_SpecificPolicyFilterCondition]
  ) {
    umgmt_SpecificPolicyCount(filter: $filter)
    umgmt_SpecificPolicyList(
      limit: $limit
      offset: $offset
      orderBy: $orderBy
      filter: $filter
    ) {
      id
      _instanceName
      resource
    }
  }
`;

const SpecificPolicyList = observer(
  (props: EntityListProps<SpecificPolicy>) => {
    const { entityList, onEntityListChange } = props;
    const onOpenScreenError = useOpenScreenErrorCallback();
    const onEntityDelete = useEntityDeleteCallback();
    const {
      items,
      count,
      relationOptions,
      executeListQuery,
      listQueryResult: { loading, error },
      handleSelectionChange,
      handleFilterChange,
      handleSortOrderChange,
      handlePaginationChange,
      handleDeleteBtnClick,
      handleCreateBtnClick,
      handleEditBtnClick,
      goToParentScreen,
      entityListState
    } = useEntityList<SpecificPolicy>({
      listQuery: UMGMT_SPECIFICPOLICY_LIST,
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
      return <RetryDialog onRetry={executeListQuery} />;
    }

    const buttons = [
      <EntityPermAccessControl
        entityName={ENTITY_NAME}
        operation="create"
        key="create"
      >
        <Button
          htmlType="button"
          style={{ margin: "0 12px 12px 0" }}
          type="primary"
          icon={<PlusOutlined />}
          onClick={handleCreateBtnClick}
        >
          <span>
            <FormattedMessage id="common.create" />
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
          style={{ margin: "0 12px 12px 0" }}
          disabled={entityListState.selectedEntityId == null}
          type="default"
          onClick={handleEditBtnClick}
        >
          <FormattedMessage id="common.edit" />
        </Button>
      </EntityPermAccessControl>,
      <EntityPermAccessControl
        entityName={ENTITY_NAME}
        operation="delete"
        key="delete"
      >
        <Button
          htmlType="button"
          style={{ margin: "0 12px 12px 0" }}
          disabled={entityListState.selectedEntityId == null}
          onClick={handleDeleteBtnClick}
          key="remove"
          type="default"
        >
          <FormattedMessage id="common.remove" />
        </Button>
      </EntityPermAccessControl>
    ];

    if (entityList != null) {
      buttons.unshift(
        <Tooltip title={<FormattedMessage id="common.back" />} key="back">
          <Button
            htmlType="button"
            style={{ margin: "0 12px 12px 0" }}
            icon={<LeftOutlined />}
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
        enableFiltersOnColumns={entityList != null ? [] : undefined}
        enableSortingOnColumns={entityList != null ? [] : undefined}
        columnDefinitions={["resource"]}
        onRowSelectionChange={handleSelectionChange}
        onFilterChange={handleFilterChange}
        onSortOrderChange={handleSortOrderChange}
        onPaginationChange={handlePaginationChange}
        hideSelectionColumn={true}
        buttons={buttons}
      />
    );
  }
);

registerEntityList({
  component: SpecificPolicyList,
  caption: "screen.SpecificPolicyList",
  screenId: "SpecificPolicyList",
  entityName: ENTITY_NAME,
  menuOptions: {
    pathPattern: `${ROUTING_PATH}/:entityId?`,
    menuLink: ROUTING_PATH
  }
});

export default SpecificPolicyList;
