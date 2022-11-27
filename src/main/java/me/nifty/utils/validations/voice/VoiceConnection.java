package me.nifty.utils.validations.voice;

import me.nifty.utils.validations.permissions.VoicePermissions;
import me.nifty.utils.VoiceUtils;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

import java.util.Objects;
import java.util.stream.Stream;

public class VoiceConnection {

    /**
     * Checks if the member is in a voice channel and if the bot is available.
     * @param memberVoiceState The voice state of the member to check.
     * @param selfVoiceState The voice state of the bot.
     * @return The error message if any, null if no error.
     */
    public static String validate(GuildVoiceState memberVoiceState, GuildVoiceState selfVoiceState) {

        // Check if the member is connected to a voice channel
        if (memberVoiceState == null || !memberVoiceState.inAudioChannel()) {
            return "You have to be connected to a voice channel before you can use this command!";
        }

        // Check if the bot is connected to a voice channel
        if (selfVoiceState != null && selfVoiceState.inAudioChannel()) {

            // Check if the bot and the member are connected to the same voice channel
            if (!Objects.equals(selfVoiceState.getChannel(), memberVoiceState.getChannel())) {

                // Gets the list of members connected to the bot voice channel, excluding bots
                Stream<Member> connectedMembers = Objects.requireNonNull(selfVoiceState.getChannel()).getMembers().stream().filter(member -> !member.getUser().isBot());

                // Checks if the bot is not alone in a voice channel
                if (connectedMembers.findAny().isPresent()) {
                    return "Someone else is already listening to music in different channel!";
                }

            }

        } else {

            // If the bot is not connected to a voice channel. Connects to the member voice channel

            // Gets the voice channel to connect
            VoiceChannel voiceChannel = Objects.requireNonNull(memberVoiceState.getChannel()).asVoiceChannel();

            // Validates the permissions of the bot over the voice channel
            String voicePermissionsError = VoicePermissions.validate(voiceChannel);

            // If the bot is missing some permission, returns the error message
            if (voicePermissionsError != null) {
                return voicePermissionsError;
            }

            return VoiceUtils.join(memberVoiceState.getChannel().asVoiceChannel());

        }

        return null;

    }

}
