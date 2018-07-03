'use strict';

// tag::vars[]
const React = require('react');
const ReactDOM = require('react-dom');

import {Router, Switch, Route} from 'react-router-dom';

import MainDashBoard from './components/maindashboard.js';
import EventDashBoard from './components/eventdashboard.js';
import Error from './components/error.js';
import createHistory from "history/createBrowserHistory"
const history = createHistory();


class WalletForTeam extends React.Component {

    state = {selectedEvent : '', errorText : ''};

    handleRESTError = (xhr) => {
        console.log(xhr);
        if(xhr.responseJSON){
            if(xhr.responseJSON.status == 403){
                this.setState({errorText : 'Session expired or invalid security token. Relogin to Event.'});
            } else {
                this.setState({errorText : 'Network error ' + xhr.responseJSON.status + '. Try again later'});
            }
        } else {
            this.setState({errorText : 'Wallet for Team service temporary unavailable. Try again later.'});
        }
        history.replace({ pathname: '/Error'});
    }

    handleEventSelected = (eventSelected, data) => {
        this.setState({selectedEvent: eventSelected});
        if(eventSelected !== '')
            history.push({ pathname: '/Event/'+ eventSelected + '/' + data});
        else
            history.replace({ pathname: '/'});
    }
	render() {
		return (
            <Router history = {history}>
                <Switch>
                  <Route exact path='/' render={(props) => <MainDashBoard{...props}
                                            onEventSelected={this.handleEventSelected}
                                            handleRESTError = {this.handleRESTError}/>}/>
                  <Route path='/Event/:eventId/:token' render={(props) => <EventDashBoard{...props}
                        eventId = {props.match.params.eventId} token={props.match.params.token}
                        onEventSelected = {this.handleEventSelected} handleRESTError = {this.handleRESTError}/>}/>
                  <Route exact path='/Error' render={(props) => <Error{...props} errorText={this.state.errorText}/>}/>
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