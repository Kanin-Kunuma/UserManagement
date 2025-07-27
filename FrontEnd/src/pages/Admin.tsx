import { PageHeaderWrapper } from '@ant-design/pro-components';
import React from 'react';

const Admin: React.FC = ( props ) => {
  const children = props.children;
  return (
    <PageHeaderWrapper>
      {children}
    </PageHeaderWrapper>
  );
};
export default Admin;
