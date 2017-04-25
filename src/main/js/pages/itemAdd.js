import React, { Component } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import { find, remove } from 'lodash';

class ItemAddPage extends Component {

    constructor(props) {
        super(props);
        this.state = {
            name: '',
            description: ''
        };

        this.nameChange = this.nameChange.bind(this);
        this.descriptionChange = this.descriptionChange.bind(this);
        this.addItem = this.addItem.bind(this);
    }

    descriptionChange(event) {
        let state = this.state;
        state.description = event.target.value;

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
            })
            .catch(function (error) {
                if (error && error.response && error.response.data && error.response.data.message) {
                    alert(error.response.data.message);
                }
            });
    }

    addItem() {
        let {name, description} = this.state;

        axios.post('http://localhost:8080/api/todoLists', {name, description})
            .then(res => {
                alert("Successfully added");
            })
            .catch(function (error) {
                if (error && error.response && error.response.data && error.response.data.message) {
                    alert(error.response.data.message);
                }
            });
    }

    render() {
        let { description, name } = this.state;
        return (
            <div className="container">
                <div className="row form-group">
                    <div className="col-md-3">Name</div>
                    <div className="col-md-5"><input className="form-control" onChange={this.nameChange} value={name}
                                                     type="text"/></div>
                </div>
                <div className="row form-group">
                    <div className="col-md-3">Description</div>
                    <div className="col-md-5"><input className="form-control" onChange={this.descriptionChange} value={description} type="text"/>
                    </div>
                </div>

                <div className="row form-group">
                    <div className="col-md-3">&nbsp;</div>
                    <div className="col-md-5">
                        <button className="btn btn-primary" onClick={this.addItem}>Add</button>
                        <Link className="btn btn-primary" to={'/'}>Back</Link>
                    </div>
                </div>
            </div>
        );
    }
}

export default ItemAddPage;
