/*
 * Copyright 2015 Austin Keener, Michael Ritter, Florian Spieß, and the JDA contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.dv8tion.jda.internal.interactions.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.interactions.commands.context.MessageContextInteraction;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.JDAImpl;

public class MessageContextInteractionImpl extends ContextInteractionImpl<Message> implements MessageContextInteraction
{
    public MessageContextInteractionImpl(JDAImpl jda, DataObject data)
    {
        super(jda, data, resolved -> parse(jda, data, resolved));
    }

    @Override
    public MessageChannel getChannel()
    {
        return (MessageChannel) super.getChannel();
    }

    private static Message parse(JDAImpl api, DataObject interactionData, DataObject resolved)
    {
        DataObject messages = resolved.getObject("messages");
        DataObject message = messages.getObject(messages.keys().iterator().next());

        //Hopefully in the future we can ask 'message.hasKey("guild_id")' instead.
        Guild guild = null;
        if (!interactionData.isNull("guild_id"))
        {
            long guildId = interactionData.getUnsignedLong("guild_id");
            guild = api.getGuildById(guildId);
            if (guild == null)
                throw new IllegalStateException("Cannot find guild for resolved message object.");
        }
        return api.getEntityBuilder().createMessageWithLookup(message, guild, false);
    }
}
