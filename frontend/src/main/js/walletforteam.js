'use strict';
const React = require('react');
const ReactDOM = require('react-dom');

import {Router, Switch, Route} from 'react-router-dom';

import MainDashBoard from './components/maindashboard.js';
import EventDashBoard from './components/eventdashboard.js';
import Error from './components/error.js';
import createHistory from "history/createBrowserHistory"
const history = createHistory();


class WalletForTeam extends React.Component {

    state = {selectedEvent : '', errorText : 'Oops, something went wrong ! Please relogin to an Event.'};

    handleRESTError = (xhr) => {
        console.log(xhr);
        if(xhr){
            if(xhr.responseJSON){
                if(xhr.responseJSON.status == 403){
                    this.setState({errorText : 'Session expired or invalid security token. Relogin to Event.'});
                } else {
                    this.setState({errorText : 'Network error ' + xhr.responseJSON.status + '. Try again later'});
                }
            } else {
            if(xhr.status){
                if(xhr.status == 403){
                    this.setState({errorText : 'Session expired or invalid security token. Relogin to Event.'});
                } else {
                    this.setState({errorText : 'Oops, something went wrong ! Please relogin to an Event.'});
                }
            } else
                this.setState({errorText : 'Wallet for Team service temporary unavailable. Try again later.'});
            }
        } else {
            this.setState({errorText : 'Oops, something went wrong ! Please relogin to an Event.'});
        }
        history.replace({ pathname: '/error'});
    }

    handleEventSelected = (eventSelected, data) => {
        this.setState({selectedEvent: eventSelected});
        if(eventSelected !== '')
            history.push({ pathname: '/EventDashBoard/'+ eventSelected + '/' + data});
        else
            history.replace({ pathname: '/'});
    }
	render() {
		return (
            <Router history = {history}>
                <Switch>
                  <Route path='/EventDashBoard/:eventId/:token' render={(props) => <EventDashBoard{...props}
                        eventId = {props.match.params.eventId} token={props.match.params.token}
                        onEventSelected = {this.handleEventSelected} handleRESTError = {this.handleRESTError}/>}/>
                  <Route exact path='/error' render={(props) => <Error{...props} errorText={this.state.errorText}/>}/>
                  <Route path='/*' render={(props) => <MainDashBoard{...props} onEventSelected={this.handleEventSelected}
                                                                    handleRESTError = {this.handleRESTError}/>}/>
                </Switch>
            </Router>
        )
	}
}

// tag::render[]
ReactDOM.render(
	<WalletForTeam/>,
 	document.getElementById('react')
)

// end::render[]