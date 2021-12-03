import {Screens} from "@haulmont/jmix-react-core";
import ChangePasswordComponent from "./ChangePasswordComponent";
import React from "react";

export function showChangePasswordComponent(username: string, basePath: string, screens?: Screens): void {
  if (screens) {
    screens.push({
      title: 'jmix.usermgmt.changePassword',
      content: React.createElement(ChangePasswordComponent, {username: username, basePath: basePath}),
      key: 'abc'
    })
  }
}