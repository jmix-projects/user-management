import React, {useCallback} from "react";
import {createAntdFormValidationMessages, registerScreen, useParentScreen} from "@haulmont/jmix-react-web";
import {Button, Card, Form, Input, Space} from "antd";
import styles from "../App.module.css";
import {FormattedMessage, IntlShape, useIntl} from "react-intl";
import {gql, useMutation} from "@apollo/client";
import {notifications, NotificationType} from "@haulmont/jmix-react-antd";
import {observer} from "mobx-react";
import {ChangePasswordProps} from "./ChangePasswordProps";

const ROUTING_PATH = "/changePasswordComponent";

const CHANGE_USER_PASSWORD = gql`
  mutation changeUserPassword($username: String!, $password: String!) {
    changeUserPassword(username: $username, password: $password)
  }
`;

export const ChangePasswordComponent: React.FC<ChangePasswordProps> = observer((props: ChangePasswordProps) => {
  const {username, basePath} = props
  const [executeChangeUserPassword] = useMutation(CHANGE_USER_PASSWORD)
  const [form] = Form.useForm()
  const intl: IntlShape = useIntl()

  const goToParentScreen = useParentScreen(basePath)

  console.log(username)

  const onSubmit = useCallback(() => {
    const newPassword = form.getFieldValue('newPassword')
    const confirmNewPassword = form.getFieldValue('confirmNewPassword')

    if (newPassword === confirmNewPassword) {
      executeChangeUserPassword({
        variables: {
          username: username,
          password: newPassword
        },
      })
    } else {
      notifications.show({
        title: intl.formatMessage({id: 'jmix.usermgmt.changePassword.error'}),
        description: intl.formatMessage({id: 'jmix.usermgmt.changePassword.passwordsDoNotMatch'}),
        type: NotificationType.ERROR,
        key: '1',
        duration: null,
      });
    }
  }, [intl, form, executeChangeUserPassword]);

  return (
    <Card className={styles.narrowLayout}>
      <Form
        form={form}
        name="basic"
        layout="vertical"
        initialValues={{username: username}}
        onFinish={onSubmit}
        autoComplete="off"
        validateMessages={createAntdFormValidationMessages(intl)}
      >

        <Form.Item
          label={intl.formatMessage({id: 'jmix.usermgmt.changePassword.username'})}
          name="username"
          style={{marginBottom: "12px"}}
        >
          <Input disabled={true}/>
        </Form.Item>


        <Form.Item
          label={intl.formatMessage({id: 'jmix.usermgmt.changePassword.newPassword'})}
          name="newPassword"
          rules={[
            {
              required: true,
              message: intl.formatMessage({id: 'jmix.usermgmt.changePassword.requiredPassword'}),
            },
          ]}
          style={{marginBottom: "12px"}}
        >
          <Input.Password/>
        </Form.Item>

        <Form.Item
          label={intl.formatMessage({id: 'jmix.usermgmt.changePassword.confirmNewPassword'})}
          name="confirmNewPassword"
          rules={[
            {
              required: true,
              message: intl.formatMessage({id: 'jmix.usermgmt.changePassword.requiredPassword'}),
            },
          ]}
          style={{marginBottom: "12px"}}
        >
          <Input.Password/>
        </Form.Item>

        <Form.Item style={{textAlign: "center"}}>
          <Space size={8}>
            <Button htmlType="button" onClick={goToParentScreen}>
              <FormattedMessage id="common.cancel"/>
            </Button>
            <Button type="primary" htmlType="submit">
              <FormattedMessage id="common.submit"/>
            </Button>
          </Space>
        </Form.Item>

      </Form>
    </Card>
  );
});

registerScreen({
  component: ChangePasswordComponent,
  caption: "screen.ChangePasswordComponent",
  screenId: "ChangePasswordComponent",
  menuOptions: {
    pathPattern: ROUTING_PATH,
    menuLink: ROUTING_PATH
  }
});

export default ChangePasswordComponent;
