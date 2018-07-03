'use strict';

// tag::vars[]
const React = require('react');

var $ = require('jquery');
import {getBackEndUrl} from './getProperties';

import ReCAPTCHA from 'react-google-recaptcha'

// tag::EventElement[]
class EventElement extends React.Component{

	constructor(props) {
        super(props);
		this.puk = -1;
	}

    state = {pin : '', loginError : false};

    onPINLogin = (token) => {
        if(this.pin){
            var url = getBackEndUrl() + 'login/' + this.props.event.id + '/' + this.pin + '/' + token;
            console.log(url);
            $.ajax({
                url: url,
                dataType: 'text',
                cache: false,
                success: function(data) {
                    if(data === ""){
                        this.setState({pin : '', loginError : true});
                        this.puk = -1;
                    }
                    else
                        this.props.onEventSelected(this.props.event.id, data);
              }.bind(this),
                error: function(xhr, status, err) {
                    this.props.handleRESTError(xhr);
                }.bind(this)
            });
        }
        this.captcha.reset();
    }

    onPUKLogin = (token) => {
        if(this.puk !== -1){
            var url = getBackEndUrl() + 'puk/' + this.props.event.id + '/' + this.puk + '/' + token;
            console.log(url);
            $.ajax({
                url: url,
                dataType: 'text',
                cache: false,
                success: function(data) {
                    this.puk = -1;
              }.bind(this),
                error: function(xhr, status, err) {
                    this.props.handleRESTError(xhr);
                }.bind(this)
            });
        }
        this.captcha.reset();
    }

    onSubmit = (e) => {
        e.preventDefault();
        this.loginType = e.target.name;
        this.captcha.execute();
    }

    onChange = (token) => {
        if(this.loginType === 'PIN')
            this.onPINLogin(token);
        if(this.loginType === 'PUK')
            this.onPUKLogin(token);
    }

    onPinChange = (e) => {
        this.puk = e.target.value;
        this.pin = e.target.value;
        this.setState({pin : e.target.value});
    }

    onPinBlur = (e) => {
        this.setState({pin : '', loginError : false});
    }

    onPinFocus = (e) => {
        this.puk = -1;
    }

	render() {
        var PINInput;
        if(this.state.loginError === false){
            PINInput =  <div className = "ui fluid input">
                            <input ref="EventPIN" type="number" min="0" max="999999999"
                                placeholder="Enter PIN and press ENTER"
                                value={this.state.pin} onChange={this.onPinChange} onBlur={this.onPinBlur}
                                    onFocus={this.onPinFocus}/>
                        </div>;
        } else {
            PINInput =  <div className = "ui fluid error input">
                            <input ref="EventPIN" type="number" min="0" max="999999999" placeholder="Incorrect PIN"
                                value={this.state.pin} onChange={this.onPinChange} onBlur={this.onPinBlur}
                                    onFocus={this.onPinFocus}/>
                        </div>;
        }

		return (
			<div className="row" >
                <div className = "three wide column">
                    <div className="ui card">
                        <h4 className="ui blue header">{this.props.event.name}</h4>
                        <a className="ui label mini left aligned" name="PUK" onClick={this.onSubmit}>Reset PIN with PUK</a>
                    </div>
                </div>
                <div className = "three wide column middle aligned">
                    <form name="PIN" onSubmit={this.onSubmit}>
                        {PINInput}
                    </form>
				</div>
                <ReCAPTCHA
                  ref={(el) => { this.captcha = el; }}
                  size="invisible"
                  sitekey="6LdhaFwUAAAAAIorazYNYgkIr_sps2S2hrdMDxRa"
                  onChange={this.onChange}
                />
			</div>
		)
	}
}
export default EventElement;
// end::EventElement[]
