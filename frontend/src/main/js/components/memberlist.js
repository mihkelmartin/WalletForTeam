const React = require('react');
var $ = require('jquery');
import ReactModal from 'react-modal';
import Member from './member.js';
import Payment from './payment.js';
import {getBackEndUrl, dialogStyles} from './getProperties';

ReactModal.setAppElement('#react');

class MemberList extends React.Component {

    state = {payments : [],
             bPaymentsDialogOpen : false,
             bPaymentsReceived : false};

    handleNewMember = () => {
        var url = getBackEndUrl() + 'Members/add/' + this.props.eventId + '/' + this.props.token;
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
     	var members = this.props.members.map( (member) => <Member key={member.id} eventId={this.props.eventId}
     	                                                        token = {this.props.token}
     	                                                        members={this.props.members}
     	                                                        member={member}
                                                                LoadMembers={this.props.LoadMembers}
                                                                handleRESTError = {this.props.handleRESTError}/> );
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
                            <button className='ui basic green button icon' onClick={this.handleNewMember}>
                                Add member  <i className='plus icon' />
                            </button>
                            </div>
                        </div>
                        <div className = "four wide column">
                        </div>
                    </div>
                    <div className="row">
                        <div className = "three wide blue column">
                            <div className = "ui center aligned inverted blue raised segment">
                                <h4 className = "ui header">
                                    Name
                                </h4>
                            </div>
                        </div>
                        <div className = "two wide blue column">
                            <div className = "ui center aligned inverted blue raised segment">
                                <h4 className = "ui header">
                                    Nickname
                                </h4>
                            </div>
                        </div>
                        <div className = "three wide blue column">
                            <div className = "ui center aligned inverted blue raised segment">
                                <h4 className = "ui header">
                                    e-mail
                                </h4>
                            </div>
                        </div>
                        <div className = "three wide blue column">
                            <div className = "ui center aligned inverted blue raised segment">
                                <h4 className = "ui header">
                                    IBAN
                                </h4>
                            </div>
                        </div>
                        <div className = "two wide blue column">
                             <div className = "ui center aligned inverted blue raised segment">
                                 <h4 className = "ui header">
                                     Payor
                                 </h4>
                             </div>
                        </div>
                        <div className = "two wide blue column">
                             <div className = "ui center aligned inverted blue raised segment">
                                 <h4 className = "ui header">
                                     Saldo
                                 </h4>
                             </div>
                        </div>
                    </div>
                    {members}
                </div>
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
export default MemberList;