import React, { Component } from 'react';
import axios from 'axios';
import {remove, each, find, map } from 'lodash';
import { Link } from 'react-router-dom';

class TodoPage extends Component {

    constructor(props) {
        super(props);
        this.state = {
            todos: []
        };

        this.deleteTodo = this.deleteTodo.bind(this);
    }

    deleteTodo(id) {
        let todos = this.state.todos;
        let todo = find(todos, (item) => item.id == id);

        let newStatus = todo.status === 'DELETED' ? 'PENDING' : 'DELETED';
        axios.patch(todo._links.self.href, {status: newStatus})
            .then(res => {
                let todos = this.state.todos;
                each(todos, (i) => {
                        if (i.id === id) {
                            i.status = newStatus;
                        }
                    }
                );
                this.setState({todos});
            })
            .catch(function (error) {
                if (error && error.response && error.response.data && error.response.data.message) {
                    alert(error.response.data.message);
                }
            });
    }

    loadTodos() {
        let {params} = this.props.match;
        axios.get('http://localhost:8080/api/todoLists/' + params.itemId + '/todoItems')
            .then(res => {
                this.setState({todos: res.data._embedded.todoItems});
            })
            .catch(function (error) {
                if (error && error.response && error.response.data && error.response.data.message) {
                    alert(error.response.data.message);
                }
            });
    }

    componentDidMount() {
        this.loadTodos();
    }


    completed(id, status) {

        let todos = this.state.todos;
        let todo = find(todos, {id: id});
        let newStatus = status === 'PENDING' ? 'COMPLETED' : 'PENDING';

        axios.patch(todo._links.self.href, {status: newStatus})
            .then(res => {
                let todos = this.state.todos;
                each(todos, (i) => {
                        if (i.id === id) {
                            i.status = newStatus;
                        }
                    }
                );
                this.setState({todos});
            })
            .catch(function (error) {
                if (error && error.response && error.response.data && error.response.data.message) {
                    alert(error.response.data.message);
                }
            });
    }

    render() {
        let {todos} = this.state;
        let {params} = this.props.match;
        let itemList = todos.map((i, j) =>
            <li className={"list-group-item " + (i.status !== 'COMPLETED' ? (i.status === 'DELETED' ? 'deleted': '') : 'line-through')}
                key={j}>
                <a href="javascript:;"
                   className={'pull-left glyphicon '+ (i.status === 'COMPLETED' ? 'glyphicon-check' : 'glyphicon-unchecked') }
                   onClick={()=> {this.completed(i.id, i.status)}}></a>

                {i.name + ' - ' + i.description}
                {i.tags !== null ?
                    <span>{map(i.tags.split(','), (itm) => <span className="label label-default">{itm}</span>)}</span>
                    : null
                }
                <a href="javascript:;" className="pull-right"
                   onClick={()=> {this.deleteTodo(i.id)}}>
                    <i className={"glyphicon "+ (i.status !== 'DELETED' ? 'glyphicon-trash' : 'glyphicon-repeat') }></i>
                    {(i.status !== 'DELETED' ? 'Delete' : 'Undo')}
                </a>

            </li>);
        return (
            <div>
                <div className="clearfix">
                    <Link className="pull-right btn btn-primary" to={'/'}>Back</Link>
                    <Link className="pull-right btn btn-primary" to={'/' + params.itemId + '/todo/add'}>Add todo</Link>
                </div>
                <ul className="list-group clearfix">
                    {itemList}
                </ul>
            </div>
        );
    }
}

export default TodoPage;
