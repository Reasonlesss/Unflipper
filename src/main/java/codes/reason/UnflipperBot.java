package codes.reason;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

// Made to automatically unflip, flipped tables.
// Tables do not deserve the hate that they must
// endure from evil people.
public class UnflipperBot {

    private static final String[] FLIP_VARIANTS = new String[] {
            "┻", "┸"
    };
    private static final String UNFLIP_STRING = "┬─┬ ノ( ゜-゜ノ)";

    public static void main(String[] args) {
        // Whoever is running the bot forgot the token in the
        // launch arguments and should probably be shot on the spot.
        if (args.length == 0) {
            System.err.println("Someone forgot to provide a bot token, pretty stupid tbh");
            System.exit(0);
            return;
        }

        final String token = args[0];

        // Create the actual bot instance
        final JDA jda = JDABuilder.createLight(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();


        // Listen for potential abuse and correct things :)
        jda.addEventListener(new ListenerAdapter() {

            @Override
            public void onMessageReceived(@NotNull MessageReceivedEvent event) {
                final String message = event.getMessage().getContentRaw();
                int flips = isFlipped(message);
                if (flips > 0) {
                    String repeated = ("  " + UNFLIP_STRING).repeat(flips).substring(1);
                    event.getMessage().reply(repeated).queue();
                }

            }

            @Override
            public void onMessageUpdate(@NotNull MessageUpdateEvent event) {
                final String message = event.getMessage().getContentRaw();
                int flips = isFlipped(message);
                if (flips > 0) {
                    String repeated = ("  " + UNFLIP_STRING).repeat(flips).substring(1);
                    event.getMessage().reply(repeated).queue();
                }
            }
        });

    }

    /**
     * Checks if a poor table has been flipped over, due to people
     * abusing MULTIPLE tables in the same message (unacceptable) I have
     * decided to correct multiple unflipped tables.
     */
    private static int isFlipped(String content) {
        boolean flip = true;
        int totalOccurrences = 0;

        for (String variant : FLIP_VARIANTS) {
            int variantFlips = 0;
            while (content.contains(variant)) {
                content = content.replaceFirst(Pattern.quote(variant), "");
                variantFlips++;
            }
            totalOccurrences += variantFlips;
        }

        return totalOccurrences / 2;
    }

}