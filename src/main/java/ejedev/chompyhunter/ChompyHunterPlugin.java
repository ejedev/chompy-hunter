package ejedev.chompyhunter;
import com.google.inject.Provides;
import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.api.ChatMessageType;
import net.runelite.api.events.NpcDespawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import javax.inject.Inject;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.NPC;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayManager;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import net.runelite.api.events.ChatMessage;
import net.runelite.client.Notifier;


@PluginDescriptor(
        name = "Chompy Hunter",
        description = "A plugin to overlay chompy birds with a timer and colour based on remaining time till despawn. This is an updated version originally by ejedev.",
        tags = {"chompy", "bird", "hunt", "hunting", "chompies", "track", "count", "western"},
        loadWhenOutdated = true,
        enabledByDefault = false
)

public class ChompyHunterPlugin extends Plugin{

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private ChompyHunterConfig config;

    private static final Pattern Chompy_KC_REGEX = Pattern.compile("You've scratched up a total of.*");

    @Provides
    ChompyHunterConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(ChompyHunterConfig.class);
    }

    @Override
    protected void startUp()
    {
        overlayManager.add(overlay);
        overlayManager.add(overlayInfo);
        chompies.clear();
        ChompyKills = 0;
        ChompyTotalKills = 0;
        StartTime = null;
    }

    @Override
    protected void shutDown()
    {
        overlayManager.remove(overlay);
        overlayManager.remove(overlayInfo);
        chompies.clear();
        ChompyKills = 0;
        ChompyTotalKills = 0;
        StartTime = null;

    }

    @Getter(AccessLevel.PACKAGE)
    private final Map<Integer, Chompy> chompies = new HashMap<>();

    @Getter(AccessLevel.PACKAGE)
    private int ChompyKills;
    @Getter(AccessLevel.PACKAGE)
    private int ChompyTotalKills;

    @Getter(AccessLevel.PACKAGE)
    private Instant StartTime;

    @Inject
    private Client client;

    @Inject
    private ChompyHunterOverlay overlay;

    @Inject
    private ChompyHunterInfoOverlay overlayInfo;

    @Inject
    private Notifier notifier;

    @Subscribe
    public void onChatMessage(ChatMessage chatMessage) {
        if (chatMessage.getMessage().equals("You scratch a notch on your bow for the chompy bird kill.") && chatMessage.getType() == ChatMessageType.SPAM) {
            if (StartTime == null) {
                StartTime = Instant.now();
            }
            ChompyKills++;
            ChompyTotalKills++;
        }
        if (Chompy_KC_REGEX.matcher(chatMessage.getMessage()).matches() && chatMessage.getType() == ChatMessageType.GAMEMESSAGE) {
            ChompyTotalKills = Integer.parseInt(chatMessage.getMessage().replaceAll("[^0-9]", ""));
        }
    }

    @Subscribe
   private void onNpcSpawned(NpcSpawned event)
    {
        NPC npc = event.getNpc();

        if (npc == null)
        {
            return;
        }

        String name = event.getNpc().getName();

        if (name != null) {
            if (name.equals("Chompy bird") && !chompies.containsKey(npc.getIndex())) {
                chompies.put(npc.getIndex(), new Chompy(npc));
                if (config.notifyChompySpawn()) {
                    notifier.notify("A chompy has spawned!");
                }
            }
        }
    }

   @Subscribe
    private void onNpcDespawned(NpcDespawned event)
    {
        NPC npc = event.getNpc();
        String name = event.getNpc().getName();
        if (name != null) {
            if (name.equals("Chompy bird") && chompies.containsKey(npc.getIndex())) {
                chompies.remove(event.getNpc().getIndex());
            }
        }
    }
}
