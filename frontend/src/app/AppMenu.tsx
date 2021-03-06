import { MenuProps } from "antd";
import React from "react";
import { AddonsMenu, MenuItem, VerticalMenu } from "@haulmont/jmix-react-antd";
import { BarsOutlined, HomeOutlined } from "@ant-design/icons";

export interface AppMenuProps extends MenuProps {}

export const AppMenu = (props: AppMenuProps) => {
  return (
    <VerticalMenu {...props}>
      <MenuItem
        screenId="HomePage"
        icon={<HomeOutlined />}
        caption={"screen.home"}
        key={"home"}
      />
      <MenuItem
        screenId={"RowLevelRoleList"}
        icon={<BarsOutlined />}
        caption={"screen.RowLevelRoleList"}
        key={"b7d60eec-efaa-4e9f-980b-a5e18accdb82"}
      />
      <MenuItem
        screenId={"ResourceRoleList"}
        icon={<BarsOutlined />}
        caption={"screen.ResourceRoleList"}
        key={"44d6767e-3cdb-45de-b5c9-852c78877f4f"}
      />
      <MenuItem
        screenId={"UserList"}
        icon={<BarsOutlined />}
        caption={"screen.UserList"}
        key={"3002cfdf-c239-4fff-92a7-3e117684fa3c"}
      />
      <MenuItem
        screenId={"UserSessionList"}
        icon={<BarsOutlined />}
        caption={"screen.UserSessionList"}
        key={"3002cfdf-c239-4fff-92a7-3e1176e4fa3c"}
      />
      <AddonsMenu key={"addonsMenu"} />
    </VerticalMenu>
  );
};
