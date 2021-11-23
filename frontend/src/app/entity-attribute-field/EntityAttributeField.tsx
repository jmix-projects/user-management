import {JmixFormFieldProps, JmixFormFieldWrapper} from "@haulmont/jmix-react-antd";
import {EntityAttributeSelect} from "./EntityAttributeSelect";
import React from "react";
import {AttributeInfo} from "../metadata";


export type EntityAttributeFieldProps = { attributeOptions: AttributeInfo[] }

export function EntityAttributeField({
                                       entityName,
                                       propertyName,
                                       formItemProps,
                                       value,
                                       onChange,
                                       attributeOptions,
                                       ...rest
                                     }: JmixFormFieldProps & EntityAttributeFieldProps) {
  const passedValue = value ? value : "";

  return (
    <JmixFormFieldWrapper
      entityName={entityName}
      propertyName={propertyName}
      formItemProps={formItemProps}
      renderField={isReadOnly =>
        <EntityAttributeSelect
          value={passedValue}
          attributeOptions={attributeOptions}
          onChange={onChange}
        />
      }
    />
  );
}