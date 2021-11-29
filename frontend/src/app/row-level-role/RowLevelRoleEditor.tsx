import React from "react";
import { Form, Button, Card, Space } from "antd";
import { useForm } from "antd/es/form/Form";
import { observer } from "mobx-react";
import { FormattedMessage } from "react-intl";
import {
  createUseAntdForm,
  createUseAntdFormValidation,
  RetryDialog,
  Field,
  GlobalErrorsAlert,
  Spinner,
  TextArea,
  useEntityPersistCallbacks,
  useSubmitFailedCallback,
  ant_to_jmixFront
} from "@haulmont/jmix-react-antd";
import {
  createAntdFormValidationMessages,
  useEntityEditor,
  EntityEditorProps,
  registerEntityEditor,
  useDefaultEditorHotkeys
} from "@haulmont/jmix-react-web";
import { gql } from "@apollo/client";
import styles from "../../app/App.module.css";
import { RowLevelRole } from "../../jmix/entities/umgmt_RowLevelRole";

const ENTITY_NAME = "umgmt_RowLevelRole";
const ROUTING_PATH = "/rowLevelRoleEditor";

const LOAD_UMGMT_ROWLEVELROLE = gql`
  query umgmt_RowLevelRoleById($id: String = "", $loadItem: Boolean!) {
    umgmt_RowLevelRoleById(id: $id) @include(if: $loadItem) {
      id
      _instanceName
      childRoles
      code
      description
      name
      rowLevelPolicies {
        id
        _instanceName
        action
        entityName
        joinClause
        script
        type
        whereClause
      }
    }
  }
`;

const UPSERT_UMGMT_ROWLEVELROLE = gql`
  mutation Upsert_umgmt_RowLevelRole($rowLevelRole: inp_umgmt_RowLevelRole!) {
    upsert_umgmt_RowLevelRole(rowLevelRole: $rowLevelRole) {
      id
    }
  }
`;

const RowLevelRoleEditor = observer(
  (props: EntityEditorProps<RowLevelRole>) => {
    const {
      onCommit,
      entityInstance,
      submitBtnCaption = "common.submit"
    } = props;
    const [form] = useForm();
    const onSubmitFailed = useSubmitFailedCallback();
    const {
      item,
      executeLoadQuery,
      loadQueryResult: { loading: queryLoading, error: queryError },
      upsertMutationResult: { loading: upsertLoading },
      serverValidationErrors,
      intl,
      handleSubmit,
      handleCancelBtnClick
    } = useEntityEditor<RowLevelRole>({
      loadQuery: LOAD_UMGMT_ROWLEVELROLE,
      upsertMutation: UPSERT_UMGMT_ROWLEVELROLE,
      entityName: ENTITY_NAME,
      routingPath: ROUTING_PATH,
      onCommit,
      entityInstance,
      persistEntityCallbacks: useEntityPersistCallbacks(),
      uiKit_to_jmixFront: ant_to_jmixFront,
      useEntityEditorForm: createUseAntdForm(form),
      useEntityEditorFormValidation: createUseAntdFormValidation(form)
    });

    useDefaultEditorHotkeys({ saveEntity: form.submit });

    if (queryLoading) {
      return <Spinner />;
    }

    if (queryError != null) {
      console.error(queryError);
      return <RetryDialog onRetry={executeLoadQuery} />;
    }

    return (
      <Card className={styles.narrowLayout}>
        <Form
          onFinish={handleSubmit}
          onFinishFailed={onSubmitFailed}
          layout="vertical"
          form={form}
          validateMessages={createAntdFormValidationMessages(intl)}
        >

          <Field
            entityName={ENTITY_NAME}
            propertyName="code"
            formItemProps={{
              style: { marginBottom: "12px" },
              rules: [{ required: true }]
            }}
          />

          <Field
            entityName={ENTITY_NAME}
            propertyName="name"
            formItemProps={{
              style: { marginBottom: "12px" },
              rules: [{ required: true }]
            }}
          />

          <TextArea
            entityName={ENTITY_NAME}
            propertyName="description"
            formItemProps={{
              style: { marginBottom: "12px" }
            }}
          />

          <Field
            entityName={ENTITY_NAME}
            propertyName="rowLevelPolicies"
            formItemProps={{
              style: { marginBottom: "12px" }
            }}
          />

          <GlobalErrorsAlert serverValidationErrors={serverValidationErrors} />

          <Form.Item style={{ textAlign: "center" }}>
            <Space size={8}>
              <Button htmlType="button" onClick={handleCancelBtnClick}>
                <FormattedMessage id="common.cancel" />
              </Button>
              <Button type="primary" htmlType="submit" loading={upsertLoading}>
                <FormattedMessage id={submitBtnCaption} />
              </Button>
            </Space>
          </Form.Item>
        </Form>
      </Card>
    );
  }
);

registerEntityEditor({
  component: RowLevelRoleEditor,
  caption: "screen.RowLevelRoleEditor",
  screenId: "RowLevelRoleEditor",
  entityName: ENTITY_NAME,
  menuOptions: {
    pathPattern: ROUTING_PATH,
    menuLink: ROUTING_PATH
  }
});

export default RowLevelRoleEditor;
