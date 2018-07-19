const React = require('react');
import createHistory from "history/createBrowserHistory"
const history = createHistory();
import {Mobile, Default} from './getProperties';

// tag::SearchBar
class SearchBar extends React.Component {

    onSubmit = (evt) => {
        this.props.onEmailChange((evt.target.elements.eventEmail.value));
        this.setNewFormVisibility();
        evt.preventDefault();
    }

    setNewFormVisibility = () => {
        this.props.setNewFormVisibility(false);
    }

	render() {
		return (
		<form onSubmit={this.onSubmit}>
		<div className="ui container">
            <div className="ui blue centered header">
                <Mobile>
                    <SearchBarInput this={this} uiInputSize={"ui right fluid massive action input"}/>
                </Mobile>
                <Default>
                    <SearchBarInput this={this} uiInputSize={"ui right huge action input"}/>
                </Default>
            </div>
        </div>
        </form>
        )
	}
}

export default SearchBar;

function SearchBarInput (props){
    return  <div className={props.uiInputSize}>
                <input name="eventEmail" type="text" autoFocus
                    placeholder="Event creator e-mail..." value = {props.this.props.email}
                    onFocus={props.this.setNewFormVisibility}/>
                <button className="ui basic blue icon button" type="submit">
                    <i className="search large green icon"></i>
                </button>
            </div>;
}