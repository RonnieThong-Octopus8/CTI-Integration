package com.novomind.ecom.app.iagent.custom.crm.training.agent.plugin;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.novomind.ecom.api.iagent.exception.PersistencyException;
import com.novomind.ecom.api.iagent.exception.WrongTypeException;
import com.novomind.ecom.api.imail.agent.MailAgentPlugin;
import com.novomind.ecom.api.imail.agent.action.AgentTicketCloseAction;
import com.novomind.ecom.api.imail.agent.validator.AgentTicketActionValidator;
import com.novomind.ecom.api.imail.exception.AgentTicketActionValidationException;
import com.novomind.ecom.app.iagent.custom.crm.training.shared.CrmTrainingConstants;

@MailAgentPlugin
public class CrmTrainingAgentTicketActionValidator implements AgentTicketActionValidator {

  @Inject
  private Logger log;

  @Override
  public void validate(AgentTicketCloseAction agentTicketCloseAction) throws AgentTicketActionValidationException {
    if (agentTicketCloseAction == null) {
      log.warn("The contact id could not be checked. Reason: agentTicketCloseAction = null");
      return;
    }
    String logTicketId = String.valueOf(agentTicketCloseAction.getTicketId());

    try {
      if (agentTicketCloseAction.getTicket().getStorage() != null) {
        String contactId = agentTicketCloseAction.getTicket().getStorage().getString(CrmTrainingConstants.ISSUE_PROPERTY_CONTACT_ID);
        if (contactId == null || contactId.isEmpty()) {
          throw new AgentTicketActionValidationException("Contact id is required.");
        }
        log.info("[{}] Contact id = {} checked as ticket property.", logTicketId, contactId);
      } else {
        log.warn("The contact id could not be checked. Reason: TicketStorage = null");
      }
    } catch (PersistencyException | WrongTypeException e) {
      log.error("[{}] Ticket property could not be checked.", logTicketId, e);
    }
  }

}
