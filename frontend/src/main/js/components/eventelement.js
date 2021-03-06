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


		return (
            <div>
                <div className="ui container" style={{padding: "1.0em"}}>
                        <Mobile>
                            <div className="ui centered blue card" style={{width:"600px"}}>
                                <h1 className="ui blue header center aligned" style={{paddingTop: "0.5em", fontSize:"34px"}}>{this.props.event.name}</h1>
                                <EventContainer this={this} uiInputSize = "ui fluid massive right action input"
                                    uiLabelSize = "ui fluid large label left aligned"/>
                            </div>
                        </Mobile>
                        <Default>
                            <div className="ui centered blue card">
                                <h3 className="ui blue header center aligned" style={{paddingTop: "0.5em"}}>{this.props.event.name}</h3>
                                <EventContainer this={this} uiInputSize = "ui fluid right action input"
                                    uiLabelSize = "ui fluid mini label left aligned"/>
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
                <form name="PIN" onSubmit={props.this.onSubmit}>
                    <PINInput this={props.this} uiInputSize = {props.uiInputSize} />
                </form>
                <a className={props.uiLabelSize} name="PUK"
                    onClick={props.this.onSubmit}>Reset PIN with PUK</a>
            </div>;
}


function PINInput(props){
        if(props.this.state.loginError === false){
            return  <div className = {props.uiInputSize}>
                        <input type="number" min="0" max="999999999"
                            placeholder="Enter PIN"
                            value={props.this.state.pin} onChange={props.this.onPinChange} onBlur={props.this.onPinBlur}
                            onFocus={props.this.onPinFocus}/>
                        <button className="ui basic blue icon button" type="submit">
                            <i className="folder open green icon"></i>
                        </button>
                    </div>;
        } else {
            return  <div className = {props.uiInputSize + " error"}>
                       <input type="number" min="0" max="999999999" placeholder="Incorrect PIN"
                            value={props.this.state.pin} onChange={props.this.onPinChange} onBlur={props.this.onPinBlur}
                                onFocus={props.this.onPinFocus}/>
                       <button className="ui basic blue icon button" type="submit">
                            <i className="folder open green icon"></i>
                       </button>
                    </div>;
        }
}
