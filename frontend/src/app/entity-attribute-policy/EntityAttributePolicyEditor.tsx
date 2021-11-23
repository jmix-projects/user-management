import React, {useEffect, useMemo, useState} from "react";
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
import {EntityAttributePolicy} from "../../jmix/entities/umgmt_EntityAttributePolicy";
import {Metadata, useMetadata} from "@haulmont/jmix-react-core";
import {getAllPersistentEntityTypesIncludeAll, getEntityPropertyNamesIncludeAll} from "../metadata";
import {EntityTypeField} from "../entity-type-field";
import {EntityAttributeField} from "../entity-attribute-field";

const ENTITY_NAME = "umgmt_EntityAttributePolicy";
const ROUTING_PATH = "/entityAttributePolicyEditor";

const LOAD_UMGMT_ENTITYATTRIBUTEPOLICY = gql`
  query umgmt_EntityAttributePolicyById($id: String = "", $loadItem: Boolean!) {
    umgmt_EntityAttributePolicyById(id: $id) @include(if: $loadItem) {
      id
      _instanceName
      action
      attribute
      entityName
    }
  }
`;

const UPSERT_UMGMT_ENTITYATTRIBUTEPOLICY = gql`
  mutation Upsert_umgmt_EntityAttributePolicy(
    $entityAttributePolicy: inp_umgmt_EntityAttributePolicy!
  ) {
    upsert_umgmt_EntityAttributePolicy(
      entityAttributePolicy: $entityAttributePolicy
    ) {
      id
    }
  }
`;

const EntityAttributePolicyEditor = observer(
  (props: EntityEditorProps<EntityAttributePolicy>) => {
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
    } = useEntityEditor<EntityAttributePolicy>({
      loadQuery: LOAD_UMGMT_ENTITYATTRIBUTEPOLICY,
      upsertMutation: UPSERT_UMGMT_ENTITYATTRIBUTEPOLICY,
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

    const [entityType, setEntityType] = useState(null)

    const allEntityAttributes = useMemo(() => {
      if (entityType !== null) {
        return getEntityPropertyNamesIncludeAll(entityType, metadata)
      } else {
        return []
      }
    }, [metadata, entityType]);

    useEffect(() => {
      setEntityType(form.getFieldValue('entityName'))
    }, [form])

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
            onChange={setEntityType}
          />

          <EntityAttributeField
            entityName={ENTITY_NAME}
            propertyName="attribute"
            formItemProps={{
              style: {marginBottom: "12px"},
              rules: [{required: true}]
            }}
            attributeOptions={allEntityAttributes}
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
  component: EntityAttributePolicyEditor,
  caption: "screen.EntityAttributePolicyEditor",
  screenId: "EntityAttributePolicyEditor",
  entityName: ENTITY_NAME,
  menuOptions: {
    pathPattern: ROUTING_PATH,
    menuLink: ROUTING_PATH
  }
});

export default EntityAttributePolicyEditor;
