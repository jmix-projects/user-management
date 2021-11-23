import {Select} from "antd";
import React from "react";
import {AttributeInfo} from "../metadata";

interface Props {
  onChange?: (value: any) => void,
  attributeOptions: AttributeInfo[]
  value: any
}

export const EntityAttributeSelect: React.FC<Props> = ({value, attributeOptions, onChange}: Props) => {
  return (
    <Select
      onChange={onChange}
      value={value}>
      {attributeOptions.map(({attribute, entityName}: AttributeInfo) => {
        return (
          <Select.Option
            value={attribute}
            key={attribute}>
            {getAttributeOptionCaption({attribute, entityName})}
          </Select.Option>
        )
      })}
    </Select>
  )
}

function getAttributeOptionCaption({attribute, entityName}: AttributeInfo): string {
  return `${attribute}`;
}
