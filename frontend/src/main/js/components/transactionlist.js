const React = require('react');
var $ = require('jquery');
import ReactModal from 'react-modal';
import ReactTable from "react-table";

import Transaction from './transaction.js';
import TransactionItem from './transactionitem.js';
import Payment from './payment.js';
import {getBackEndUrl, dialogStyles} from './getProperties';

ReactModal.setAppElement('#react');

class TransactionList extends React.Component {

    state = {payments : [],
             bPaymentsDialogOpen : false,
             bPaymentsReceived : false};

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

    handlePayments = () => {
        this.setState({payments: [], bPaymentsReceived : false});
        var url = getBackEndUrl() + 'Payments/' + this.props.eventId + '/' + this.props.token;
        $.ajax({
            url: url,
            dataType: 'json',
            cache: false,
            success: function(data) {
                this.setState({payments: data, bPaymentsReceived : true});
            }.bind(this),
            error: function(xhr, status, err) {
                this.props.handleRESTError(xhr);
            }.bind(this)
        });
        this.setState({bPaymentsDialogOpen : true});
    }

    closeModal = (e) => {
       this.setState({bPaymentsDialogOpen : false});
    }

	render() {
            var membersInTransactions = [];
            if(this.props.transactions.length !== 0)
                membersInTransactions = this.props.transactions[0].items.map(transactionitem => transactionitem.memberId );

            var columns = [];
            columns.push({Header: "",
                        minWidth: 250,
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
                            minWidth: 120,
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

     	var payments = this.state.payments.map( (payment) => <Payment key={payment.payor+payment.receiver}
     	                                                                payment = {payment}/> );

        var content;
        if(payments.length === 0){
            if(this.state.bPaymentsReceived === false)
                content =   <div>
                                <div className="ui divider"></div>
                                 <div className="ui loading segment">
                                  <div className="ui blue centered header">
                                       Calculating ...
                                  </div>
                                </div>
                            </div>;
            else
                content =   <div>
                             <div className="ui divider"></div>
                               <div className="ui blue centered header">
                                    No payments required !
                               </div>
                         </div>;
            } else {
            content =   <div>
                            <div className="ui blue centered header">
                            Payments
                            </div>
                            <div className="ui divider"></div>
                            <div className="ui five column centered grid">
                                    {payments}
                            </div>
                        </div>;
        }

		return (
            <div className= "ui container">
                <div className="ui seven column centered grid">
                    <div className="row">
                        <div className = "four wide column">
                            <div className='ui basic content left aligned segment'>
                                <button className='ui basic green button icon' onClick={this.handlePayments}>
                                    Payments  <i className='calculator icon' />
                                </button>
                            </div>
                        </div>
                        <div className = "eight wide column">
                            <div className='ui basic content center aligned segment'>
                                <button className='ui basic green button icon' onClick={this.handleNewTransaction}>
                                    Add transaction  <i className='plus icon' />
                                </button>
                            </div>
                        </div>
                        <div className = "four wide column">
                        </div>
                    </div>
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
                <ReactModal
                    isOpen={this.state.bPaymentsDialogOpen}
                    onRequestClose={this.closeModal}
                    style={dialogStyles}
                    contentLabel='Create payments'>
                    <div className= "ui container">
                            {content}
                       <div className="ui divider"></div>
                        <div className="ui two buttons">
                          <div className="ui basic blue button" onClick={this.closeModal}>Cancel</div>
                          <div className="ui basic blue button" onClick={this.closeModal}>Send e-mails</div>
                        </div>
                    </div>
                </ReactModal>
            </div>
		)
	}
}
export default TransactionList;