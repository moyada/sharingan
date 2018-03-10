import React from 'react';
import { Router, Route} from 'react-router';
import IndexPage from './routes/IndexPage';


const Routes = ({ history }) => (
  <Router history={history}>
    <Route path="/" exact component={IndexPage} />
  </Router>
)

export default Routes;
