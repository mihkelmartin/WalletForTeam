const React = require('react');
var $ = require('jquery');
import Member from './member.js';
import {getBackEndUrl} from './getProperties';

class MemberList extends React.Component {

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

	render() {
     	var members = this.props.members.map( (member) => <Member key={member.id} eventId={this.props.eventId}
     	                                                        token = {this.props.token}
     	                                                        members={this.props.members}
     	                                                        member={member}
                                                                LoadMembers={this.props.LoadMembers}
                                                                handleRESTError = {this.props.handleRESTError}/> );

		return (
            <div className="ui seven column centered grid">
                <div className="row">
                    <div className = "four wide column">
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
                <div className="row" style={{height: "3em"}}>
                    <div className = "three wide blue column">
                        <div className = "ui inverted centered white header">
                                Name
                        </div>
                    </div>
                    <div className = "two wide blue column">
                         <div className = "ui inverted centered white header">
                                 Payor
                         </div>
                    </div>
                    <div className = "three wide blue column">
                        <div className = "ui inverted centered white header">
                                e-mail
                        </div>
                    </div>
                    <div className = "four wide blue column">
                        <div className = "ui inverted centered white header">
                                Bankaccount
                        </div>
                    </div>
                    <div className = "two wide blue column">
                         <div className = "ui inverted centered white header">
                                 Saldo
                         </div>
                    </div>
                </div>
                {members}
                <div className="row">
                    <div className = "four wide column">
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
            </div>
		)
	}
}
export default MemberList;