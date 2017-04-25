import React, { Component } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import { find, remove } from 'lodash';

class ItemPage extends Component {

    constructor(props) {
        super(props);
        this.state = {
            items: []
        };

        this.deleteTodo = this.deleteTodo.bind(this);
    }


    deleteTodo(id) {
        let items = this.state.items;
        let item = find(items, (it) => it.id == id);

        axios.delete(item._links.self.href)
            .then(res => {
                remove(items, (i) => i.id === id);
                this.setState({items});
            })
            .catch(function (error) {
                if (error && error.response && error.response.data && error.response.data.message) {
                    alert(error.response.data.message);
                }
            });
    }

    loadData() {
        axios.get('http://localhost:8080/api/todoLists')
            .then(res => {
                this.setState({items: res.data._embedded.todoLists});
            })
            .catch(function (error) {
                if (error && error.response && error.response.data && error.response.data.message) {
                    alert(error.response.data.message);
                }
            });
    }

    componentDidMount() {
        this.loadData();
    }

    render() {
        let {items } = this.state;
        let itemList = items.map((i, j) => <li className="list-group-item" key={j}><Link
            to={ i.id + '/todos'}>{ i.name + ' - ' + i.description }</Link>
            <a href="javascript:;" className="pull-right glyphicon glyphicon-trash"
               onClick={()=> {this.deleteTodo(i.id)}}></a></li>)
        return (
            <div>
                <div className="row form-group">
                    <div className="col-md-12">
                        <Link className="pull-right btn btn-primary" to={'/item/add'}>Add Todo List</Link>
                    </div>
                </div>
                <div className="list-group">
                    {itemList}
                </div>
            </div>
        );
    }
}

export default ItemPage;
