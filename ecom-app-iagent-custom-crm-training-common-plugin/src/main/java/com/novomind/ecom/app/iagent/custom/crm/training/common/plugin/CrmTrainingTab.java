package com.novomind.ecom.app.iagent.custom.crm.training.common.plugin;

import java.util.Objects;

import javax.inject.Inject;

import com.novomind.ecom.api.iagent.frontend.tab.InfoTabBehavior;
import com.novomind.ecom.api.iagent.frontend.tab.InfoTabNotification;
import com.novomind.ecom.api.iagent.model.App;
import org.slf4j.Logger;

import com.novomind.ecom.api.iagent.frontend.chatinfo.ChatInfoTab;
import com.novomind.ecom.api.iagent.frontend.chatinfo.ChatInfoTabProvider;
import com.novomind.ecom.api.iagent.frontend.chatinfo.ChatInfoViewContext;
import com.novomind.ecom.api.iagent.plugin.ChatAgentPlugin;
import com.novomind.ecom.api.imail.agent.MailAgentPlugin;
import com.novomind.ecom.api.imail.common.frontend.mailinfo.MailInfoTab;
import com.novomind.ecom.api.imail.common.frontend.mailinfo.MailInfoTabProvider;
import com.novomind.ecom.api.imail.common.frontend.mailinfo.MailInfoViewContext;
import com.novomind.ecom.api.imail.routing.RoutingPlugin;
import com.novomind.ecom.app.iagent.custom.crm.training.shared.CrmTrainingConstants;

@RoutingPlugin
@MailAgentPlugin
@ChatAgentPlugin
public class CrmTrainingTab implements MailInfoTabProvider, ChatInfoTabProvider {

    @Inject
    private Logger log;

    @Inject
    private App app;

    @Override
    public MailInfoTab getMailInfoTab(MailInfoViewContext context) {
        if (Objects.nonNull(context)) {
            String displayName = CrmTrainingConstants.ISSUE_INFO_TAB_DISPLAY_NAME;
            InfoTabBehavior.SelectionType selectionType = InfoTabBehavior.SelectionType.SELECTED;
            boolean fullscreen = false;
            if (Objects.nonNull(app)) {
                CrmTrainingApiBean crmTrainingApiBean = new CrmTrainingApiBean(app, log);
                displayName = crmTrainingApiBean.getTabHeading();
                fullscreen = crmTrainingApiBean.getTabFullscreen();
                if (fullscreen) {
                    selectionType = InfoTabBehavior.SelectionType.SELECTED_FULLSCREEN;
                }
            }

            return new
                    MailInfoTab(CrmTrainingConstants.ISSUE_INFO_TAB_NAME,
                    displayName,
                    context.getViewUrl(CrmTrainingConstants.ISSUE_INFO_TAB_VIEW_URL))
                    .setBehavior(new InfoTabBehavior(selectionType));
        } else {
            log.warn("MailInfoTab could not be displayed. Reason: context=null");
        }
        return null;
    }

    @Override
    public ChatInfoTab getChatInfoTab(ChatInfoViewContext context) {
        if (Objects.nonNull(context)) {
            String displayName = CrmTrainingConstants.ISSUE_INFO_TAB_DISPLAY_NAME;
            InfoTabBehavior.SelectionType selectionType = InfoTabBehavior.SelectionType.SELECTED;
            boolean fullscreen = false;
            if (Objects.nonNull(app)) {
                CrmTrainingApiBean crmTrainingApiBean = new CrmTrainingApiBean(app, log);
                displayName = crmTrainingApiBean.getTabHeading();
                fullscreen = crmTrainingApiBean.getTabFullscreen();
                if (fullscreen) {
                    selectionType = InfoTabBehavior.SelectionType.SELECTED_FULLSCREEN;
                }
            }

            return new ChatInfoTab(CrmTrainingConstants.ISSUE_INFO_TAB_NAME,
                    displayName, context.getViewUrl(CrmTrainingConstants.ISSUE_INFO_TAB_VIEW_URL))
                    .setBehavior(new InfoTabBehavior(selectionType));
        } else {
            log.warn("ChatInfoTab could not be displayed. Reason: context=null");
        }

        return null;
    }

}