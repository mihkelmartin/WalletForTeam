const React = require('react');

var $ = require('jquery');

import Transaction from './transaction.js';
import TransactionItem from './transactionitem.js';
import {getBackEndUrl} from './getProperties';
import ReactTable from "react-table";

class TransactionList extends React.Component {

    handleNewTransaction = () => {
        var url = getBackEndUrl() + 'Transactions/add/' + this.props.eventId + '/' + this.props.token;
        $.ajax({
            url: url,
            dataType: 'text',
            cache: false,
            success: function(data) {
               this.props.LoadMembers();
            }.bind(this),
            error: function(xhr, status, err) {
                this.props.handleRESTError(xhr);
            }.bind(this)
        });
    }

	render() {
            var membersInTransactions = [];
            if(this.props.transactions.length !== 0)
                membersInTransactions = this.props.transactions[0].items.map(transactionitem => transactionitem.memberId );

            var columns = [];
            columns.push({Header: "",
                        minWidth: 210,
                         accessor:"transaction",
                        Cell: props => (<Transaction  eventId = {this.props.eventId}
                                            token = {this.props.token} transaction={props.value}
                                            LoadMembers={this.props.LoadMembers}
                                            handleRESTError = {this.props.handleRESTError}/>)});

            this.props.members.map( member => {
                if(membersInTransactions.indexOf(member.id) !== -1 ){
                    columns.push({Header: <div className = "ui center aligned inverted blue raised segment">
                                            <h4 className = "ui header">{member.nickName}</h4>
                                          </div>,
                            accessor:member.id,
                            Cell: props => (
                                    <TransactionItem
                                             eventId = {this.props.eventId}
                                             token = {this.props.token}
                                             transactionId = {props.value.transactionId}
                                             memberId={member.id}
                                             bcreditAutoCalculated={props.value.bcreditAutoCalculated}
                                             debit = {props.value.debit}
                                             credit = {props.value.credit}
                                             LoadMembers={this.props.LoadMembers}
                                             handleRESTError = {this.props.handleRESTError}/>
                            )
                    });
                }

            });
            var data = [];
            this.props.transactions.map( transaction=>{
                    var arrElement = {};
                    arrElement['transaction'] = transaction;
                    transaction.items.map( transactionitem => {
                        arrElement[transactionitem.memberId] = transactionitem;
                    });
                    data.push(arrElement);
                }
            );

		return (
            <div className= "ui container">
                <div className='ui basic content center aligned segment'>
                    <button className='ui basic green button icon' onClick={this.handleNewTransaction}>
                        Add transaction  <i className='plus icon' />
                    </button>
                </div>
                <ReactTable
                    showPagination={true}
                    sortable={false}
                    defaultPageSize= {5}
                    data={data}
                    columns={columns}
                    getTrProps={(state, rowInfo, column) => {
                      return {
                        style: {
                        background: '#767676'
                          }
                      };
                    }}
                    getTheadTrProps={(state, rowInfo, column) => {
                      return {
                        style: {
                            background:    '#2185D0'
                        }
                      };
                    }}
                />
            </div>
		)
	}
}
export default TransactionList;