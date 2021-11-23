import React, {useMemo} from "react";
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
import {EntityPolicy} from "../../jmix/entities/umgmt_EntityPolicy";
import {Metadata, useMetadata} from "@haulmont/jmix-react-core";
import {EntityTypeField} from "../entity-type-field";
import {getAllPersistentEntityTypesIncludeAll} from "../metadata/";

const ENTITY_NAME = "umgmt_EntityPolicy";
const ROUTING_PATH = "/entityPolicyEditor";

const LOAD_UMGMT_ENTITYPOLICY = gql`
  query umgmt_EntityPolicyById($id: String = "", $loadItem: Boolean!) {
    umgmt_EntityPolicyById(id: $id) @include(if: $loadItem) {
      id
      _instanceName
      action
      entityName
    }
  }
`;

const UPSERT_UMGMT_ENTITYPOLICY = gql`
  mutation Upsert_umgmt_EntityPolicy($entityPolicy: inp_umgmt_EntityPolicy!) {
    upsert_umgmt_EntityPolicy(entityPolicy: $entityPolicy) {
      id
    }
  }
`;

const EntityPolicyEditor = observer(
  (props: EntityEditorProps<EntityPolicy>) => {
    const {
      onCommit,
      entityInstance,
      submitBtnCaption = "common.submit"
    } = props;
    const [form] = useForm();
    const onSubmitFailed = useSubmitFailedCallback();
    const {
      executeLoadQuery,
      loadQueryResult: {loading: queryLoading, error: queryError},
      upsertMutationResult: {loading: upsertLoading},
      serverValidationErrors,
      intl,
      handleSubmit,
      handleCancelBtnClick
    } = useEntityEditor<EntityPolicy>({
      loadQuery: LOAD_UMGMT_ENTITYPOLICY,
      upsertMutation: UPSERT_UMGMT_ENTITYPOLICY,
      entityName: ENTITY_NAME,
      routingPath: ROUTING_PATH,
      onCommit,
      entityInstance,
      persistEntityCallbacks: useEntityPersistCallbacks(),
      uiKit_to_jmixFront: ant_to_jmixFront,
      useEntityEditorForm: createUseAntdForm(form),
      useEntityEditorFormValidation: createUseAntdFormValidation(form)
    });

    const metadata: Metadata = useMetadata();

    const allEntityTypes = useMemo(() => {
      return getAllPersistentEntityTypesIncludeAll(metadata, intl)
    }, [metadata, intl]);

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

          <EntityTypeField
            entityName={ENTITY_NAME}
            propertyName="entityName"
            formItemProps={{
              style: {marginBottom: "12px"},
              rules: [{required: true}]
            }}
            entityTypeOptions={allEntityTypes}
          />

          <Field
            entityName={ENTITY_NAME}
            propertyName="action"
            formItemProps={{
              style: {marginBottom: "12px"},
              rules: [{required: true}]
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
  component: EntityPolicyEditor,
  caption: "screen.EntityPolicyEditor",
  screenId: "EntityPolicyEditor",
  entityName: ENTITY_NAME,
  menuOptions: {
    pathPattern: ROUTING_PATH,
    menuLink: ROUTING_PATH
  }
});

export default EntityPolicyEditor;
