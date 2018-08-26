import React from 'react';
import { connect } from 'dva';
import ResultList from '../components/ResultList';

const FakerManager = ({ dispatch, data }) => {

  function handleDelete(id) {
    dispatch({
      type: 'products/delete',
      payload: id,
    });
  }

  return (
    <div>
      <ResultList data={data} />
    </div>
  );
};

// export default Products;
export default connect()(FakerManager);
