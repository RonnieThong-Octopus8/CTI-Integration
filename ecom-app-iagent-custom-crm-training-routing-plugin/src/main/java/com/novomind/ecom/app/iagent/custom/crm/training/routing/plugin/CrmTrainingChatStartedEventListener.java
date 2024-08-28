package com.novomind.ecom.app.iagent.custom.crm.training.routing.plugin;

import com.novomind.ecom.api.iagent.exception.PersistencyException;
import com.novomind.ecom.api.iagent.exception.WrongTypeException;
import com.novomind.ecom.api.iagent.model.Call;
import com.novomind.ecom.api.iagent.routing.event.call.CallDeliveredEvent;
import com.novomind.ecom.api.iagent.routing.workflow.CallStateEventListener;
import com.novomind.ecom.api.imail.routing.RoutingPlugin;
import org.slf4j.Logger;

import javax.inject.Inject;

@RoutingPlugin
public class CrmTrainingChatStartedEventListener implements CallStateEventListener {

  @Inject
  private Logger log;

  @Override
  public void callStateChanged(CallDeliveredEvent callDeliveredEvent) {
    if (callDeliveredEvent == null) {
      log.warn("The source ID could not be stored. Reason: callDeliveredEvent = null");
      return;
    }

    Call call = callDeliveredEvent.getCall().orElse(null);
    if (call == null) {
      log.warn("The source ID could not be stored. Reason: call = null");
      return;
    }

    String sourceId = String.valueOf(call.getSourceId());
    String callId = String.valueOf(call.getCallId());
    try {
      if (call.getStorage() != null) {
        // Store the source ID and call ID in the call's storage
        call.getStorage().setString("sourceId", sourceId);
        call.getStorage().setString("callId", callId);
        call.getStorage().store();
        log.info("[{}] Source ID and Call ID stored as call properties.", sourceId);
      } else {
        log.warn("[{}] The source ID and Call ID could not be stored. Reason: CallStorage = null", sourceId);
      }
    } catch (PersistencyException | WrongTypeException e) {
      log.error("[{}] Error occurred while storing source ID and Call ID!", sourceId, e);
    }
  }
}
