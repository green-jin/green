package com.ncip.core.process.actions;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import com.ncip.core.constants.NcipCoreConstants;
import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.task.RetryLaterException;

/**
 * A process action to send emails.
 */
public class NcipSendEmailAction extends AbstractProceduralAction {
  
  private static final Logger LOG = Logger.getLogger(NcipSendEmailAction.class);
  
  private EmailService emailService;
 
  protected EmailService getEmailService() {
    return emailService;
  }

  @Required
  public void setEmailService(final EmailService emailService) {
    this.emailService = emailService;
  }
  
  @Override
  public void executeAction(
      final de.hybris.platform.processengine.model.BusinessProcessModel businessProcessModel)
      throws RetryLaterException { 
     
    final String sendEmailId = businessProcessModel.getProcessDefinitionName();

    if (sendEmailId.equalsIgnoreCase(NcipCoreConstants.ORDER_APPROVAL_REJECTION_EMAIL_PROCESS)
        || sendEmailId.equalsIgnoreCase(NcipCoreConstants.ORDER_PENDING_APPROVAL_EMAIL_PROCESS)
        || sendEmailId.equalsIgnoreCase(NcipCoreConstants.SEND_DELIVERY_EMAIL_PROCESS)) {
      for (final EmailMessageModel email : businessProcessModel.getEmails()) {
        
        email.setEmailPageTemplateProcess(sendEmailId);
        getEmailService().send(email);
      }
    } else {
      for (final EmailMessageModel email : businessProcessModel.getEmails()) {
        getEmailService().send(email);
      }
    }
  }
}
