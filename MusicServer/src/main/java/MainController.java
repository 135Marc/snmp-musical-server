import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainController {

    // Agent-Side
    private MainView mview;
    private AgentPrototype agentPrototype;
    private Agent mainAgent;
    private List<Agent> managedAgents = new ArrayList<>();
    private AgentUtilities mainAgentUtilities;
    private MusicInfo selectedMusic;
    private AudioPlayer audioPlayer;
    private ConfigAdder configAdder;
    private ConfigEditor configEditor;
    private ConfigManager configManager;
    private AgentMusicServer agentMusicServer;
    // Manager-Side
    private ClientMainDisplay clientMainDisplay;
    private ClientOptionsDisplay clientOptionsDisplay;
    private AgentAddDisplay agentAddDisplay;
    private ClientMusicServer clientMusicServer;
    private ClientAudioPlayer clientAudioPlayer;
    private ClientStatisticDisplay clientStatisticDisplay;
    private ClientSettingsEditor clientSettingsEditor;

    public MainController(MainView mv) {
        this.mview = mv;
    }

    public void setAgentPrototype(AgentPrototype agentPrototype) {
        this.agentPrototype = agentPrototype;
    }

    public void setAudioPlayer(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    public void setConfigAdder(ConfigAdder configAdder) {
        this.configAdder = configAdder;
    }

    public void setConfigEditor(ConfigEditor configEditor) {
        this.configEditor = configEditor;
    }

    public void setConfigManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public void setClientMainDisplay(ClientMainDisplay clientMainDisplay) {
        this.clientMainDisplay = clientMainDisplay;
    }

    public void setClientOptionsDisplay(ClientOptionsDisplay clientOptionsDisplay) {
        this.clientOptionsDisplay = clientOptionsDisplay;
    }

    public void setClientStatisticDisplay(ClientStatisticDisplay clientStatisticDisplay) {
        this.clientStatisticDisplay = clientStatisticDisplay;
    }

    public void setSelectedMusic(MusicInfo mi) {
        this.selectedMusic = mi;
    }

    public void setAgentMusicServer(AgentMusicServer ams) {
        this.agentMusicServer = ams;
    }

    public void setMainAgent(Agent a) {
        this.mainAgent = a;
    }

    public void setAgentAddDisplay(AgentAddDisplay agentAddDisplay) {
        this.agentAddDisplay = agentAddDisplay;
    }

    public void setManagedAgents(List<Agent> agents) {
        if (this.managedAgents.size()!=0) this.managedAgents.clear();
        this.managedAgents.addAll(agents);
    }

    public void setMainAgentUtilities(AgentUtilities mainAU) {
        this.mainAgentUtilities = mainAU;
    }

    public void setClientMusicServer(ClientMusicServer clientMusicServer) {
        this.clientMusicServer = clientMusicServer;
    }

    public void setClientAudioPlayer(ClientAudioPlayer clientAudioPlayer) {
        this.clientAudioPlayer = clientAudioPlayer;
    }

    public void setClientSettingsEditor(ClientSettingsEditor cse) {
        this.clientSettingsEditor=cse;
    }

    public void initMainAgent(String ip, int port, String community_string, boolean multithreaded, int threads) throws Exception {
        this.mainAgent = new Agent(ip,port,community_string,multithreaded,threads);
    }

    public List<Agent> getManagedAgents() {
        return this.managedAgents;
    }

    public AgentUtilities getMainAgentUtilities() {
        return mainAgentUtilities;
    }

    public MainView getMainView() {
        return mview;
    }

    public MusicInfo getSelectedMusic() {
        return this.selectedMusic;
    }

    public Agent getMainAgent() {
        return this.mainAgent;
    }

}
