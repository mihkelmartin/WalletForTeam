const React = require('react');
var $ = require('jquery');
import ReactModal from 'react-modal';
import {getBackEndUrl, dialogStyles} from './getProperties';

ReactModal.setAppElement('#react');

class Member extends React.Component {



    state = {member : '',
             prevMember : '',
             bDeleteDialogOpen : false,
             saveButtonVisible : {nickName : {display:"none"}, eMail :{display:"none"}, bankAccount :{display:"none"}}
             };

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
        this.newMember = Object.assign({}, this.state.member);
        this.newMember[e.target.name] = e.target.value;
        let newsaveButtonVisible = Object.assign({}, this.state.saveButtonVisible);
        newsaveButtonVisible[e.target.name] = {display:"block"};
        this.setState({member: this.newMember, saveButtonVisible : newsaveButtonVisible});

        if(e.target.name === 'payor')
            this.saveToDB(e);
    }

    saveToDB = (e) => {
        if(this.newMember)
           if(this.newMember[e.target.name] !== this.state.prevMember[e.target.name])
                this.handleUpdateMember(this.newMember);

        let newsaveButtonVisible = Object.assign({}, this.state.saveButtonVisible);
        newsaveButtonVisible[e.target.name] = {display:"none"};
        this.setState({saveButtonVisible : newsaveButtonVisible});
    }

    static getDerivedStateFromProps(nextProps, prevState) {
        if(prevState.prevMember.nickName === nextProps.member.nickName &&
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
                                                            {member.nickName}
                                                          </option> );
		return (
            <div className="row">
				<div className = "three wide light column center aligned">
				    <div className="ui fluid left action input">
                        <button className="ui icon button">
                            <i className="trash icon" onClick={this.onMemberDelete}></i>
                        </button>
                        <input type = "text" name="nickName" maxLength="12" size="6" style={{borderColor:'#2185D0'}}
                           value={this.state.member.nickName} onChange = {this.onInputChange}
                           onBlur={this.saveToDB}/>
                        <button className="ui mini basic green icon button" style={this.state.saveButtonVisible.nickName}>
                            <i className="ui save icon"></i>
                        </button>
                    </div>
                </div>
				<div className = "two wide light column center aligned">
                    <select className="ui dropdown" value={this.state.member.payor} name="payor"
                        style={{borderColor:'#2185D0'}} onChange = {this.onInputChange}>
                        {options}
                    </select>
                </div>
				<div className = "three wide light column center aligned">
                    <div className = "ui fluid input">
                        <input type = "email" name="eMail" maxLength="64" style={{borderColor:'#2185D0'}}
                          value={this.state.member.eMail} onChange = {this.onInputChange}
                          onBlur={this.saveToDB}/>
                        <button className="ui mini basic green icon button" style={this.state.saveButtonVisible.eMail}>
                            <i className="ui save icon"></i>
                        </button>
                    </div>
                </div>
				<div className = "four wide light column center aligned">
				    <div className = "ui fluid input">
                        <input type = "text" name="bankAccount" maxLength="34" style={{borderColor:'#2185D0'}}
                          value={this.state.member.bankAccount} onChange = {this.onInputChange}
                          onBlur={this.saveToDB}/>
                        <button className="ui mini basic green icon button" style={this.state.saveButtonVisible.bankAccount}>
                            <i className="ui save icon"></i>
                        </button>
                    </div>
                </div>
                <div className = "two wide light column center aligned">
                    <p>{parseFloat(this.state.member.debit-this.state.member.credit).toFixed(2)}</p>
                </div>
                <ReactModal
                    isOpen={this.state.bDeleteDialogOpen}
                    onRequestClose={this.closeModal}
                    style={dialogStyles}
                    contentLabel='Delete Member?'>
                    <p>This will remove {this.state.member.nickName} and all related transactionitems</p>
                    <p>from Event!</p>
                    <p>Remove {this.state.member.nickName}?</p>
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