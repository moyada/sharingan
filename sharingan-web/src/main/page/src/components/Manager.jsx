import React from 'react';
import { connect } from 'dva';
import ResultList from './ResultList';

const Manager = ({ dispatch, data }) => {

  return (
    <div>
      <ResultList data={data} />
    </div>
  );
};

// export default Products;
export default connect()(Manager);
