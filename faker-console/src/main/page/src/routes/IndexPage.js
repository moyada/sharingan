import React from 'react';
import { connect } from 'dva';
import styles from './IndexPage.css';
import FakerManager from '../components/FakerManager';

function IndexPage() {
  return (
    <div className={styles.normal}>
      <FakerManager/>
    </div>
  );
}

IndexPage.propTypes = {
};

export default connect()(IndexPage);
