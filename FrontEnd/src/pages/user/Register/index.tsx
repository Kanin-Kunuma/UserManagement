import { LockOutlined, UserOutlined, EyeOutlined, EyeInvisibleOutlined } from '@ant-design/icons';
import { message, Tabs, Input, Form } from 'antd';
import React, { useState } from 'react';
import { history } from 'umi';
import { PLANET_LINK, SYSTEM_LOGO } from '@/constants';
import Footer from '@/components/Footer';
import { register } from '@/services/ant-design-pro/api';
import styles from './index.less';
import { LoginForm, ProFormText } from '@ant-design/pro-form';

/* ----------------------------------------------------------------------------------------------------------------------------- */

const Register: React.FC = () => {
  const [type, setType] = useState<string>('account');
  const [passwordVisible, setPasswordVisible] = useState(false);
  const [checkPasswordVisible, setCheckPasswordVisible] = useState(false);

  // 表单提交
  const handleSubmit = async (values: API.RegisterParams) => {
    const { userPassword, checkPassword } = values;
    // 校验
    if (userPassword !== checkPassword) {
      message.error('两次输入的密码不一致');
      return;
    }

    try {
      // 注册
      const id = await register(values);

      if (id) {
        const defaultLoginSuccessMessage = '注册成功！';
        message.success(defaultLoginSuccessMessage);

        /** 此方法会跳转到 redirect 参数所在的位置 */
        if (!history) return;
        const { query } = history.location;
        history.push({
          pathname: '/user/login',
          query,
        });
        return;
      }

      /*      else {
        throw new Error();
      }*/
    } catch (error: any) {
      const defaultLoginFailureMessage = '注册失败，请重试！';
      message.error(error.message ?? defaultLoginFailureMessage);
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.content}>
        <LoginForm
          submitter={{
            searchConfig: {
              submitText: '注册',
            },
          }}
          logo={<img alt="logo" src={SYSTEM_LOGO} />}
          title="编程导航知识星球"
          subTitle={
            <a href={PLANET_LINK} target="_blank" rel="noreferrer">
              最好的编程学习知识圈子
            </a>
          }
          initialValues={{
            autoLogin: true,
          }}
          onFinish={async (values) => {
            await handleSubmit(values as API.RegisterParams);
          }}
        >
          <Tabs activeKey={type} onChange={setType}>
            <Tabs.TabPane key="account" tab={'账号密码注册'} />
          </Tabs>
          {type === 'account' && (
            <>
              <ProFormText
                name="userAccount"
                fieldProps={{
                  size: 'large',
                  prefix: <UserOutlined className={styles.prefixIcon} />,
                }}
                placeholder="请输入账号"
                rules={[
                  {
                    required: true,
                    message: '账号是必填项！',
                  },
                ]}
              />
              <Form.Item
                name="userPassword"
                rules={[
                  {
                    required: true,
                    message: '密码是必填项！',
                  },
                  {
                    min: 8,
                    type: 'string',
                    message: '长度不能小于 8',
                  },
                ]}
              >
                <Input
                  type={passwordVisible ? 'text' : 'password'}
                  size="large"
                  prefix={<LockOutlined className={styles.prefixIcon} />}
                  suffix={
                    <span
                      onClick={() => setPasswordVisible(!passwordVisible)}
                      style={{ cursor: 'pointer' }}
                    >
                      {passwordVisible ? <EyeInvisibleOutlined /> : <EyeOutlined />}
                    </span>
                  }
                  placeholder="请输入密码"
                />
              </Form.Item>
              <Form.Item
                name="checkPassword"
                rules={[
                  {
                    required: true,
                    message: '确认密码是必填项！',
                  },
                  {
                    min: 8,
                    type: 'string',
                    message: '长度不能小于 8',
                  },
                ]}
              >
                <Input
                  type={checkPasswordVisible ? 'text' : 'password'}
                  size="large"
                  prefix={<LockOutlined className={styles.prefixIcon} />}
                  suffix={
                    <span
                      onClick={() => setCheckPasswordVisible(!checkPasswordVisible)}
                      style={{ cursor: 'pointer' }}
                    >
                      {checkPasswordVisible ? <EyeInvisibleOutlined /> : <EyeOutlined />}
                    </span>
                  }
                  placeholder="请再次输入密码"
                />
              </Form.Item>
              <ProFormText
                name="planetCode"
                fieldProps={{
                  size: 'large',
                  prefix: <UserOutlined className={styles.prefixIcon} />,
                }}
                placeholder="请输入星球编号"
                rules={[
                  {
                    required: true,
                    message: '星球编号是必填项！',
                  },
                ]}
              />
            </>
          )}
        </LoginForm>
      </div>
      <Footer />
    </div>
  );
};

export default Register;
