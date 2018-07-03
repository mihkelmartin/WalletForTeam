const React = require('react');

class Payment extends React.Component {


	render() {

		return (
                <div className="row">
                    <div className = "three wide grey column left aligned">
                        {this.props.payment.payor}
                    </div>
                    <div className = "one wide grey column center aligned">
                        =>
                    </div>
                    <div className = "three wide grey column left aligned">
                        {this.props.payment.receiver}
                    </div>
                    <div className = "three wide blue column left aligned">
                        {this.props.payment.bankAccount}
                    </div>
                    <div className = "one wide blue column right aligned">
                        {parseFloat(this.props.payment.amount).toFixed(2)}
                    </div>
                </div>
	    )
	}
}
export default Payment;