import React, { Component } from 'react';
import './App.css';
import Home from './Home';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import PessoaList from './PessoaList';
import PessoaEdit from "./PessoaEdit";

class App extends Component {
  render() {
    return (
        <Router>
          <Switch>
            <Route path='/' exact={true} component={Home}/>
            <Route path='/pessoa' exact={true} component={PessoaList}/>
            <Route path='/pessoa/:id' component={PessoaEdit}/>
          </Switch>
        </Router>
    )
  }
}

export default App;