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

package net.dv8tion.jda.api.requests.restaction.interactions;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ActionComponent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.AttachmentOption;
import net.dv8tion.jda.internal.utils.Checks;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * A {@link InteractionCallbackAction} which can be used to edit the message for an interaction.
 */
public interface MessageEditCallbackAction extends InteractionCallbackAction<InteractionHook>
{
    /**
     * Set the new content for this message.
     *
     * @param  content
     *         The new message content
     *
     * @throws IllegalArgumentException
     *         If the provided content is longer than {@link net.dv8tion.jda.api.entities.Message#MAX_CONTENT_LENGTH MAX_CONTENT_LENGTH} characters
     *
     * @return The same update action, for chaining convenience
     */
    @Nonnull
    MessageEditCallbackAction setContent(@Nullable final String content);

    /**
     * Set the {@link MessageEmbed MessageEmbeds} for the message
     *
     * @param  embeds
     *         The message embeds
     *
     * @throws IllegalArgumentException
     *         If null is provided, or one of the embeds is too big, or more than {@value Message#MAX_EMBED_COUNT} embeds are provided
     *
     * @return The same update action, for chaining convenience
     */
    @Nonnull
    @CheckReturnValue
    default MessageEditCallbackAction setEmbeds(@Nonnull MessageEmbed... embeds)
    {
        Checks.noneNull(embeds, "MessageEmbed");
        return setEmbeds(Arrays.asList(embeds));
    }

    /**
     * Set the {@link MessageEmbed MessageEmbeds} for the message
     *
     * @param  embeds
     *         The message embeds
     *
     * @throws IllegalArgumentException
     *         If null is provided, one of the embeds is too big, or more than {@value Message#MAX_EMBED_COUNT} embeds are provided
     *
     * @return The same update action, for chaining convenience
     */
    @Nonnull
    @CheckReturnValue
    MessageEditCallbackAction setEmbeds(@Nonnull Collection<? extends MessageEmbed> embeds);

    /**
     * Set the action rows for the message.
     *
     * @param  rows
     *         The new action rows
     *
     * @throws IllegalArgumentException
     *         If null is provided, more than 5 action rows are provided,
     *         or any custom {@link ActionComponent#getId() id} is duplicated
     *
     * @return The same update action, for chaining convenience
     */
    @Nonnull
    @CheckReturnValue
    default MessageEditCallbackAction setActionRows(@Nonnull Collection<? extends ActionRow> rows)
    {
        Checks.noneNull(rows, "ActionRows");
        return setActionRows(rows.toArray(new ActionRow[0]));
    }

    /**
     * Set the action rows for the message.
     *
     * @param  rows
     *         The new action rows
     *
     * @throws IllegalArgumentException
     *         If null is provided, more than 5 action rows are provided,
     *         or any custom {@link ActionComponent#getId() id} is duplicated
     *
     * @return The same update action, for chaining convenience
     */
    @Nonnull
    @CheckReturnValue
    MessageEditCallbackAction setActionRows(@Nonnull ActionRow... rows);

    /**
     * Set only one action row for convenience.
     *
     * @param  components
     *         The action row components, such as {@link Button Buttons}
     *
     * @throws IllegalArgumentException
     *         If null is provided, an invalid number of components is provided,
     *         or any custom {@link ActionComponent#getId() id} is duplicated
     *
     * @return The same update action, for chaining convenience
     *
     * @see    ActionRow#of(ItemComponent...)
     */
    @Nonnull
    @CheckReturnValue
    default MessageEditCallbackAction setActionRow(@Nonnull ItemComponent... components)
    {
        return setActionRows(ActionRow.of(components));
    }

    /**
     * Set only one action row for convenience.
     *
     * @param  components
     *         The action row components, such as {@link Button Buttons}
     *
     * @throws IllegalArgumentException
     *         If null is provided, an invalid number of components is provided,
     *         or any custom {@link ActionComponent#getId() id} is duplicated
     *
     * @return The same update action, for chaining convenience
     *
     * @see    ActionRow#of(Collection)
     */
    @Nonnull
    @CheckReturnValue
    default MessageEditCallbackAction setActionRow(@Nonnull Collection<? extends ItemComponent> components)
    {
        return setActionRows(ActionRow.of(components));
    }

    /**
     * Adds the provided {@link File}.
     * <br><u>The stream will be closed upon execution!</u>
     * <br>The provided file will be appended to the message.
     *
     * @param  file
     *         The {@link File} data to upload in response to the interaction.
     * @param  options
     *         Possible options to apply to this attachment, such as marking it as spoiler image
     *
     * @throws java.lang.IllegalArgumentException
     *         If the provided file is {@code null}.
     *
     * @return The same update action, for chaining convenience
     */
    @Nonnull
    @CheckReturnValue
    default MessageEditCallbackAction addFile(@Nonnull File file, @Nonnull AttachmentOption... options)
    {
        Checks.notNull(file, "File");
        return addFile(file, file.getName(), options);
    }

    /**
     * Adds the provided {@link File}.
     * <br><u>The stream will be closed upon execution!</u>
     * <br>The provided file will be appended to the message.
     *
     * <p>The {@code name} parameter is used to inform Discord about what the file should be called. This is 2 fold:
     * <ol>
     *     <li>The file name provided is the name that is found in {@link net.dv8tion.jda.api.entities.Message.Attachment#getFileName()}
     *          after upload and it is the name that will show up in the client when the upload is displayed.
     *     <br>Note: The fileName does not show up on the Desktop client for images. It does on mobile however.</li>
     *     <li>The extension of the provided fileName also determines how Discord will treat the file. Discord currently only
     *         has special handling for image file types, but the fileName's extension must indicate that it is an image file.
     *         This means it has to end in something like .png, .jpg, .jpeg, .gif, etc. As a note, you can also not provide
     *         a full name for the file and instead ONLY provide the extension like "png" or "gif" and Discord will generate
     *         a name for the upload and append the fileName as the extension.</li>
     * </ol>
     *
     * @param  file
     *         The {@link File} data to upload in response to the interaction.
     * @param  name
     *         The file name that should be sent to discord
     * @param  options
     *         Possible options to apply to this attachment, such as marking it as spoiler image
     *
     * @throws java.lang.IllegalArgumentException
     *         If the provided file or filename is {@code null}.
     *
     * @return The same update action, for chaining convenience
     */
    @Nonnull
    @CheckReturnValue
    default MessageEditCallbackAction addFile(@Nonnull File file, @Nonnull String name, @Nonnull AttachmentOption... options)
    {
        try
        {
            Checks.notNull(file, "File");
            Checks.check(file.exists() && file.canRead(), "Provided file either does not exist or cannot be read from!");
            return addFile(new FileInputStream(file), name, options);
        }
        catch (FileNotFoundException e)
        {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Adds the provided {@code byte[]} as file data.
     * <br><u>The stream will be closed upon execution!</u>
     * <br>The provided file will be appended to the message.
     *
     * @param  data
     *         The {@code byte[]} data to upload in response to the interaction.
     * @param  name
     *         The file name that should be sent to discord
     *         <br>Refer to the documentation for {@link #addFile(java.io.File, String, AttachmentOption...)} for information about this parameter.
     * @param  options
     *         Possible options to apply to this attachment, such as marking it as spoiler image
     *
     * @throws java.lang.IllegalArgumentException
     *         If the provided data or filename is {@code null}.
     *
     * @return The same update action, for chaining convenience
     */
    @Nonnull
    @CheckReturnValue
    default MessageEditCallbackAction addFile(@Nonnull byte[] data, @Nonnull String name, @Nonnull AttachmentOption... options)
    {
        Checks.notNull(data, "Data");
        return addFile(new ByteArrayInputStream(data), name, options);
    }

    /**
     * Adds the provided {@link java.io.InputStream InputStream} as file data.
     * <br><u>The stream will be closed upon execution!</u>
     * <br>The provided file will be appended to the message.
     *
     * @param  data
     *         The InputStream data to upload in response to the interaction.
     * @param  name
     *         The file name that should be sent to discord
     *         <br>Refer to the documentation for {@link #addFile(java.io.File, String, AttachmentOption...)} for information about this parameter.
     * @param  options
     *         Possible options to apply to this attachment, such as marking it as spoiler image
     *
     * @throws java.lang.IllegalArgumentException
     *         If the provided data or filename is {@code null}.
     *
     * @return The same update action, for chaining convenience
     */
    @Nonnull
    @CheckReturnValue
    MessageEditCallbackAction addFile(@Nonnull InputStream data, @Nonnull String name, @Nonnull AttachmentOption... options);

    /**
     * Removes all attachments that are currently attached to the existing message except for the ones provided.
     * <br>For example {@code retainFilesById(Arrays.asList("123"))} would remove all attachments except for the one with the id 123.
     *
     * <p>To remove all attachments from the message you can pass an empty list.
     *
     * @param  ids
     *         The ids for the attachments which should be retained on the message
     *
     * @throws IllegalArgumentException
     *         If any of the ids is null or not a valid snowflake
     *
     * @return The same update action, for chaining convenience
     */
    @Nonnull
    @CheckReturnValue
    MessageEditCallbackAction retainFilesById(@Nonnull Collection<String> ids);

    /**
     * Removes all attachments that are currently attached to the existing message except for the ones provided.
     * <br>For example {@code retainFilesById(Arrays.asList("123"))} would remove all attachments except for the one with the id 123.
     *
     * <p>To remove all attachments from the message you can pass an empty list.
     *
     * @param  ids
     *         The ids for the attachments which should be retained on the message
     *
     * @throws IllegalArgumentException
     *         If any of the ids is null or not a valid snowflake
     *
     * @return The same update action, for chaining convenience
     */
    @Nonnull
    @CheckReturnValue
    default MessageEditCallbackAction retainFilesById(@Nonnull String... ids)
    {
        Checks.notNull(ids, "IDs");
        return retainFilesById(Arrays.asList(ids));
    }

    /**
     * Removes all attachments that are currently attached to the existing message except for the ones provided.
     * <br>For example {@code retainFilesById(Arrays.asList("123"))} would remove all attachments except for the one with the id 123.
     *
     * <p>To remove all attachments from the message you can pass an empty list.
     *
     * @param  ids
     *         The ids for the attachments which should be retained on the message
     *
     * @throws IllegalArgumentException
     *         If any of the ids is null or not a valid snowflake
     *
     * @return The same update action, for chaining convenience
     */
    @Nonnull
    @CheckReturnValue
    default MessageEditCallbackAction retainFilesById(long... ids)
    {
        Checks.notNull(ids, "IDs");
        return retainFilesById(Arrays
                .stream(ids)
                .mapToObj(Long::toUnsignedString)
                .collect(Collectors.toList())
        );
    }

    /**
     * Removes all attachments that are currently attached to the existing message except for the ones provided.
     * <br>For example {@code retainFiles(message.getAttachments().subList(1, message.getAttachments().size()))} would only remove the first attachment from the message.
     *
     * <p>To remove all attachments from the message you can pass an empty list.
     *
     * @param  attachments
     *         The attachments which should be retained on the message
     *
     * @throws IllegalArgumentException
     *         If any of the ids is null or not a valid snowflake
     *
     * @return The same update action, for chaining convenience
     */
    @Nonnull
    @CheckReturnValue
    default MessageEditCallbackAction retainFiles(@Nonnull Collection<? extends Message.Attachment> attachments)
    {
        Checks.noneNull(attachments, "Attachments");
        return retainFilesById(attachments
                .stream()
                .map(Message.Attachment::getId)
                .collect(Collectors.toList())
        );
    }

    /**
     * Removes all attachments that are currently attached to the existing message except for the ones provided.
     * <br>For example {@code retainFiles(message.getAttachments().subList(1, message.getAttachments().size()))} would only remove the first attachment from the message.
     *
     * <p>To remove all attachments from the message you can pass an empty list.
     *
     * @param  attachments
     *         The attachments which should be retained on the message
     *
     * @throws IllegalArgumentException
     *         If any of the ids is null or not a valid snowflake
     *
     * @return The same update action, for chaining convenience
     */
    @Nonnull
    @CheckReturnValue
    default MessageEditCallbackAction retainFiles(@Nonnull Message.Attachment... attachments)
    {
        Checks.noneNull(attachments, "Attachments");
        return retainFiles(Arrays.asList(attachments));
    }
}
