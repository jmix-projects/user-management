import React, {useEffect, useState} from "react";
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
  TextArea,
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
import {RowLevelPolicy} from "../../jmix/entities/umgmt_RowLevelPolicy";
import {RowLevelType} from "../../jmix/enums/enums";

const ENTITY_NAME = "umgmt_RowLevelPolicy";
const ROUTING_PATH = "/rowLevelPolicyEditor";

const LOAD_UMGMT_ROWLEVELPOLICY = gql`
  query umgmt_RowLevelPolicyById($id: String = "", $loadItem: Boolean!) {
    umgmt_RowLevelPolicyById(id: $id) @include(if: $loadItem) {
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
`;

const UPSERT_UMGMT_ROWLEVELPOLICY = gql`
  mutation Upsert_umgmt_RowLevelPolicy(
    $rowLevelPolicy: inp_umgmt_RowLevelPolicy!
  ) {
    upsert_umgmt_RowLevelPolicy(rowLevelPolicy: $rowLevelPolicy) {
      id
    }
  }
`;

const RowLevelPolicyEditor = observer(
  (props: EntityEditorProps<RowLevelPolicy>) => {
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
    } = useEntityEditor<RowLevelPolicy>({
      loadQuery: LOAD_UMGMT_ROWLEVELPOLICY,
      upsertMutation: UPSERT_UMGMT_ROWLEVELPOLICY,
      entityName: ENTITY_NAME,
      routingPath: ROUTING_PATH,
      onCommit,
      entityInstance,
      persistEntityCallbacks: useEntityPersistCallbacks(),
      uiKit_to_jmixFront: ant_to_jmixFront,
      useEntityEditorForm: createUseAntdForm(form),
      useEntityEditorFormValidation: createUseAntdFormValidation(form)
    });

    const [type, setType] = useState<any>();

    function handleTypeChange(value) {
      setType(value)
    }

    useEffect(() => {
      setType(form.getFieldValue('type'))
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
          <Field
            entityName={ENTITY_NAME}
            propertyName="action"
            formItemProps={{
              style: {marginBottom: "12px"},
              rules: [{required: true}]
            }}
          />

          <Field
            entityName={ENTITY_NAME}
            propertyName="entityName"
            formItemProps={{
              style: {marginBottom: "12px"},
              rules: [{required: true}]
            }}
          />

          <Field
            entityName={ENTITY_NAME}
            propertyName="type"
            formItemProps={{
              style: {marginBottom: "12px"},
              rules: [{required: true}]
            }}
            componentProps={{
              onChange: handleTypeChange
            }}
          />

          <TextArea
            entityName={ENTITY_NAME}
            propertyName="script"
            formItemProps={{
              style: {marginBottom: "12px"},
              hidden: type === RowLevelType.JPQL
            }}
          />

          <TextArea
            entityName={ENTITY_NAME}
            propertyName="joinClause"
            formItemProps={{
              style: {marginBottom: "12px"},
              hidden: type === RowLevelType.PREDICATE
            }}
          />

          <TextArea
            entityName={ENTITY_NAME}
            propertyName="whereClause"
            formItemProps={{
              style: {marginBottom: "12px"},
              hidden: type === RowLevelType.PREDICATE
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
  component: RowLevelPolicyEditor,
  caption: "screen.RowLevelPolicyEditor",
  screenId: "RowLevelPolicyEditor",
  entityName: ENTITY_NAME,
  menuOptions: {
    pathPattern: ROUTING_PATH,
    menuLink: ROUTING_PATH
  }
});

export default RowLevelPolicyEditor;
