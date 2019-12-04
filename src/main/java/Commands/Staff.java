package Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.Color;
import java.util.List;

public class Staff {

    public static void sendStaff(MessageReceivedEvent event) {
        List<Member> headAdmins = event.getGuild().getMembersWithRoles(event.getGuild().getRolesByName("Head Administrators", false));
        List<Member> seniorAdmins = event.getGuild().getMembersWithRoles(event.getGuild().getRolesByName("Senior Administrators", false));
        List<Member> admins = event.getGuild().getMembersWithRoles(event.getGuild().getRolesByName("Administrators", false));
        List<Member> mods = event.getGuild().getMembersWithRoles(event.getGuild().getRolesByName("Moderators", false));
        List<Member> tradeMods = event.getGuild().getMembersWithRoles(event.getGuild().getRolesByName("Trade Server Moderators", false));

        EmbedBuilder staff = new EmbedBuilder();
        staff.setTitle("FirePowered Staff");
        staff.setDescription("Current Staff Roster");
        staff.setColor(Color.RED);

        StringBuilder stringBuilder = new StringBuilder();
        for (Member member : headAdmins) {
            stringBuilder.append(String.format("%s\n", member.getEffectiveName()));
        }
        staff.addField("Head Administrators", stringBuilder.toString(), false);

        stringBuilder.setLength(0);
        for (Member member : seniorAdmins) {
            stringBuilder.append(String.format("%s\n", member.getEffectiveName()));
        }
        staff.addField("Senior Administrators", stringBuilder.toString(), false);

        stringBuilder.setLength(0);
        for (Member member : admins) {
            stringBuilder.append(String.format("%s\n", member.getEffectiveName()));
        }
        staff.addField("Administrators", stringBuilder.toString(), false);

        stringBuilder.setLength(0);
        for (Member member : mods) {
            stringBuilder.append(String.format("%s\n", member.getEffectiveName()));
        }
        staff.addField("Moderators", stringBuilder.toString(), false);

        stringBuilder.setLength(0);
        for (Member member : tradeMods) {
            stringBuilder.append(String.format("%s\n", member.getEffectiveName()));
        }
        staff.addField("Trade Server Moderators", stringBuilder.toString(), false);

        event.getChannel().sendMessage(staff.build()).queue();
        staff.clear();
    }
}
