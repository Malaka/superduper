import React, { Component } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import Datetime from 'react-datetime';
import { find, remove } from 'lodash';

class TodoAddPage extends Component {

    constructor(props) {
        super(props);
        this.state = {
            name: '',
            description: '',
            dateOfCompletion: '',
            tags: ''
        };

        this.nameChange = this.nameChange.bind(this);
        this.descriptionChange = this.descriptionChange.bind(this);
        this.tagChange = this.tagChange.bind(this);
        this.dateTimeChange = this.dateTimeChange.bind(this);
        this.addItem = this.addItem.bind(this);
    }

    descriptionChange(event) {
        let state = this.state;
        state.description = event.target.value;

        this.setState(state);
    }

    tagChange(event) {
        let state = this.state;
        state.tags = event.target.value;
        this.setState(state);
    }

    dateTimeChange(event) {
        let state = this.state;
        state.dateOfCompletion = event.toDate();
        this.setState(state);
    }

    nameChange(event) {
        let state = this.state;
        state.name = event.target.value;
        this.setState(state);
    }

    deleteTodo(id) {
        let items = this.state.items;
        let item = find(items, (it) => it.id == id);

        axios.delete(item._links.self.href)
            .then(res => {
                remove(items, (i) => i.id === id);
                this.setState({items});
            });
    }

    addItem() {
        let {name, description, tags, dateOfCompletion} = this.state;
        let {params} = this.props.match;
        if (params && params.itemId) {
            axios.post('http://localhost:8080/api/todoItems', {
                "name": name,
                "description": description,
                "status": "PENDING",
                "dateOfCompletion": dateOfCompletion,
                "tags": tags,
                "todoList": "http://localhost:8080/api/todoLists/" + params.itemId
            })
                .then(res => {
                    alert("Successfully edited");
                })
                .catch(function (error) {
                    if (error && error.response && error.response.data && error.response.data.message) {
                        alert(error.response.data.message);
                    }
                });
        }
    }

    render() {
        let { description, name, tags, dateOfCompletion } = this.state;
        let {params} = this.props.match;
        return (
            <div className="container">
                <div className="row form-group">
                    <div className="col-md-3">Name</div>
                    <div className="col-md-5"><input className="form-control" onChange={this.nameChange} value={name}
                                                     type="text"/></div>
                </div>
                <div className="row form-group">
                    <div className="col-md-3">Description</div>
                    <div className="col-md-5"><input className="form-control" onChange={this.descriptionChange}
                                                     value={description} type="text"/>
                    </div>
                </div>
                <div className="row form-group">
                    <div className="col-md-3">Tags</div>
                    <div className="col-md-5"><input className="form-control" onChange={this.tagChange} value={tags}
                                                     type="text"/>
                    </div>
                </div>
                <div className="row form-group">
                    <div className="col-md-3">Date Time</div>
                    <div className="col-md-5"><Datetime onChange={this.dateTimeChange} value={dateOfCompletion}
                                                        type="text"/>
                    </div>
                </div>

                <div className="row form-group">
                    <div className="col-md-3">&nbsp;</div>
                    <div className="col-md-5">
                        <button className="btn btn-primary" onClick={this.addItem}>Add</button>
                        <Link className="btn btn-primary" to={'/'+params.itemId+'/todos'}>Back</Link>
                    </div>
                </div>
            </div>
        );
    }
}

export default TodoAddPage;
