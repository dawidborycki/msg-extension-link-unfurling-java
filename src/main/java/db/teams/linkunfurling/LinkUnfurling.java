// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package db.teams.linkunfurling;

import com.codepoetics.protonpack.collectors.CompletableFutures;
import com.microsoft.bot.builder.ActivityHandler;
import com.microsoft.bot.builder.MessageFactory;
import com.microsoft.bot.builder.TurnContext;
import com.microsoft.bot.builder.teams.TeamsActivityHandler;
import com.microsoft.bot.schema.CardImage;
import com.microsoft.bot.schema.ChannelAccount;
import com.microsoft.bot.schema.HeroCard;
import com.microsoft.bot.schema.ThumbnailCard;
import com.microsoft.bot.schema.teams.AppBasedLinkQuery;
import com.microsoft.bot.schema.teams.MessagingExtensionAttachment;
import com.microsoft.bot.schema.teams.MessagingExtensionParameter;
import com.microsoft.bot.schema.teams.MessagingExtensionQuery;
import com.microsoft.bot.schema.teams.MessagingExtensionResponse;
import com.microsoft.bot.schema.teams.MessagingExtensionResult;

import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This class implements the functionality of the Bot.
 *
 * <p>
 * This is where application specific logic for interacting with the users would be added. For this
 * sample, the {@link #onMessageActivity(TurnContext)} echos the text back to the user. The {@link
 * #onMembersAdded(List, TurnContext)} will send a greeting to new conversation participants.
 * </p>
 */
public class LinkUnfurling extends TeamsActivityHandler {

    private String GetQueryText(MessagingExtensionQuery query) {
        String queryText = "Empty query";

        if (query != null && query.getParameters() != null) {
            List<MessagingExtensionParameter> queryParams = query.getParameters();

            if (!queryParams.isEmpty()) {
                MessagingExtensionParameter firstParam = queryParams.get(0);

                if(firstParam.getName().equals("searchQuery")) {
                    queryText = (String) queryParams.get(0).getValue();
                }
            }
        }
        
        return queryText;
    }
    
@Override
protected CompletableFuture<MessagingExtensionResponse> onTeamsMessagingExtensionQuery(
        TurnContext turnContext,
        MessagingExtensionQuery query
) {
    // Get query text
    String queryText = GetQueryText(query);
    
    // Create a hero card
    HeroCard card = new HeroCard();
    card.setTitle("Echo");
    card.setSubtitle(queryText);
    card.setText("Link unfurling");

    // Create attachment
    MessagingExtensionAttachment attachment = new MessagingExtensionAttachment();
    attachment.setContent(card);
    attachment.setContentType(HeroCard.CONTENTTYPE);
    attachment.setPreview(card.toAttachment());

    // Prepare result
    MessagingExtensionResult result = new MessagingExtensionResult();
    result.setAttachmentLayout("list");
    result.setType("result");
    result.setAttachment(attachment);

    // Return the response
    return CompletableFuture.completedFuture(new MessagingExtensionResponse(result));       
}


@Override
    protected CompletableFuture<MessagingExtensionResponse> onTeamsAppBasedLinkQuery(
        TurnContext turnContext,
        AppBasedLinkQuery query
    ) {
        ThumbnailCard card = new ThumbnailCard();
        card.setTitle("CodeProject");
        card.setText(query.getUrl());        
        card.setImages(Collections.singletonList(
            new CardImage("https://codeproject.freetls.fastly.net/App_Themes/CodeProject/Img/logo250x135.gif")
        ));

        MessagingExtensionAttachment attachments = new MessagingExtensionAttachment();
        attachments.setContentType(HeroCard.CONTENTTYPE);
        attachments.setContent(card);

        MessagingExtensionResult result = new MessagingExtensionResult();
        result.setAttachmentLayout("list");
        result.setType("result");
        result.setAttachments(Collections.singletonList(attachments));

        return CompletableFuture.completedFuture(new MessagingExtensionResponse(result));
    }


}
