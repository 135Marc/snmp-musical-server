import org.snmp4j.agent.DefaultMOServer;
import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.mo.*;
import org.snmp4j.smi.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class AgentUtilities {

    private Agent agent;
    private MOTableBuilder music_table_builder;
    private MOTableBuilder artist_table_builder;
    private MOTableBuilder album_table_builder;
    private MOTableBuilder genre_table_builder;
    private MOTable music_table;
    private MOTable artist_table;
    private MOTable album_table;
    private MOTable genre_table;
    private MOScalar refresh_period;
    private MOScalar last_refresh;

    private Counter32 listen_counter;
    private OID refreshPeriod;
    private OID lastRefresh;
    private Map<String,Integer> albumMap;
    private Map<String,Integer> artistMap;
    private Map<String,Integer> genreMap;
    private Map<Integer,MusicInfo> musicMap = new HashMap<>();



    public AgentUtilities() {
        refreshPeriod = new OID("1.3.6.1.3.2020.1.1.0");
        lastRefresh = new OID("1.3.6.1.3.2020.1.2.0");
        music_table_builder = new MOTableBuilder(new OID("1.3.6.1.3.2020.1.3.1"));
        album_table_builder = new MOTableBuilder(new OID("1.3.6.1.3.2020.1.4.1"));
        genre_table_builder = new MOTableBuilder(new OID("1.3.6.1.3.2020.1.5.1"));
        artist_table_builder = new MOTableBuilder(new OID("1.3.6.1.3.2020.1.6.1"));
        this.albumMap = new HashMap<>();
        this.artistMap = new HashMap<>();
        this.genreMap = new HashMap<>();
        this.listen_counter = new Counter32(0);
    }

    public void init(Agent agent) throws Exception {
        this.agent = agent;
        this.agent.start();
        this.agent.unregisterManagedObject(agent.getSnmpv2MIB());
        List<MusicInfo> musics = agent.getMusicList();
        buildMIBTables();
        loadMIBTables(musics);
        registerManagedObjects();
    }
        // Setup the client to use our newly started agent client = new SNMPManager("udp:127.0.0.1/"+agente.getPorta());

    public void buildMIBTables() {
        // Build Music Table
        music_table_builder.addColumn(SMIConstants.SYNTAX_UNSIGNED_INTEGER32,MOAccessImpl.ACCESS_READ_ONLY);
        music_table_builder.addColumn(SMIConstants.SYNTAX_OCTET_STRING,MOAccessImpl.ACCESS_READ_WRITE);
        music_table_builder.addColumn(SMIConstants.SYNTAX_UNSIGNED_INTEGER32,MOAccessImpl.ACCESS_READ_WRITE);
        music_table_builder.addColumn(SMIConstants.SYNTAX_UNSIGNED_INTEGER32,MOAccessImpl.ACCESS_READ_WRITE);
        music_table_builder.addColumn(SMIConstants.SYNTAX_UNSIGNED_INTEGER32,MOAccessImpl.ACCESS_READ_WRITE);
        music_table_builder.addColumn(SMIConstants.SYNTAX_UNSIGNED_INTEGER32,MOAccessImpl.ACCESS_READ_WRITE);
        music_table_builder.addColumn(SMIConstants.SYNTAX_UNSIGNED_INTEGER32,MOAccessImpl.ACCESS_READ_WRITE);
        music_table_builder.addColumn(SMIConstants.SYNTAX_COUNTER32,MOAccessImpl.ACCESS_READ_WRITE);
        // Build Artist Table
        artist_table_builder.addColumn(SMIConstants.SYNTAX_UNSIGNED_INTEGER32,MOAccessImpl.ACCESS_READ_ONLY);
        artist_table_builder.addColumn(SMIConstants.SYNTAX_OCTET_STRING,MOAccessImpl.ACCESS_READ_WRITE);
        // Build Album Table
        album_table_builder.addColumn(SMIConstants.SYNTAX_UNSIGNED_INTEGER32,MOAccessImpl.ACCESS_READ_ONLY);
        album_table_builder.addColumn(SMIConstants.SYNTAX_OCTET_STRING,MOAccessImpl.ACCESS_READ_WRITE);
        // Build Genre Table
        genre_table_builder.addColumn(SMIConstants.SYNTAX_UNSIGNED_INTEGER32,MOAccessImpl.ACCESS_READ_ONLY);
        genre_table_builder.addColumn(SMIConstants.SYNTAX_OCTET_STRING,MOAccessImpl.ACCESS_READ_WRITE);
    }

    public void loadMIBTables(List<MusicInfo> musics) {
        for (int j=1;j<musics.size()+1;j++) {
            this.musicMap.put(j,musics.get(j-1));
        }
        int i = 1;
        for (MusicInfo music : musics) {
            music_table_builder = music_table_builder.addVariable(new UnsignedInteger32(i));
            music_table_builder = music_table_builder.addVariable(new OctetString(music.getName()));
            String artist = music.getArtist();
            String album = music.getAlbum();
            String genre = music.getGenre();
            int year = Integer.parseInt(music.getYear());
            int time = (int) Math.round(Double.parseDouble(music.getDuration()));
            if (!artistMap.containsValue(i)) {
                if (!artistMap.containsKey(artist)) {
                    artistMap.put(artist, i);
                    music_table_builder = music_table_builder.addVariable(new UnsignedInteger32(i));
                } else {
                    int id = artistMap.get(artist);
                    music_table_builder = music_table_builder.addVariable(new UnsignedInteger32(id));
                }
            }
            if (!albumMap.containsValue(i)) {
                if (!albumMap.containsKey(album)) {
                    albumMap.put(album, i);
                    music_table_builder = music_table_builder.addVariable(new UnsignedInteger32(i));
                } else {
                    int id = albumMap.get(album);
                    music_table_builder = music_table_builder.addVariable(new UnsignedInteger32(id));
                }
            }
            music_table_builder = music_table_builder.addVariable(new UnsignedInteger32(year));
            if (!genreMap.containsValue(i)) {
                if (!genreMap.containsKey(genre)) {
                    genreMap.put(genre, i);
                    music_table_builder = music_table_builder.addVariable(new UnsignedInteger32(i));
                } else {
                    int id = genreMap.get(genre);
                    music_table_builder = music_table_builder.addVariable(new UnsignedInteger32(id));
                }
            }
            music_table_builder = music_table_builder.addVariable(new UnsignedInteger32(time));
            music_table_builder = music_table_builder.addVariable(listen_counter);
            i++;
        }

        music_table = music_table_builder.build();

        Set<String> artistNames = artistMap.keySet();
        for (String artist : artistNames) {
            int artistID = artistMap.get(artist);
            artist_table_builder = artist_table_builder.addVariable(new UnsignedInteger32(artistID));
            artist_table_builder = artist_table_builder.addVariable(new OctetString(artist));
        }
        Set<String> albumNames = albumMap.keySet();
        for (String album : albumNames) {
            int albumID = albumMap.get(album);
            album_table_builder = album_table_builder.addVariable(new UnsignedInteger32(albumID));
            album_table_builder = album_table_builder.addVariable(new OctetString(album));
        }
        Set<String> genreNames = genreMap.keySet();
        for (String genre : genreNames) {
            int genreID = genreMap.get(genre);
            genre_table_builder = genre_table_builder.addVariable(new UnsignedInteger32(genreID));
            genre_table_builder = genre_table_builder.addVariable(new OctetString(genre));
        }


        artist_table = artist_table_builder.build();
        album_table = album_table_builder.build();
        genre_table = genre_table_builder.build();
    }

    public void registerManagedObjects() throws DuplicateRegistrationException {
        refresh_period = MOScalarBuilder.createReadWrite(refreshPeriod, new UnsignedInteger32(30));
        agent.registerManagedObject(refresh_period);
        last_refresh = MOScalarBuilder.createReadWrite(lastRefresh,LocalDateTime.now().toString());
        agent.registerManagedObject(last_refresh);
        agent.registerManagedObject(music_table);
        agent.registerManagedObject(artist_table);
        agent.registerManagedObject(album_table);
        agent.registerManagedObject(genre_table);
        agent.unregisterManagedObjects();

    }

    public void updateRefresh(String period) {
        refresh_period.setValue(new UnsignedInteger32(Integer.parseInt(period)));
        last_refresh.setValue(new OctetString(LocalDateTime.now().toString()));
    }

    public void updateMusicCounter(long counter32,int position) {
        int pos = position +1;
        VariableBinding vb = new VariableBinding();
        vb.setOid(new OID("1.3.6.1.3.2020.1.3.1.8" + "." + pos));
        vb.setVariable(new Counter32(counter32));
        music_table.setValue(vb);
    }

}
