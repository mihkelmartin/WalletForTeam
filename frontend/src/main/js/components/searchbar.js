const React = require('react');
import createHistory from "history/createBrowserHistory"
const history = createHistory();

// tag::SearchBar
class SearchBar extends React.Component {

    onSubmit = (evt) => {
        this.props.onEmailChange((this.refs.eventEmail.value));
        this.setNewFormVisibility();
        evt.preventDefault();
    }

    setNewFormVisibility = () => {
        this.props.setNewFormVisibility(false);
    }

	render() {
		return (
		<form onSubmit={this.onSubmit}>
            <div className="ui blue centered header">
                <div className="icon input">
                    <input ref="eventEmail" type="text" autoFocus
                        placeholder="Event creator e-mail..." value = {this.props.email}
                                            onFocus={this.setNewFormVisibility}/>
                    <i className="search icon" onClick={this.onSubmit}></i>
                </div>
            </div>
        </form>
        )
	}
}

export default SearchBar;
// end::SearchBar[]