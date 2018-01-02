import React from 'react';
import { Table, Form, Row, Col, Input, Button, message } from 'antd';
import moment from 'moment';
import 'moment/locale/zh-cn';
import InvokeForm from '../components/InvokeForm';
import request from '../utils/request';

const FormItem = Form.Item;
moment.locale('zh-cn');

const columns = [{
  title: '请求参数',
  dataIndex: 'realParam',
  width: '22%',
  render(data) {
    return <div style={{paddingLeft: 10}}> {data} </div>
  }
}, {
  title: '结果码',
  dataIndex: 'code',
  width: '6%',
  render(data) {
    if(data === 200) {
      return data
    }
    return <div style={{color: 'red'}}> {data} </div>
  }
}, {
  title: '请求结果',
  dataIndex: 'result',
  width: '20%',
}, {
  title: '异常信息',
  dataIndex: 'message',
  width: '20%',
}, {
  title: '耗时',
  dataIndex: 'spendTime',
  width: '8%',
}, {
  title: '请求时间',
  dataIndex: 'invokeTime',
  width: '12%',
  render(data) {
    return <div> {moment(data).format("YYYY-MM-DD HH:mm:ss")} </div>
  }
}];

class ResultList extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      currentPageIndex: 1,
      currentPageSize: 100,
      loading: false,
      data: [],
      fakerId: null,
      pagination: {
        current: 1,
        pageSize: 100,
        defaultPageSize: 100,
        total: 0,
        onChange: (nextPage, pageSize) => {
          this.loadData(this.state.fakerId, nextPage, pageSize);
        },
      },
    }
  }

  loadData(fakerId, pageIndex, pageSize) {
    const payload = {fakerId: fakerId, pageIndex: pageIndex, pageSize: pageSize };
    this.setState({loading: true})

    request('/faker/getMethodByFakerId.jsonp', payload)
      .then(({data, err}) => {
        if(err) {
          message.error(err)
          this.setState({loading: false})
          return
        }

        let dataSource = data.data.map(item =>
          ({
            key: item.id,
            realParam: item.realParam,
            code: item.code,
            result: item.result,
            message: item.message,
            spendTime: item.spendTime,
            invokeTime: item.invokeTime,
          })
        )

        this.state.pagination.current = data.pageIndex
        this.state.pagination.total = data.total

        this.setState({
          data: dataSource,
          loading: false,
          currentPageIndex: data.pageIndex,
          currentPageSize: data.pageSize,
        });
      })
  }

  handleSearch = (e) => {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if(err) {
        return
      }
      const fakerId = values.fakerId
      this.state.fakerId = fakerId
      this.loadData(fakerId, 1, 100)
    });
  }

  render() {
    const { getFieldDecorator } = this.props.form;

    const formItemLayout = {
      labelCol: { span: 9 },
      wrapperCol: { span: 15 },
    };

    const formItemRowLayout = {
        labelCol: {span: 6},
        wrapperCol: {span: 18},
      }
    ;

    return (
      <div>
        <InvokeForm />
        <Form
          className="ant-advanced-search-form"
          style={{paddingLeft: 20, marginTop: 30}}
          onSubmit={this.handleSearch}
        >
          <Row>
            <Col span={12} key='fakerId'>
              <FormItem {...formItemRowLayout} style={{ marginRight: '100px' }} label={`测试请求序号`}>
                {getFieldDecorator(`fakerId`, {initFieldsValue: null, rules: [{ required: true}] })(
                  <Input

                  />
                )}
              </FormItem>
            </Col>
            <Col span={4} style={{ textAlign: 'right', paddingRight: '50px', marginBottom: 10 }}>
              <Button type="primary" htmlType="submit">查询</Button>
            </Col>
          </Row>
        </Form>
        <Table
          loading={this.state.loading}
          dataSource={this.state.data}
          columns={columns}
          pagination={this.state.pagination}
        />
      </div>
    );
  }
}

export default Form.create()(ResultList);
