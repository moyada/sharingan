import React from 'react';
import { Cascader } from 'antd';
import request from '../utils/request';

class InvokeCascader extends React.Component {
  constructor(props) {
    super(props);
    request("faker/getAllApp.json").then(({data, err}) => {
      if(err) {
        return new Error(err);
      }
      this.setState({
        data : data.data.map(({appId, appName}) =>
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
        request("faker/getClassByApp.json", {appId: selected.value})
          .then(({data, err}) => {
            if(err) {
              return new Error(err);
            }
            const children = data.data.map(({key, value}) =>
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
        request("faker/getMethodByClass.json", {className: selected.value})
          .then(({data, err}) => {
            if(err) {
              return new Error(err);
            }
            const children = data.data.map(({id, methodName, paramType, returnType, expression}) =>
              ({
                value: id + `-` + expression,
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
