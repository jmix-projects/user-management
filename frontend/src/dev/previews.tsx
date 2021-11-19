import React from "react";
import RowLevelPolicyList from "../app/row-level-policy/RowLevelPolicyList";
import RowLevelPolicyEditor from "../app/row-level-policy/RowLevelPolicyEditor";
import RowLevelRoleList from "../app/row-level-role/RowLevelRoleList";
import RowLevelRoleEditor from "../app/row-level-role/RowLevelRoleEditor";
import { Previews, ComponentPreview } from "@haulmont/react-ide-toolbox";

export const ComponentPreviews = () => {
  return (
    <Previews>
      <ComponentPreview path="/RowLevelRoleEditor">
        <RowLevelRoleEditor />
      </ComponentPreview>
      <ComponentPreview path="/RowLevelRoleList">
        <RowLevelRoleList />
      </ComponentPreview>
      <ComponentPreview path="/RowLevelPolicyEditor">
        <RowLevelPolicyEditor />
      </ComponentPreview>
      <ComponentPreview path="/RowLevelPolicyList">
        <RowLevelPolicyList />
      </ComponentPreview>
    </Previews>
  );
};
