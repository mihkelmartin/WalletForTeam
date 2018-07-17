'use strict';
const React = require('react');
var $ = require('jquery');

import Event from './event.js';
import MemberList from './memberlist.js';
import TransactionList from './transactionlist.js';

import {getBackEndUrl} from './getProperties';

// tag::EventDashBoard[]
class EventDashBoard extends React.Component {

	state = {members: [], transactions : [], bMenuTransactions : true };

    LoadMembers = () => {
        var url = getBackEndUrl() + 'Members/' + this.props.eventId + '/' + this.props.token;
        $.ajax({
            url: url,
            dataType: 'json',
            cache: false,
            success: function(data) {
                data.sort(function(a,b) {return (a.order > b.order) ? 1 : ((b.order> a.order) ? -1 : 0);} );
                this.setState({members : data});
                this.LoadTransactions(data);
            }.bind(this),
            error: function(xhr, status, err) {
                this.props.handleRESTError(xhr);
            }.bind(this)
        });
    }
    LoadTransactions = (members) => {
        var url = getBackEndUrl() + 'Transactions/' + this.props.eventId + '/' + this.props.token;
        $.ajax({
            url: url,
            dataType: 'json',
            cache: false,
            success: function(data) {
                data.sort(function(a,b) {return (a.order < b.order) ? 1 : ((b.order < a.order) ? -1 : 0);} );
                data.forEach(
                    function(element){
                        element.items.sort(
                            function(a,b){
                                var memberA = members.find(function(element){return element.id === a.memberId});
                                var memberB = members.find(function(element){return element.id === b.memberId});
                                return (memberA.order > memberB.order) ? 1 : ((memberB.order > memberA.order) ? -1 : 0);
                            }.bind(this)
                        );
                    }.bind(this)
                );
                this.setState({transactions : data});
            }.bind(this),
            error: function(xhr, status, err) {
                this.props.handleRESTError(xhr);
            }.bind(this)
        });
    }

    componentDidMount() {
        this.LoadMembers();
    }

    onTransactionItemClick = (e) => {
        if(!this.state.bMenuTransactions)
            this.setState({bMenuTransactions : true});
    }

    onMemberItemClick = (e) => {
        if(this.state.bMenuTransactions)
            this.setState({bMenuTransactions : false});
    }


 	render() {
        var menuContent = <div>
                          <TransactionList eventId = {this.props.eventId} token = {this.props.token}
                              members={this.state.members}
                              transactions={this.state.transactions}
                              LoadMembers={this.LoadMembers}
                              handleRESTError = {this.props.handleRESTError}/>
                          <div className="ui divider"></div>
                          </div>;
        var transactionItem = 'item basic active';
        var memberItem = 'item basic';
        if(!this.state.bMenuTransactions){
            menuContent = <div>
                          <MemberList eventId = {this.props.eventId} token = {this.props.token}
                              members={this.state.members}
                              LoadMembers={this.LoadMembers}
                              handleRESTError = {this.props.handleRESTError}/>
                          <div className="ui divider"></div>
                          </div>;
            transactionItem = 'item basic';
            memberItem = 'item basic active';
 	    }

		return (
		    <div className= "ui container">
		        <div className="ui divider"></div>
                <Event eventId = {this.props.eventId} token = {this.props.token}
		            onEventSelected = {this.props.onEventSelected}
		            handleRESTError = {this.props.handleRESTError}/>
                <div className="ui divider"></div>
                <div className="ui two item basic blue tabular menu">
                    <a className={transactionItem} onClick={this.onTransactionItemClick}>Transactions</a>
                    <a className={memberItem} onClick={this.onMemberItemClick}>Members/Balance</a>
                </div>
                {menuContent}
            </div>
		)
	}
}
export default EventDashBoard;
// end::EventDashBoard[]