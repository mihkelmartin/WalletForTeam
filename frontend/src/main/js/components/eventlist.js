'use strict';
const React = require('react');
import EventElement from './eventelement.js';

// tag::EventList[]
class EventList extends React.Component {

    render(){
        var events = this.props.events.map(
            event => <EventElement key={event.id} event={event}
                onEventSelected={this.props.onEventSelected}
                handleRESTError={this.props.handleRESTError}/>
        );
        return (
            <div>
                {events}
            </div>
        )
    }
}

export default EventList;
// end::EventList[]