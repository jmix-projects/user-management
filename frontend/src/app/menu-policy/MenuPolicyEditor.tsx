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
import { MenuPolicy } from "../../jmix/entities/umgmt_MenuPolicy";

const ENTITY_NAME = "umgmt_MenuPolicy";
const ROUTING_PATH = "/menuPolicyEditor";

const LOAD_UMGMT_MENUPOLICY = gql`
  query umgmt_MenuPolicyById($id: String = "", $loadItem: Boolean!) {
    umgmt_MenuPolicyById(id: $id) @include(if: $loadItem) {
      id
      _instanceName
      resource
    }
  }
`;

const UPSERT_UMGMT_MENUPOLICY = gql`
  mutation Upsert_umgmt_MenuPolicy($menuPolicy: inp_umgmt_MenuPolicy!) {
    upsert_umgmt_MenuPolicy(menuPolicy: $menuPolicy) {
      id
    }
  }
`;

const MenuPolicyEditor = observer((props: EntityEditorProps<MenuPolicy>) => {
  const {
    onCommit,
    entityInstance,
    submitBtnCaption = "common.submit"
  } = props;
  const [form] = useForm();
  const onSubmitFailed = useSubmitFailedCallback();
  const {
    executeLoadQuery,
    loadQueryResult: { loading: queryLoading, error: queryError },
    upsertMutationResult: { loading: upsertLoading },
    serverValidationErrors,
    intl,
    handleSubmit,
    handleCancelBtnClick
  } = useEntityEditor<MenuPolicy>({
    loadQuery: LOAD_UMGMT_MENUPOLICY,
    upsertMutation: UPSERT_UMGMT_MENUPOLICY,
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
          propertyName="resource"
          formItemProps={{
            style: { marginBottom: "12px" },
            rules: [{ required: true }]
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
});

registerEntityEditor({
  component: MenuPolicyEditor,
  caption: "screen.MenuPolicyEditor",
  screenId: "MenuPolicyEditor",
  entityName: ENTITY_NAME,
  menuOptions: {
    pathPattern: ROUTING_PATH,
    menuLink: ROUTING_PATH
  }
});

export default MenuPolicyEditor;
