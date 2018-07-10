'use strict';
const React = require('react');
var $ = require('jquery');

import SearchBar from './searchbar.js';
import {EventListConditionalRender} from './eventlistconditionalrenderer.js';
import {getBackEndUrl} from './getProperties';

// tag::MainDashBoard[]
class MainDashBoard extends React.Component {

    state = {events: [], email:'', newFormVisible : false};
    handleEmailChange = (email) => {
        this.setState({
          email: email
        }, () => {
           if(this.state.email && this.state.email.trim().length !== 0){
               var url = getBackEndUrl() + 'Event/find/email/' + this.state.email.trim();
                $.ajax({
                    url: url,
                    dataType: 'json',
                    cache: false,
                    success: function(data) {
                        data.sort(function(a,b){
                            return new Date(b.eventCreatedTS) - new Date(a.eventCreatedTS);
                        });
                        this.setState({events: data});
                    }.bind(this),
                    error: function(xhr, status, err) {
                        this.props.handleRESTError(xhr);
                    }.bind(this)
                });
            }
        });
    }

    setNewFormVisibility = (isVisible) => {
        this.setState({newFormVisible: isVisible});
        if(isVisible)
            this.setState({events: []});
    }

	render() {
        return (
            <div>
                <div className="ui divider"></div>
                <SearchBar currentEmail = {this.state.email}
                    onEmailChange = {this.handleEmailChange} setNewFormVisibility = {this.setNewFormVisibility}/>
                <div className="ui divider"></div>
                <EventListConditionalRender this={this}/>
            </div>
		)
	}
}

export default MainDashBoard;
// end::MainDashBoard[]