import { FC, useEffect } from 'react';

const INTEGRATION_NAME = 'integrationName';
const URL = 'url';
const PROJECT = 'project';
const OWNER = 'owner';
const API_TOKEN = 'apiToken';

export const IntegrationFormFields: FC<any> = (props) => {
  const { initialize, disabled, lineAlign, initialData, updateMetaData, ...extensionProps } = props;
  const {
    components: { FieldErrorHint, FieldElement, FieldText, FieldTextFlex },
    validators: { requiredField, btsUrl, btsIntegrationName },
    constants: { SECRET_FIELDS_KEY },
  } = extensionProps;

  useEffect(() => {
    initialize(initialData);
    updateMetaData({
      [SECRET_FIELDS_KEY]: [API_TOKEN],
    });
  }, []);

  return (
    <>
      <FieldElement
        name={INTEGRATION_NAME}
        label="Integration Name"
        disabled={disabled}
        validate={btsIntegrationName}
        isRequired
      >
        <FieldErrorHint provideHint={false}>
          <FieldText defaultWidth={false} />
        </FieldErrorHint>
      </FieldElement>
      <FieldElement name={URL} label="Link to BTS" validate={btsUrl} disabled={disabled} isRequired>
        <FieldErrorHint provideHint={false}>
          <FieldText defaultWidth={false} />
        </FieldErrorHint>
      </FieldElement>
      <FieldElement
        name={PROJECT}
        label="Project ID in BTS"
        disabled={disabled}
        validate={requiredField}
        isRequired
      >
        <FieldErrorHint provideHint={false}>
          <FieldText defaultWidth={false} />
        </FieldErrorHint>
      </FieldElement>
      <FieldElement
        name={OWNER}
        label="Owner ID in BTS"
        disabled={disabled}
        validate={requiredField}
        isRequired
      >
        <FieldErrorHint provideHint={false}>
          <FieldText defaultWidth={false} />
        </FieldErrorHint>
      </FieldElement>
      <FieldElement
        name={API_TOKEN}
        label="Access Token"
        disabled={disabled}
        validate={requiredField}
        isRequired
      >
        <FieldErrorHint provideHint={false}>
          <FieldTextFlex />
        </FieldErrorHint>
      </FieldElement>
    </>
  );
};
