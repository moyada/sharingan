import React from 'react';
import { connect } from 'dva';
import styles from './IndexPage.css';
import Manager from '../components/Manager';

function IndexPage() {
  return (
    <div className={styles.normal}>
      <Manager/>
    </div>
  );
}

IndexPage.propTypes = {
};

export default connect()(IndexPage);
