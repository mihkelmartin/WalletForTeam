// tag::vars[]
const React = require('react');
var $ = require('jquery');
var captcha;

import ReCAPTCHA from 'react-google-recaptcha'
import {getBackEndUrl} from './getProperties';



class AddEvent extends React.Component {

	constructor(props) {
        super(props);
	}

    state = {msgText : 'Fill in and press Enter', eventname : '', email : ''};

    setResult = (data) => {
        captcha.reset();
        if(data.id == null){
            this.setState({
                 msgText: 'Adding new event failed'
             });

        } else {
             this.props.setNewFormVisibility(false);
             this.props.handleEmailChange(data.ownerEmail);
        }
    }
    onChange = (token) => {
        this.setState({
          msgText: 'Verifying...'
        });

        var url = getBackEndUrl() + '/Event/add/' + token;
        var eventJson = '{"name" : "' + this.refs.eventNameField.value +
                          '", "ownerEmail" : "' + this.refs.eMailField.value + '"}';
        $.ajax({
            url: url,
            type: "POST",
            contentType: 'application/json;charset=UTF-8',
            data: eventJson,
            cache: false,
            success: this.setResult,
            error: function(xhr, status, err) {
                this.props.handleRESTError(xhr);
            }.bind(this)
        });

    }

    onSubmit = (evt) => {
        captcha.execute();
        evt.preventDefault();
    }


	render() {
		return (
		<form onSubmit={this.onSubmit}>
            <div className='ui centered blue card'>
                <div className='content'>
                    <div className='header'>

                        <div className = "ui fluid input">
                            <input ref="eventNameField" type="text" maxLength="32" autoFocus placeholder="Event name"/>
                        </div>
                        <br></br>

                        <div className = "ui fluid input">
                            <input ref="eMailField" type="email" maxLength="64" placeholder="e-mail"/>
                        </div>
                        <input type="submit" style={{display:"none"}}/>

                        <br></br>

                        <div className = "ui label">
                            <a>{this.state.msgText}</a>
                        </div>

                        <ReCAPTCHA
                          ref={(el) => { captcha = el; }}
                          size="invisible"
                          sitekey="6LdhaFwUAAAAAIorazYNYgkIr_sps2S2hrdMDxRa"
                          onChange={this.onChange}
                        />

                    </div>
                </div>
            </div>
        </form>
		)
	}
}

export default AddEvent;

