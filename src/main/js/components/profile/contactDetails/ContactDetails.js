import React from 'react';
import ContactDetailsList from './ContactDetailsList';
import ContactDetailsForm from './ContactDetailsForm';

class ContactDetails extends React.Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div>
        <h2>Contact Details</h2>
        <ContactDetailsList {...this.props} />

        <br />

        <ContactDetailsForm {...this.props} />
      </div>
    )
  }
}

export default ContactDetails;
