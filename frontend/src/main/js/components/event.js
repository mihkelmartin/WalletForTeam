const React = require('react');
var $ = require('jquery');
var eventObject;

import ReactModal from 'react-modal';
import {getBackEndUrl, dialogStyles} from './getProperties';

ReactModal.setAppElement('#react');

class Event extends React.Component {

	state = {eventName:'', bDeleteDialogOpen : false};

	componentDidMount(){
        var url = getBackEndUrl() + 'Event/load/' + this.props.eventId + '/' + this.props.token;
        $.ajax({
            url: url,
            dataType: 'json',
            cache: false,
            success: function(data) {
               this.setState({eventName : data.name});
               eventObject = data;
            }.bind(this),
            error: function(xhr, status, err) {
                this.props.handleRESTError(xhr);
            }.bind(this)
        });

	}

    handleEventNameChange = (event) => {
        this.setState({eventName: event.target.value});
        eventObject.name = event.target.value
        var url = getBackEndUrl() + 'Event/update/' + this.props.token;
        $.ajax({
            url: url,
            type: "POST",
            contentType: 'application/json;charset=UTF-8',
            data: JSON.stringify(eventObject),
            cache: false,
            success: function(data) {
            }.bind(this),
            error: function(xhr, status, err) {
                this.props.handleRESTError(xhr);
            }.bind(this)
        });
    }

    handleEventDelete = () => {
        var url = getBackEndUrl() + 'Event/remove/' + this.props.eventId + '/' + this.props.token;
        $.ajax({
            url: url,
            dataType: 'text',
            cache: false,
            success: function(data){
                this.props.onEventSelected('','');
            }.bind(this),
            error: function(xhr, status, err) {
                this.props.handleRESTError(xhr);
            }.bind(this)
        });
        this.closeModal();
    }

    onEventDelete = (e) => {
       this.setState({bDeleteDialogOpen : true});
    }

    onLogOut = (e) => {
        this.props.onEventSelected('','');
    }

    closeModal = (e) => {
       this.setState({bDeleteDialogOpen : false});
    }

	render() {
		return (
            <div>
                <div className="ui seven column centered grid">
                    <div className="row">
                        <div className = "four wide column"/>
                        <div className = "eight wide column">
                            <div className='ui centered blue card'>
                                <div className='content'>
                                    <div className='header'>
                                        <div className='ui fluid input'>
                                            <input type="text" maxLength="32" value={this.state.eventName}
                                                 onChange={this.handleEventNameChange}/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className = "four wide column right aligned">
                            <i className='ui trash large blue icon' onClick={this.onEventDelete} />
                            <span>
                                <i className='ui close large red icon' onClick={this.onLogOut} />
                            </span>
                        </div>
                    </div>
                </div>
                <ReactModal
                    isOpen={this.state.bDeleteDialogOpen}
                    onRequestClose={this.closeModal}
                    style={dialogStyles}
                    contentLabel='Delete Event?'>
                    <p>This will delete {this.state.eventName}! It cannot be restored.</p>
                    <p>Delete {this.state.eventName}?</p>
                    <div className="ui two buttons">
                      <div className="ui basic blue button" onClick={this.handleEventDelete}>Yes</div>
                      <div className="ui basic blue button" onClick={this.closeModal}>No</div>
                    </div>
                </ReactModal>
            </div>
		)
	}
}

export default Event;

