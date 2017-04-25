import React, { Component } from 'react';

import ReactDOM from 'react-dom';

import {
    BrowserRouter as Router,
    Route,
    Link
} from 'react-router-dom';

import TodoPage from './pages/todoPage';
import ItemPage from './pages/itemPage';
import ItemAddPage from './pages/itemAdd';
import TodoAddPage from './pages/todoAddPage';

class App extends Component {
    render() {
        return (
            <Router>
                <div className="container">
                    <Route exact path="/" component={ItemPage}/>
                    <Route exact path="/item/add" component={ItemAddPage}/>
                    <Route exact path="/item/edit/:itemId" component={ItemAddPage}/>
                    <Route path="/:itemId/todos" component={TodoPage}/>
                    <Route path="/:itemId/todo/add" component={TodoAddPage}/>
                </div>
            </Router>
        );
    }
}


ReactDOM.render(
    <App />,
    document.getElementById('react')
)
