'use strict';
const React = require('react');
var $ = require('jquery');
import {getBackEndUrl, Mobile, Default} from './getProperties';
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
    }

    onPUKLogin = (token) => {
        if(this.puk !== -1){
            var url = getBackEndUrl() + 'puk/' + this.props.event.id + '/' + this.puk + '/' + token;
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
        this.captcha.reset();
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
            PINInput = <div className="ui fluid right action input">
                            <input ref="EventPIN" type="number" min="0" max="999999999"
                                placeholder="Enter PIN and press ENTER"
                                value={this.state.pin} onChange={this.onPinChange} onBlur={this.onPinBlur}
                                onFocus={this.onPinFocus}/>
                            <button className="ui basic blue icon button" type="submit">
                                <i className="folder open green icon"></i>
                            </button>
                        </div>;
        } else {
            PINInput =  <div className = "ui fluid right error action input">
                            <input ref="EventPIN" type="number" min="0" max="999999999" placeholder="Incorrect PIN"
                                value={this.state.pin} onChange={this.onPinChange} onBlur={this.onPinBlur}
                                    onFocus={this.onPinFocus}/>
                           <button className="ui basic blue icon button" type="submit">
                                <i className="folder open icon"></i>
                            </button>
                        </div>;
        }

		return (
            <div>
                <div className="ui container" style={{padding: "1.0em"}}>
                    <Mobile>
                        <div className="ui centered fluid blue card">
                        <EventContainer this={this} PINInput={PINInput}/>
                        </div>
                    </Mobile>
                    <Default>
                        <div className="ui centered blue card">
                        <EventContainer this={this} PINInput={PINInput}/>
                        </div>
                    </Default>
                </div>
                <ReCAPTCHA
                  ref={(el) => { this.captcha = el; }}
                  size="invisible"
                  sitekey="6LcyQmIUAAAAAHmSz5TvnQLXMGpjwjMVhEYif9la"
                  onChange={this.onChange}
                />
            </div>
		)
	}
}
export default EventElement;

function EventContainer (props){
    return  <div>
                <h4 className="ui blue header center aligned" style={{paddingTop: "0.5em"}}>{props.this.props.event.name}</h4>
                <form name="PIN" onSubmit={props.this.onSubmit}>
                    {props.PINInput}
                </form>
                <a className="ui label mini left aligned" name="PUK"
                    onClick={props.this.onSubmit}>Reset PIN with PUK</a>
            </div>;
}
