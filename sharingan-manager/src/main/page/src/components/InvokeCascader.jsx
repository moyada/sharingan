import React from 'react';
import { Cascader } from 'antd';
import request from '../utils/request';

class InvokeCascader extends React.Component {
  constructor(props) {
    super(props);

    // 查询所有应用
    request("api/getAllApp.json").then(({data, err}) => {
      if(err) {
        return new Error(err);
      }
      this.setState({
        data : data.data.map(({key, value}) =>
          ({
            value: key,
            label: value,
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
      // 查询应用下服务信息
      case "app" :
        request("api/getServiceByApp.json", {appId: selected.value})
          .then(({data, err}) => {
            if(err) {
              return new Error(err);
            }
            const children = data.data.map(({key, value, extra}) =>
              ({
                value: key,
                label: value,
                extra: extra,
                children: null,
                isLeaf: false,
                type: 'service'
              })
            )
            selected.children = children
            this.setState({
              data: [...this.state.data],
            });
        })
        break

      // 查询服务下方法信息
      case "service":
        request("api/getMethodByService.json", {serviceId: selected.value})
          .then(({data, err}) => {
            if(err) {
              return new Error(err);
            }
            const children = data.data.map(({key, value, extra, header, body}) =>
              ({
                value: key,
                label: value,
                extra: extra,
                header: header,
                body: body,
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
