import React from 'react';
import PropTypes from 'prop-types';
import { Table, Form, Row, Col, Input, Button } from 'antd';
import InvokeForm from '../components/InvokeForm';
import request from '../utils/request';

const FormItem = Form.Item;

const columns = [{
  title: '请求参数',
  dataIndex: 'realParam',
  width: '15%',
}, {
  title: '结果码',
  dataIndex: 'code',
  width: '6%',
}, {
  title: '请求结果',
  dataIndex: 'result',
  width: '20%',
}, {
  title: '异常信息',
  dataIndex: 'message',
  width: '30%',
}, {
  title: '耗时',
  dataIndex: 'spendTime',
  width: '8%',
}, {
  title: '请求时间',
  dataIndex: 'invokeTime',
  width: '10%',
}];

class ResultList extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      currentPageIndex: 1,
      currentPageSize: 100,
      loading: false,
      data: [],
      pagination: {
        current: 1,
        pageSize: 100,
        defaultPageSize: 100,
        total: 0,
        onChange: (p, s) => {
          this.loadBrands(this.state.brandCode, this.state.brandName, p, data.pageSize);
        },
      },
    };
  }


  loadBrands(brandCode, brandName, pageIndex, pageSize) {
    const payload = {brand_code: brandCode, brand_name: brandName, page_index: pageIndex, page_size: pageSize };
    this.setState({loading: true})
    const item = this
    request('/brand/getBrandPage.jsonp', payload).then(data => {
      const brandList = [];
      data.items.forEach((item, index, array) => {
        brandList.push({
          key: item.brandCode,
          country: item.country,
          brandCode: item.brandCode,
          brandName: item.brandName,
          enName: item.enName,
          pinyin: item.pinyin,
          aliasName: item.aliasName,
          firstWord: item.firstWord,
          logoUrl: item.logoUrl,
        });
      });

      this.state.pagination.current = pageIndex
      this.state.pagination.total = data.totalNumber

      this.setState({
        brands: brandList,
        loading: false,
        brandCode: brandCode,
        brandName: brandName,
        currentPageIndex: pageIndex,
        currentPageSize: pageSize,
      });
    });
  }

  handleSearch = (e) => {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if(err) {
        return
      }


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
