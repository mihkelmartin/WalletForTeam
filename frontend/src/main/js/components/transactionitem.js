'use strict';
const React = require('react');
var $ = require('jquery');
import {getBackEndUrl} from './getProperties';

var genCredit;

class TransactionItem extends React.Component {

    state = {debit : parseFloat(0.0).toFixed(2),
             credit : parseFloat(0.0).toFixed(2),
             bcreditAutoCalculated : true,
             prevDebitFromProps : 0.0,
             prevCreditFromProps : 0.0,
             prevbcreditAutoCalculated : true
            };

    onAddCredit = (e) => {
        e.preventDefault();
        // /Transactions/addCredit/{eventid}/{token}/{transactionid}/{memberid}/{credit}
        var url = getBackEndUrl() + 'Transactions/addCredit/' + this.props.eventId + '/' +
                    this.props.token + '/' + this.props.transactionId + '/' + this.props.memberId +
                    '/' + this.state.credit;
        $.ajax({
            url: url,
            dataType: 'text',
            cache: false,
            success: function(data){
               this.props.LoadMembers();
            }.bind(this),
            error: function(xhr, status, err) {
                this.props.handleRESTError(xhr);
            }.bind(this)
        });
    }

    onAddDebit = (e) => {
        e.preventDefault();
        // /Transactions/addDebit/{eventid}/{token}/{transactionid}/{memberid}/{debit}
        var url = getBackEndUrl() + 'Transactions/addDebit/' + this.props.eventId + '/' +
                    this.props.token + '/' + this.props.transactionId + '/' + this.props.memberId +
                    '/' + this.state.debit;
        $.ajax({
            url: url,
            dataType: 'text',
            cache: false,
            success: function(data){
               this.props.LoadMembers();
            }.bind(this),
            error: function(xhr, status, err) {
                this.props.handleRESTError(xhr);
            }.bind(this)
        });
    }
    onChangeAutoCalc = (bcreditAutoCalculatedCopy) => {
        // /Transactions/setAutoCalc/{eventid}/{token}/{transactionid}/{memberid}/{bAutoCalc}
        var url = getBackEndUrl() + 'Transactions/setAutoCalc/' + this.props.eventId + '/' +
                    this.props.token + '/' + this.props.transactionId + '/' + this.props.memberId +
                    '/' + bcreditAutoCalculatedCopy;
        $.ajax({
            url: url,
            dataType: 'text',
            cache: false,
            success: function(data){
               this.props.LoadMembers();
            }.bind(this),
            error: function(xhr, status, err) {
                this.props.handleRESTError(xhr);
            }.bind(this)
        });
    }
    onDebitChange = (e) => {
        this.setState({debit : e.target.value});
    }
    onCreditChange = (e) => {
        this.setState({credit : e.target.value});
    }
    onbcreditAutoCalculatedChange = (e) => {
        var bcreditAutoCalculatedCopy = this.state.bcreditAutoCalculated;
        this.setState({bcreditAutoCalculated : !bcreditAutoCalculatedCopy});
        this.onChangeAutoCalc(!bcreditAutoCalculatedCopy);
    }

    static getDerivedStateFromProps(nextProps, prevState) {
        if(prevState.prevDebitFromProps === nextProps.debit &&
           prevState.prevCreditFromProps === nextProps.credit &&
           prevState.prevbcreditAutoCalculated === nextProps.bcreditAutoCalculated){
            return null;
        }

        return {debit: parseFloat(nextProps.debit).toFixed(2),
                credit: parseFloat(nextProps.credit).toFixed(2),
                bcreditAutoCalculated: nextProps.bcreditAutoCalculated,
                prevDebitFromProps : nextProps.debit,
                prevCreditFromProps : nextProps.credit,
                prevbcreditAutoCalculated : nextProps.bcreditAutoCalculated
               }
    }

	render() {
        return (
            <div className = "two wide grey column center aligned">
                <form onSubmit={this.onAddDebit}>
                    <div className="ui input mini">
                       <input type="number" step="0.01" min="0" max="9999" pattern="\d+(\.\d{2})?"
                                style={{borderColor:'#2185D0',borderWidth:'1.5px', textAlign:'right',
                                fontWeight:this.state.debit == 0.0 ? 'normal':'bold'}}
                                value = {this.state.debit} onChange={this.onDebitChange}/>
                    </div>
                </form>
                <form onSubmit={this.onAddCredit}>
                    <div className="ui input mini">
                       <input type="number" step="0.01" min="0" max="9999" pattern="\d+(\.\d{2})?"
                       style={{borderColor:'#DB2828',borderWidth:'1.5px', textAlign:'right',
                        fontWeight:this.state.credit == 0.0 ? 'normal':'bold'}}
                       value = {this.state.credit} onChange={this.onCreditChange}/>
                    </div>
                </form>

                    <button disabled={this.state.bcreditAutoCalculated} onClick={this.onbcreditAutoCalculatedChange}>
                        <i className={!this.state.bcreditAutoCalculated ?
                                       "calculator blue icon disabled" : "calculator blue icon"}></i>
                    </button>
                    <span>
                    <button disabled={!this.state.bcreditAutoCalculated} onClick={this.onbcreditAutoCalculatedChange}>
                        <i className={this.state.bcreditAutoCalculated ?
                                        "edit green icon disabled" : "edit green icon"}></i>
                    </button>
                    </span>

            </div>
	    )
	}
}
export default TransactionItem;
