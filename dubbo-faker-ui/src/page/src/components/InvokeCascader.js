import React from 'react';
import { Cascader, message } from 'antd';
import request from '../utils/request';

class InvokeCascader extends React.Component {
  constructor(props) {
    super(props);
    request("faker/getAllApp").then(({data, err}) => {
      if(err) {
        message.error(err)
        return
      }
      this.setState({
        data : data.map(({appId, appName}) =>
          ({
            value: appId,
            label: appName,
            children: null,
            isLeaf: false,
            type: 'app'
          })
        )
      })
    })
  }

  state = {
    data : []
  }

  loadData(obj) {
    const selected = obj[obj.length - 1]

    switch (selected.type) {
      case "app" :
        request("faker/getClassByApp", {appId: selected.value})
          .then(({data, err}) => {
            if(err) {
              message.error(err)
              return
            }
            const children = data.map(({key, value}) =>
              ({
                value: key,
                label: value,
                children: null,
                isLeaf: false,
                type: 'class'
              })
            )
            selected.children = children
            this.setState({
              data: [...this.state.data],
            });
        })
        break
      case "class":
        request("faker/getMethodByClass", {className: selected.value})
          .then(({data, err}) => {
            if(err) {
              message.error(err)
              return
            }
            const children = data.map(({id, methodName, paramType, returnType}) =>
              ({
                value: id,
                label: methodName + `, ` + paramType + `, ` + returnType,
                children: null,
                isLeaf: true,
                type: 'method'
              })
            )
            selected.children = children
            this.setState({
              data: [...this.state.data],
            });
          })
        break
    }
  }

  render(){
		return (
      <Cascader
        placeholder='请选择'
        options={this.state.data}
        loadData={this.loadData.bind(this)}
        onChange={this.props.onChange}
        allowClear={this.props.allowClear?this.props.allowClear:false}
        changeOnSelect
        />
		)
	}
}

export default InvokeCascader;
