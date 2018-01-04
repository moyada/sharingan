import React from 'react';
import { Tabs, Form, Row, Col, Input, InputNumber, Button, Radio, message, notification } from 'antd';
import InvokeSelect from '../components/InvokeSelect';
import InvokeCascader from '../components/InvokeCascader';
import request from "../utils/request";

const TabPane = Tabs.TabPane;
const FormItem = Form.Item;
const RadioButton = Radio.Button;
const RadioGroup = Radio.Group;

class InvokeForm extends React.Component {
  state = {
    expand: false,
    type: 'dubbo'
  };

  handleSearch = (e) => {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if(err) {
        return
      }
      let payload
      switch (this.state.type) {
        case 'dubbo':
          if(null == values.invokeId || values.invokeId === undefined || values.invokeId.length !== 3) {
            message.error("请选择调用请求")
            return
          }
          if(null == values.invokeExpression || values.invokeExpression === undefined) {
            message.error("请输入参数表达式")
            return
          }

          payload = {
            invokeId: values.invokeId[2].split(`-`)[0],
            invokeExpression: values.invokeExpression,
            poolSize: values.poolSize,
            qps: values.qps,
            loop: values.loop,
            saveResult: values.saveResult,
            resultParam: values.resultParam,
          }

          message.success("生成测试请求")

          request("faker/invokeDubbo", payload)
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
                console.log(resp.data.data)
              }
              else {
                message.error(resp.data.msg, 10)
              }
            })
          break

        case 'http':
          if(null == values.invokeUrl || values.invokeUrl === undefined) {
            message.error("请输入请求地址")
            return
          }

          payload = {
            invokeUrl: values.invokeUrl,
            poolSize: values.poolSize,
            qps: values.qps,
            loop: values.loop,
            saveResult: values.saveResult,
            resultParam: values.resultParam,
          }

          message.success("生成测试请求")

          request("faker/invokeHttp", payload)
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
                console.log(resp.data.data)
              }
              else {

              }
            })
          break
      }
    })
  }

  handleReset = () => {
    this.props.form.resetFields();
  }

  onSelectInvoke(value) {
    if(value !== undefined && null !== value && value.length === 3) {
      let values = value[2].split(`-`)
      if(value !== undefined && null !== value) {
        this.props.form.setFieldsValue({
          invokeExpression: values[1],
        })
      }
    }
  }

  render() {
    const { getFieldDecorator } = this.props.form;

    const formItemLayout = {
      labelCol: { span: 9 },
      wrapperCol: { span: 15 },
    };

    const formItemRowLayout = {
        labelCol: {span: 2},
        wrapperCol: {span: 22},
      }
    ;

    return (
      <Form
        className="ant-advanced-search-form"
        style={{backgroundColor : '#EEEEEE', paddingLeft: 20}}
        onSubmit={this.handleSearch}
      >
        <Row>
          <Tabs defaultActiveKey="dubbo" onChange={(key) => this.state.type = key }>
            <TabPane tab="Dubbo" key="dubbo">
              <Col span={24} key='invokeId'>
                <FormItem {...formItemRowLayout} style={{ marginRight: '100px', marginTop: '20px' }} label={`请求`}>
                  {getFieldDecorator(`invokeId`, {initFieldsValue: null})(
                    <InvokeCascader
                      onChange={this.onSelectInvoke.bind(this)}
                    />
                  )}
                </FormItem>
              </Col>
              <Col span={24} key='invokeExpression'>
                <FormItem {...formItemRowLayout} style={{ marginRight: '100px' }} label={`参数表达式`}>
                  {getFieldDecorator(`invokeExpression`, {initFieldsValue: null})(
                    <Input
                      placeholder='["${1.test}"]'
                    />
                  )}
                </FormItem>
              </Col>
            </TabPane>

            <TabPane tab="Http" key="http">
              <Col span={24} key='invokeUrl'>
                <FormItem {...formItemRowLayout} style={{ marginRight: '100px' }} label={`请求地址`}>
                  {getFieldDecorator(`invokeUrl`, {initFieldsValue: null})(
                    <Input
                      placeholder='http://test.dubbo.api?code=${1.test}'
                    />
                  )}
                </FormItem>
              </Col>
            </TabPane>
          </Tabs>

          <Col span={4} key='poolSize'>
            <FormItem {...formItemLayout} label={`并发数`}>
              {getFieldDecorator(`poolSize`, {initFieldsValue: null})(
                <InputNumber
                  min={0}
                  max={1000}
                  precision={0}
                />
              )}
            </FormItem>
          </Col>
          <Col span={4} key='qps'>
            <FormItem {...formItemLayout} label={`每秒请求数`}>
              {getFieldDecorator(`qps`, {initFieldsValue: null})(
                <InputNumber
                  min={0}
                  max={1000}
                  precision={0}
                />
              )}
            </FormItem>
          </Col>
          <Col span={4} key='loop'>
            <FormItem {...formItemLayout} label={`请求次数`}>
              {getFieldDecorator(`loop`, {initFieldsValue: null})(
                <InputNumber
                  min={0}
                  precision={0}
                />
              )}
            </FormItem>
          </Col>
          <Col span={4} key='saveResult'>
            <FormItem {...formItemLayout} label={`保存结果`}>
              {getFieldDecorator(`saveResult`, {initFieldsValue: false})(
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
              {getFieldDecorator(`resultParam`, {initFieldsValue: null})(
                <Input
                  disabled={!this.props.form.getFieldValue('saveResult')}
                />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={24} style={{ textAlign: 'right', paddingRight: '50px', marginBottom: 10 }}>
            <Button type="primary" htmlType="submit">Search</Button>
            <Button style={{ marginLeft: 8 }} onClick={this.handleReset}>
              Clear
            </Button>
          </Col>
        </Row>
      </Form>
    );
  }
}

export default Form.create()(InvokeForm);
