import React, { PropTypes } from 'react';
import { Router, Route, Switch } from 'dva/router';
import IndexPage from './routes/IndexPage';

// function RouterConfig({ history }) {
//   return (
//     <Router history={history}>
//       <Switch>
//         <Route path="/" exact component={IndexPage} />
//       </Switch>
//     </Router>
//   );
// }


const Routes = ({ history }) =>
  <Router history={history}>
    <Route path="/" exact component={IndexPage} />
  </Router>

Routes.propTypes = {
  history: PropTypes.any,
};


export default Routes;
