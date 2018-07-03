// tag::vars[]
const React = require('react');

class Error extends React.Component {
	render() {
		return (
		<div>
		    <div className="ui divider"></div>
            <div className="ui header centered">
                <i className="ban red icon"></i>
               {this.props.errorText}
           </div>
        </div>
		)
	}
}
export default Error;

