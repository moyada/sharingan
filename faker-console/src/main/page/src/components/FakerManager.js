import React from 'react';
import { connect } from 'dva';
import ResultList from '../components/ResultList';

const FakerManager = ({ dispatch, data }) => {

  return (
    <div>
      <ResultList data={data} />
    </div>
  );
};

// export default Products;
export default connect()(FakerManager);
