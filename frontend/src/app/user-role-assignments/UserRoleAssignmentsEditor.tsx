import React from "react";
import {Button, Card, Form, Space} from "antd";
import {useForm} from "antd/es/form/Form";
import {observer} from "mobx-react";
import {FormattedMessage} from "react-intl";
import {
  ant_to_jmixFront,
  createUseAntdForm,
  createUseAntdFormValidation,
  Field,
  GlobalErrorsAlert,
  RetryDialog,
  Spinner,
  useEntityPersistCallbacks,
  useSubmitFailedCallback
} from "@haulmont/jmix-react-antd";
import {
  createAntdFormValidationMessages,
  EntityEditorProps,
  registerEntityEditor,
  useDefaultEditorHotkeys,
  useEntityEditor
} from "@haulmont/jmix-react-web";
import {gql} from "@apollo/client";
import styles from "../../app/App.module.css";
import {UserRoleAssignments} from "../../jmix/entities/umgmt_UserRoleAssignments";

const ENTITY_NAME = "umgmt_UserRoleAssignments";
const ROUTING_PATH = "/userRoleAssignmentsEditor";

const LOAD_UMGMT_USERROLEASSIGNMENTS = gql`
  query umgmt_UserRoleAssignmentsById($id: String = "", $loadItem: Boolean!) {
    umgmt_UserRoleAssignmentsById(id: $id) @include(if: $loadItem) {
      id
      _instanceName
      resourceRoles {
        id
        _instanceName
        code
        name
      }
      rowLevelRoles {
        id
        _instanceName
        code
        name
      }
    }
    umgmt_ResourceRoleList {
      id
      _instanceName
    }

    umgmt_RowLevelRoleList {
      id
      _instanceName
    }
  }
`;

const UPSERT_UMGMT_USERROLEASSIGNMENTS = gql`
  mutation Upsert_umgmt_UserRoleAssignments(
    $userRoleAssignments: inp_umgmt_UserRoleAssignments!
  ) {
    upsert_umgmt_UserRoleAssignments(
      userRoleAssignments: $userRoleAssignments
    ) {
      id
    }
  }
`;

const UserRoleAssignmentsEditor = observer(
  (props: EntityEditorProps<UserRoleAssignments>) => {
    const {
      onCommit,
      entityInstance,
      submitBtnCaption = "common.submit"
    } = props;
    const [form] = useForm();
    const onSubmitFailed = useSubmitFailedCallback();
    const {
      relationOptions,
      executeLoadQuery,
      loadQueryResult: {loading: queryLoading, error: queryError},
      upsertMutationResult: {loading: upsertLoading},
      serverValidationErrors,
      intl,
      handleSubmit,
      handleCancelBtnClick
    } = useEntityEditor<UserRoleAssignments>({
      loadQuery: LOAD_UMGMT_USERROLEASSIGNMENTS,
      upsertMutation: UPSERT_UMGMT_USERROLEASSIGNMENTS,
      entityName: ENTITY_NAME,
      routingPath: ROUTING_PATH,
      onCommit,
      entityInstance,
      persistEntityCallbacks: useEntityPersistCallbacks(),
      uiKit_to_jmixFront: ant_to_jmixFront,
      useEntityEditorForm: createUseAntdForm(form),
      useEntityEditorFormValidation: createUseAntdFormValidation(form)
    });

    useDefaultEditorHotkeys({saveEntity: form.submit});

    if (queryLoading) {
      return <Spinner/>;
    }

    if (queryError != null) {
      console.error(queryError);
      return <RetryDialog onRetry={executeLoadQuery}/>;
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
            propertyName="id"
            formItemProps={{
              style: {marginBottom: "12px"}
            }}
            disabled={true}
          />

          <Field
            entityName={ENTITY_NAME}
            propertyName="resourceRoles"
            associationOptions={relationOptions?.get("umgmt_ResourceRole")}
            formItemProps={{
              style: {marginBottom: "12px"}
            }}
          />

          <Field
            entityName={ENTITY_NAME}
            propertyName="rowLevelRoles"
            associationOptions={relationOptions?.get("umgmt_RowLevelRole")}
            formItemProps={{
              style: {marginBottom: "12px"}
            }}
          />

          <GlobalErrorsAlert serverValidationErrors={serverValidationErrors}/>

          <Form.Item style={{textAlign: "center"}}>
            <Space size={8}>
              <Button htmlType="button" onClick={handleCancelBtnClick}>
                <FormattedMessage id="common.cancel"/>
              </Button>
              <Button type="primary" htmlType="submit" loading={upsertLoading}>
                <FormattedMessage id={submitBtnCaption}/>
              </Button>
            </Space>
          </Form.Item>
        </Form>
      </Card>
    );
  }
);

registerEntityEditor({
  component: UserRoleAssignmentsEditor,
  caption: "screen.UserRoleAssignmentsEditor",
  screenId: "UserRoleAssignmentsEditor",
  entityName: ENTITY_NAME,
  menuOptions: {
    pathPattern: ROUTING_PATH,
    menuLink: ROUTING_PATH
  }
});

export default UserRoleAssignmentsEditor;
