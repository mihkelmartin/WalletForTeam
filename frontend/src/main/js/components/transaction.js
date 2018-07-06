const React = require('react');
import TransactionItem from './transactionitem.js';
var $ = require('jquery');
import ReactModal from 'react-modal';
import {getBackEndUrl, dialogStyles} from './getProperties';

ReactModal.setAppElement('#react');

class Transaction extends React.Component {

    state = {transaction : '',
             prevTransaction : '',
             bDeleteDialogOpen : false,
             saveButtonVisible : {display:"none"}
             };

    handleDeleteTransaction = () => {
        var url = getBackEndUrl() + 'Transactions/remove/' + this.props.eventId + '/' +
                    this.props.token + '/' + this.props.transaction.id;
        $.ajax({
            url: url,
            dataType: 'text',
            cache: false,
            success: function(data){
               this.closeModal();
               this.props.LoadMembers();
            }.bind(this),
            error: function(xhr, status, err) {
                this.props.handleRESTError(xhr);
            }.bind(this)
        });
    }

    handleUpdateTransaction = (newTransaction) => {
        var url = getBackEndUrl() + 'Transactions/update/' + this.props.eventId + '/' +
                    this.props.token;
        $.ajax({
            url: url,
            type: "POST",
            contentType: 'application/json;charset=UTF-8',
            data: JSON.stringify(newTransaction),
            cache: false,
            success: function(data) {
               this.props.LoadMembers()
            }.bind(this),
            error: function(xhr, status, err) {
                this.props.handleRESTError(xhr);
            }.bind(this)
        });
    }

    onInputChange = (e) => {
        this.newTransaction = Object.assign({}, this.state.transaction);
        this.newTransaction[e.target.name] = e.target.value;
        this.setState({member: this.newMember, saveButtonVisible : {display:"block"}});
   	    this.setState({transaction: this.newTransaction});
    }

    saveToDB = (e) => {
        if(this.newTransaction)
           if(this.newTransaction[e.target.name] !== this.state.prevTransaction[e.target.name])
                this.handleUpdateTransaction(this.newTransaction);
        this.setState({saveButtonVisible : {display:"none"}});
    }

    static getDerivedStateFromProps(nextProps, prevState) {
        if(prevState.prevTransaction.name === nextProps.transaction.name &&
           prevState.prevTransaction.order === nextProps.transaction.order &&
            prevState.prevTransaction.bmanualCalculation === nextProps.transaction.bmanualCalculation)
            return null;

        return {transaction: nextProps.transaction,
                prevTransaction: nextProps.transaction}
    }

    onTransactionDelete = (e) => {
       this.setState({bDeleteDialogOpen : true});
    }

    closeModal = (e) => {
       this.setState({bDeleteDialogOpen : false});
    }

	render() {
	    return (
			<div>
                <div className="ui left action input">
                    <button className="ui icon button">
                        <i className="trash icon" onClick={this.onTransactionDelete}></i>
                    </button>
                    <input type="text" name = "name" maxLength="48" style={{borderColor:'#2185D0'}}
                        value={this.state.transaction.name} onChange = {this.onInputChange}
                        onBlur={this.saveToDB}/>
                    <button className="ui mini basic green icon button" style={this.state.saveButtonVisible}>
                        <i className="ui save icon"></i>
                    </button>
                </div>
                <ReactModal
                    isOpen={this.state.bDeleteDialogOpen}
                    onRequestClose={this.closeModal}
                    style={dialogStyles}
                    contentLabel='Delete Transaction?'>
                    <p>This will remove {this.state.transaction.name} from Event!</p>
                    <p>Remove {this.state.transaction.name}?</p>
                    <div className="ui two buttons">
                      <div className="ui basic blue button" onClick={this.handleDeleteTransaction}>Yes</div>
                      <div className="ui basic blue button" onClick={this.closeModal}>No</div>
                    </div>
                </ReactModal>
			</div>
	    )
	}
}
export default Transaction;
