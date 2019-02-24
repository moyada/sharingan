import React from 'react';
import { Tabs, Form, Table, Row, Col, Input, InputNumber, Button, Radio, Modal, message, notification } from 'antd';
import InvokeCascader from './InvokeCascader';
import request from "../utils/request";

const TextArea = Input.TextArea;
const TabPane = Tabs.TabPane;
const FormItem = Form.Item;
const RadioButton = Radio.Button;
const RadioGroup = Radio.Group;


const HTTP_TYPE = "HTTP";

class InvokeForm extends React.PureComponent {
  state = {
    expand: false,
    data: [],
    visible: false,
    index: 1,
  }

  // 校验表达式是否为json格式
  static isJSON(str) {
    if (null === str) {
      return true
    }

    if (typeof str === 'string') {
      try {
        JSON.parse(str)
        return true
      } catch (e) {
        console.log(e)
        return false
      }
    }
    return false
  }

  handleSearch = (e) => {
    e.preventDefault()
    this.props.form.validateFields((err, values) => {
      if(err) {
        return
      }
      let payload
      if(null == values.invokeId || values.invokeId === undefined || values.invokeId.length !== 3) {
        message.error("请选择调用请求")
        return
      }
      if(values.expression === undefined || !InvokeForm.isJSON(values.expression)) {
        message.error("请输入正确的参数表达式")
        return
      }

      payload = {
        appId: values.invokeId[0],
        serviceId: values.invokeId[1],
        invokeId: values.invokeId[2],
        header: values.header,
        body: values.body,
        expression: values.expression,
        concurrent: values.concurrent,
        qps: values.qps,
        random: values.random,
        total: values.total,
        saveResult: values.saveResult,
        resultParam: values.resultParam,
      }

      message.success("生成测试请求")

      request("api/invoke.json", payload, 'POST')
        .then(resp => {
          if(resp.err) {
            message.error(resp.err.message, 10)
            return
          }

          if(resp.data.code === 200) {
            notification.open({
              message: '请求成功',
              description: resp.data.data,
              duration: 20
            });
          }
          else {
            message.error(resp.data.msg, 10)
          }
        })
    })
  }

  handleReset = () => {
    this.props.form.resetFields()
  }

  onSelectInvoke(id, value) {
    if(id === undefined || null === id) {
      return
    }

    switch (id.length) {
      case 2:
        this.setState({
          type: value[1].extra
        })
        break
      case 3:
        if (this.state.type === HTTP_TYPE) {
          console.log(value[2])
          this.props.form.setFieldsValue({
            expression: value[2].extra,
            header: value[2].header,
            body: value[2].body
          })
          return
        }

        this.props.form.setFieldsValue({
          expression: value[2].extra
        })
        break
    }
  }

  showModal = () => {
    this.setState({
      visible: true,
    });
  }

  removeHttp = (key) => {
    return (event) => {
      event.preventDefault();
      let data = this.state.data
      data = data.filter(e => e.key !== key)
      this.setState({
        data: data
      })
    }
  }

  handleOk = (e) => {
    const {url, method, header, cookie, param} = this.props.form.getFieldsValue(["url", "method", "header", "cookie", "param"])
    if(url === undefined || null === url) {

      notification.error({
        message: '错误',
        description: 'url 不能为空',
        duration: 5
      });
      return
    }
    if(method === undefined || null === method) {

      notification.error({
        message: '错误',
        description: '请求方法 不能为空',
        duration: 5
      });
      return
    }

    if(null != header && header !== undefined && header.trim() !== "" &&
      header.charAt(0) === '{' && !InvokeForm.isJSON(header)) {

      notification.error({
        message: '错误',
        description: '请求头非JSON格式',
        duration: 5
      });
      return
    }

    if(null != cookie && cookie !== undefined && cookie.trim() !== "" &&
      cookie.charAt(0) === '{' && !InvokeForm.isJSON(cookie)) {

      notification.error({
        message: '错误',
        description: 'cookie非JSON格式',
        duration: 5
      });
      return
    }

    if(null != param && param !== undefined && param.trim() !== "" &&
      param.charAt(0) === '{' && !InvokeForm.isJSON(param)) {

      notification.error({
        message: '错误',
        description: '请求参数非JSON格式',
        duration: 5
      });
      return
    }

    this.state.data.push({
      key: `_` + this.state.index,
      url: url,
      method: method,
      header: header,
      cookie: cookie,
      param: param,
    })
    this.props.form.resetFields(["url", "method", "header", "cookie", "param"])
    this.setState({
      index: this.state.index + 1,
      visible: false,
    });
  }

  render() {
    const { getFieldDecorator } = this.props.form;

    const formItemLayout = {
      labelCol: { span: 9 },
      wrapperCol: { span: 15 },
    };

    const formItemHttpLayout = {
      labelCol: { span: 5 },
      wrapperCol: { span: 18 },
    };

    const formItemRowLayout = {
        labelCol: {span: 2},
        wrapperCol: {span: 22},
      }
    ;

    const columns = [{
      title: '编号',
      dataIndex: 'key',
      width: '5%'
    }, {
      title: '请求链接',
      dataIndex: 'url',
      width: '18%'
    }, {
      title: '请求方法',
      dataIndex: 'method',
      width: '8%'
    }, {
      title: '请求头',
      dataIndex: 'header',
      width: '20%'
    }, {
      title: 'cookie',
      dataIndex: 'cookie',
      width: '20%'
    }, {
      title: '请求参数',
      dataIndex: 'param',
      width: '25%'
    }, {
      title: '操作',
      dataIndex: 'operate',
      width: '5%',
      render: (data, record) => {
        return (
          <div>
            <a onClick={this.removeHttp(record.key)}>删除</a>
          </div>
        )
      }
    }];

    return (
      <Form
        className="ant-advanced-search-form"
        style={{backgroundColor : '#EEEEEE', paddingLeft: 20}}
        onSubmit={this.handleSearch}
      >
        <Row>
          <Tabs defaultActiveKey="simple" onChange={(key) => this.state.type = key }>
            <TabPane tab="接口测试" key="simple">
              <Col span={24} key='invokeId'>
                <FormItem {...formItemRowLayout} style={{ marginRight: '100px', marginTop: '20px' }} label={`请求`}>
                  {getFieldDecorator(`invokeId`, {initialValue: null})(
                    <InvokeCascader
                      onChange={this.onSelectInvoke.bind(this)}
                    />
                  )}
                </FormItem>
              </Col>
              <Col span={24} key='expression'>
                {
                  this.state.type === HTTP_TYPE ?
                    <Row>
                      <Col span={8} key='param'>
                        <FormItem {...formItemHttpLayout} style={{ marginRight: '10px' }} label={`参数`}>
                          {getFieldDecorator(`expression`, {initialValue: null})(
                            <TextArea
                              maxLength={1000}
                              placeholder='{"key" : "${project.domain}"}'
                              autosize
                            />
                          )}
                        </FormItem>
                      </Col>
                      <Col span={8} key='header'>
                        <FormItem {...formItemHttpLayout} style={{ marginRight: '10px' }} label={`头信息`}>
                          {getFieldDecorator(`header`, {initialValue: null})(
                            <TextArea
                              maxLength={1000}
                              placeholder='{"key" : "value"}'
                              autosize
                            />
                          )}
                        </FormItem>
                      </Col>
                      <Col span={8} key='body'>
                        <FormItem {...formItemHttpLayout} label={`请求体`}>
                          {getFieldDecorator(`body`, {initialValue: null})(
                            <TextArea
                              maxLength={1000}
                              autosize
                            />
                          )}
                        </FormItem>
                      </Col>
                    </Row>
                  :
                    <FormItem {...formItemRowLayout} style={{ marginRight: '100px' }} label={`参数表达式`}>
                    {getFieldDecorator(`expression`, {initialValue: null})(
                      <TextArea
                        maxLength={20000}
                        placeholder='["${project.domain}", "#{int.random}"]'
                        autosize
                      />
                    )}
                    </FormItem>
                }

              </Col>
            </TabPane>
          </Tabs>

          <Col span={1} key='NULL'/>

          <Col span={3} key='concurrent'>
            <FormItem {...formItemLayout} label={`并发数`} >
              {getFieldDecorator(`concurrent`, {initialValue: null})(
                <InputNumber
                  min={1}
                  max={300}
                  precision={0}
                  // disabled
                />
              )}
            </FormItem>
          </Col>

          <Col span={3} key='total'>
            <FormItem {...formItemLayout} label={`请求次数`}>
              {getFieldDecorator(`total`, {initialValue: null})(
                <InputNumber
                  // onChange={(total) =>this.changePollSize(null, total)}
                  min={1}
                  max={100000}
                  precision={0}
                />
              )}
            </FormItem>
          </Col>
          <Col span={3} key='qps'>
            <FormItem labelCol={{span : 11}} wrapperCol={{span : 11}} label={`每秒请求数`}>
              {getFieldDecorator(`qps`, {initialValue: null})(
                <InputNumber
                  // onChange={(qps) => this.changePollSize(qps, null)}
                  min={1}
                  max={10000}
                  precision={0}
                />
              )}
            </FormItem>
          </Col>
          <Col span={3} key='random'>
            <FormItem {...formItemLayout} label={`随机取参`}>
              {getFieldDecorator(`random`, {initialValue: true })(
                <RadioGroup >
                  <RadioButton value={true}>是</RadioButton>
                  <RadioButton value={false}>否</RadioButton>
                </RadioGroup>
              )}
            </FormItem>
          </Col>
          <Col span={3} key='saveResult'>
            <FormItem {...formItemLayout} label={`保存结果`}>
              {getFieldDecorator(`saveResult`, {initialValue: true })(
                <RadioGroup >
                  <RadioButton value={true}>是</RadioButton>
                  <RadioButton value={false}>否</RadioButton>
                </RadioGroup>
              )}
            </FormItem>
          </Col>
          <Col span={6} key='resultParam'>
            <FormItem labelCol={{span: 8}} wrapperCol={{span: 16}}
                      label={`保存结果参数`}>
              {getFieldDecorator(`resultParam`, {initialValue: null})(
                <Input
                  disabled={!this.props.form.getFieldValue('saveResult')}
                />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={24} style={{ textAlign: 'right', paddingRight: '50px', marginBottom: 10 }}>
            <Button type="primary" htmlType="submit">提交请求</Button>
            <Button style={{ marginLeft: 8 }} onClick={this.handleReset}>
              清除
            </Button>
          </Col>
        </Row>
      </Form>
    );
  }
}

export default Form.create()(InvokeForm);
