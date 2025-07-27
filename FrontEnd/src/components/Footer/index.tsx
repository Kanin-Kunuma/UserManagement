import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import {PLANET_LINK} from "@/constants";
const Footer: React.FC = () => {
  const defaultMessage = '人机玛巴荣誉出品';
  const currentYear = new Date().getFullYear();
  return (
    <DefaultFooter
      copyright={`${currentYear} ${defaultMessage}`}
      links={[
        {
          key: 'Ant Design Pro',
          title: '知识星球',
          href: PLANET_LINK,
          blankTarget:true,
        },
        {
          key: 'github',
          title: <GithubOutlined />,
          href: 'https://github.com/ant-design/ant-design-pro',
          blankTarget: true,
        },
        {
          key: 'Ant Design',
          title: 'Ciallo',
          href: 'https://yuzu-soft.com',
          blankTarget: true,
        },
      ]}
    />
  );
};
export default Footer;
