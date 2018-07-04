const React = require('react');
var $ = require('jquery');
import ReactModal from 'react-modal';
import {getBackEndUrl, dialogStyles} from './getProperties';

ReactModal.setAppElement('#react');

class Member extends React.Component {

    state = {member : '',
             prevMember : '',
             bDeleteDialogOpen : false};

    handleDeleteMember = () => {
        var url = getBackEndUrl() + 'Members/remove/' + this.props.eventId + '/' +
                    this.props.token + '/' + this.props.member.id;
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

    handleUpdateMember = (newMember) => {
        var url = getBackEndUrl() + 'Members/update/' + this.props.eventId + '/' + this.props.token;
        $.ajax({
            url: url,
            type: "POST",
            contentType: 'application/json;charset=UTF-8',
            data: JSON.stringify(newMember),
            cache: false,
            success: function(data) {
                this.props.LoadMembers();
            }.bind(this),
            error: function(xhr, status, err) {
                this.props.handleRESTError(xhr);
            }.bind(this)
        });
    }

    onInputChange = (e) => {
        const newMember = Object.assign({}, this.state.member);
        newMember[e.target.name] = e.target.value;
        this.setState({member: newMember});
        this.handleUpdateMember(newMember);
    }

    static getDerivedStateFromProps(nextProps, prevState) {
        if(prevState.prevMember.name === nextProps.member.name &&
           prevState.prevMember.nickName === nextProps.member.nickName &&
            prevState.prevMember.eMail === nextProps.member.eMail &&
            prevState.prevMember.bankAccount === nextProps.member.bankAccount &&
            prevState.prevMember.credit=== nextProps.member.credit &&
            prevState.prevMember.debit === nextProps.member.debit &&
            prevState.prevMember.payor === nextProps.member.payor)
            return null;

        return {member: nextProps.member,
                prevMember: nextProps.member}
    }

    onMemberDelete = (e) => {
       this.setState({bDeleteDialogOpen : true});
    }

    closeModal = (e) => {
       this.setState({bDeleteDialogOpen : false});
    }

	render() {
        var options = this.props.members.map( (member) => <option key={member.id} value={member.id}>
                                                            {member.name}
                                                          </option> );
		return (
            <div className="row">
				<div className = "three wide light grey column center aligned">
				    <div className="ui left action input">
                        <button className="ui icon button">
                            <i className="trash icon" onClick={this.onMemberDelete}></i>
                        </button>
                        <input type = "text" name="name" maxLength="24"
                               value={this.state.member.name} onChange = {this.onInputChange}/>
				    </div>
                </div>
				<div className = "two wide light grey column center aligned">
				    <div className = "ui fluid input ">
                        <input type = "text" name="nickName" maxLength="6" size="6"
                           value={this.state.member.nickName} onChange = {this.onInputChange}/>
                    </div>
                </div>
				<div className = "three wide light grey column center aligned">
                    <div className = "ui fluid input">
                        <input type = "email" name="eMail" maxLength="64"
                          value={this.state.member.eMail} onChange = {this.onInputChange}/>
                    </div>
                </div>
				<div className = "three wide light grey column center aligned">
				    <div className = "ui fluid input">
                        <input type = "text" name="bankAccount" maxLength="34"
                          value={this.state.member.bankAccount} onChange = {this.onInputChange}/>
                    </div>
                </div>
				<div className = "two wide light grey column center aligned">
				    <div className = "ui fluid input">
				        <select value={this.state.member.payor} name="payor" onChange = {this.onInputChange}>
				            {options}
				        </select>
                    </div>
                </div>
                <div className = "two wide light grey column center aligned">
                    <p>{parseFloat(this.state.member.debit-this.state.member.credit).toFixed(2)}</p>
                </div>
                <ReactModal
                    isOpen={this.state.bDeleteDialogOpen}
                    onRequestClose={this.closeModal}
                    style={dialogStyles}
                    contentLabel='Delete Member?'>
                    <p>This will remove {this.state.member.name} and all related transactionitems</p>
                    <p>from Event!</p>
                    <p>Remove {this.state.member.name}?</p>
                    <div className="ui two buttons">
                      <div className="ui basic blue button" onClick={this.handleDeleteMember}>Yes</div>
                      <div className="ui basic blue button" onClick={this.closeModal}>No</div>
                    </div>
                </ReactModal>
			</div>
	    )
	}
}
export default Member;