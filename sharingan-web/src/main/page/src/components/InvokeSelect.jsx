import React from 'react';
import { Select } from 'antd';
import request from '../utils/request';

const Option = Select.Option;

class InvokeSelect extends React.Component {
  constructor(props) {
    super(props);
    request("api/getAllInvoke.json").then(({data, err}) => {
      if(err) {
        return new Error(err);
      }
      this.setState({
        data : data.data.map(({key, value}) => <Option key={key} value={key}>{value}</Option>)
      })
    })
  }

  state = {
    data : []
  }

  render(){
		return (
			<Select placeholder='请选择调用'
              onSelect={this.props.onSelect}
              value={this.props.value}>
        {this.state.data}
      </Select>
		)
	}
}

export default InvokeSelect;
