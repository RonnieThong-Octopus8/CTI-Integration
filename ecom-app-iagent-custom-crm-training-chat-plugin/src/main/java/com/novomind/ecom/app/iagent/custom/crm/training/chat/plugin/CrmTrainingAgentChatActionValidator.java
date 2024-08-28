package com.novomind.ecom.app.iagent.custom.crm.training.chat.plugin;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.novomind.ecom.api.iagent.action.chat.AgentChatActionValidator;
import com.novomind.ecom.api.iagent.action.chat.AgentChatCloseAction;
import com.novomind.ecom.api.iagent.exception.AgentChatActionValidationException;
import com.novomind.ecom.api.iagent.exception.PersistencyException;
import com.novomind.ecom.api.iagent.exception.WrongTypeException;
import com.novomind.ecom.api.iagent.plugin.ChatAgentPlugin;
import com.novomind.ecom.app.iagent.custom.crm.training.shared.CrmTrainingConstants;

@ChatAgentPlugin
public class CrmTrainingAgentChatActionValidator implements AgentChatActionValidator {

  @Inject
  private Logger log;

  @Override
  public void validate(AgentChatCloseAction agentChatCloseAction) throws AgentChatActionValidationException {
    if (agentChatCloseAction == null) {
      log.warn("The contact id could not be checked. Reason: agentChatCloseAction = null");
      return;
    }
    String logChatId = String.valueOf(agentChatCloseAction.getChat().getId());

    try {
      if (agentChatCloseAction.getChat().getStorage() != null) {
        String contactId = agentChatCloseAction.getChat().getStorage().getString(CrmTrainingConstants.ISSUE_PROPERTY_CONTACT_ID);
        if (contactId == null || contactId.isEmpty()) {

        }
        log.info("[{}] Contact id = {} checked as chat property.", logChatId, contactId);
      } else {
        log.warn("The contact id could not be checked. Reason: ChatStorage = null");
      }
    } catch (PersistencyException | WrongTypeException e) {
      log.error("[{}] Chat property could not be checked.", logChatId, e);
    }
  }

}
